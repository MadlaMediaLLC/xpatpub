package com.han.xpatpub.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.han.xpatpub.R;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.User;

public class SearchPatronActivity extends AbstractedActivity {
	
	public static final int REQUEST_CODE_COUPON = 1003;
	public static final int SEND_COUPON_BUTTON_INDEX = 5000;

	private CheckBox cbSelectAll;
	private LinearLayout llSelectAll;
	private ListView lstUser;	
	private ImageView imgProfile;
	private TextView txtUserName;
	private Button btnBack;
	private Button bSendCoupon;
	private ArrayList<User> tempPatrons = null;
	private UserAdapter adapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_patrons);
	    
		initWidget();		
		onReceive();
		initValue();
		initEvent();
	}
	
	private void initWidget() {
		cbSelectAll = (CheckBox) findViewById(R.id.cbSelectAll);
		llSelectAll = (LinearLayout) findViewById(R.id.llSelectAll);
		lstUser = (ListView) findViewById(R.id.user_listView);		
		imgProfile = (ImageView) findViewById(R.id.my_profile_imageView);
		txtUserName = (TextView) findViewById(R.id.myname_textView);
		btnBack = (Button) findViewById(R.id.pub_patron_back_button);
		bSendCoupon = (Button) findViewById(R.id.bSendCoupon);
	}
	
	private void initValue() {
//		txtUserName.setText(GlobalData.currentUser.firstName + " " + GlobalData.currentUser.lastName);
		txtUserName.setText(GlobalData.currentUser.userCoupons + " coupons remaining.");
		imgProfile.setImageBitmap(GlobalData.bmpProfile);
	}
	
	@Override
	public void onReceive() {
		ArrayList<User> patrons = GlobalData.getPatrons();
		if (tempPatrons == null || !tempPatrons.equals(patrons)) {
			adapter = new UserAdapter(this, R.layout.row_user, tempPatrons = patrons);
			lstUser.setAdapter(adapter);
		}
	}
	
	private void initEvent() {
		btnBack.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
        });
		
		bSendCoupon.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SearchPatronActivity.this.startActivity(new Intent(SearchPatronActivity.this, CouponActivity.class));				
			}
		});
		
		cbSelectAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				adapter.selectAll(isChecked);								
			}
		});
		
		llSelectAll.setOnClickListener(new LinearLayout.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cbSelectAll.performClick();				
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_COUPON) {
			if (resultCode == RESULT_OK) {
				setResult(RESULT_OK);
				finish();
			}
		}
	}
	
	public class UserAdapter extends ArrayAdapter<User> {
    	
    	ArrayList<User> arrData;
    	int row_id;

		public UserAdapter(Context context, int _row_id, ArrayList<User> _arrData) {
			super(context, _row_id, _arrData);
			row_id = _row_id;
			arrData = _arrData;
			CouponActivity.userList = new User[arrData.size()];
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        LayoutInflater inflater = SearchPatronActivity.this.getLayoutInflater();
	        row = inflater.inflate(row_id, parent, false);        
	        
	        TextView txtUserName = (TextView) row.findViewById(R.id.row_name_textView);
//	        TextView txtMsgDesc = (TextView) row.findViewById(R.id.advertise_type_textView);
//	        SmartImageView imgAdIcon = (SmartImageView) row.findViewById(R.id.pub_owner_icon_imageView);
//	        Button btnSendCoupon = (Button) row.findViewById(R.id.change_pub_owner_icon_button);    	        
//	        btnSendCoupon.setId(SEND_COUPON_BUTTON_INDEX + position);
//	        btnSendCoupon.setOnClickListener(new Button.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					int position = v.getId() - SEND_COUPON_BUTTON_INDEX;
//			        User user = arrData.get(position);
//			        CouponActivity.userSend = user;
//			        SearchPatronActivity.this.startActivity(new Intent(SearchPatronActivity.this, CouponActivity.class));
//				}
//	        });
	        
	        CheckBox cbSendCoupon = (CheckBox) row.findViewById(R.id.cbSendCoupon);
	        cbSendCoupon.setId(SEND_COUPON_BUTTON_INDEX + position);
	        cbSendCoupon.setChecked(CouponActivity.userList[position] != null ? true : false);
	        cbSendCoupon.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int position = buttonView.getId() - SEND_COUPON_BUTTON_INDEX;
					User user = arrData.get(position);		
					
					if (isChecked) {
						CouponActivity.userList[position] = user;
					
					} else {
						CouponActivity.userList[position] = null;
					}
				}
			});
	        
	        User user = arrData.get(position);
//	        Pub pub = GlobalData.getPub(message.msgPubID);
	        if (Integer.valueOf(user.userType) == User.USER_TYPE_PATRON) {
		        if (Integer.valueOf(user.userPrivacy) == User.USER_PRIVACY_ANONYMOUS) {
		        	txtUserName.setText("Anonymous");
		        	
		        } else if (Integer.valueOf(user.userPrivacy) == User.USER_PRIVACY_PUBLIC) {
		        	txtUserName.setText(user.userName);
		        }	        
	        }	               
			
	        return row;
		}
		
		public void selectAll(boolean select) {
			for (int i = 0; i < arrData.size(); i++) {
				if (select) {
					CouponActivity.userList[i] = arrData.get(i);
					
				} else {
					CouponActivity.userList[i] = null;
				}
			}
			
			notifyDataSetChanged();
		}
    }

    @Override
    protected void onResume() {
        super.onResume();
        initValue();
    }
}

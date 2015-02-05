package com.han.xpatpub.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.han.xpatpub.R;
import com.han.xpatpub.asynctasks.GeneralAsyncTask;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.Coupon;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.User;

public class CouponActivity extends Activity {

	public final static int SEND_COUPON_BUTTON_INDEX = 7000;
	public static User userSend = new User();
	public static ArrayList<User> arrUserSend = new ArrayList<User>();
	public static User[] userList;

	private CouponAdapter adapter;
	private ImageView imgProfile;
	private TextView txtUserName;
	private Button btnBack;

	private ListView lstCoupon;
	
	private static int lastPosition;	// TODO This can be done better with Handler in case of multiple sends at once.
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon);
	    
		initWidget();
		initValue();
		initEvent();
	}
	
	private void initWidget() {
		imgProfile = (ImageView) findViewById(R.id.my_profile_imageView);
		txtUserName = (TextView) findViewById(R.id.myname_textView);
		btnBack = (Button) findViewById(R.id.pub_patron_back_button);
		lstCoupon = (ListView) findViewById(R.id.coupon_listView);
	}
	
	private void initValue() {
		adapter = new CouponAdapter(this, GlobalData.arrMyCoupon);
		lstCoupon.setAdapter(adapter);		
//		txtUserName.setText(GlobalData.currentUser.firstName + " " + GlobalData.currentUser.lastName);
		txtUserName.setText(GlobalData.currentUser.userCoupons + " coupons remaining.");
		imgProfile.setImageBitmap(GlobalData.bmpProfile);
	}
	
	private void initEvent() {
//		btnCouponDesc1.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				showGeneralAlert("Sent Coupon", "Your offer has been sent!");
//			}
//        });
//		btnCouponDesc2.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				showGeneralAlert("Sent Coupon", "Your offer has been sent!");
//			}
//        });
		btnBack.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
        });
	}


    public class CouponAdapter extends ArrayAdapter<Coupon> {
    	
    	ArrayList<Coupon> arrData;
    	static final int layout_id = R.layout.row_coupon;

		public CouponAdapter(Context context, ArrayList<Coupon> _arrData) {
			super(context, layout_id, _arrData);
			// TODO Auto-generated constructor stub
//			row_id = _row_id;
			arrData = _arrData;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        LayoutInflater inflater = CouponActivity.this.getLayoutInflater();
	        row = inflater.inflate(layout_id, parent, false);
	        
	        TextView txtCouponName = (TextView) row.findViewById(R.id.row_name_textView);
	        TextView tvCouponDesc = (TextView) row.findViewById(R.id.tvCouponDescription);
//	        TextView tvCouponUsesLimit = (TextView) row.findViewById(R.id.tvCouponUsesLimit);

	        Button btnSendCoupon = (Button) row.findViewById(R.id.change_pub_owner_icon_button);
	        
	        btnSendCoupon.setId(SEND_COUPON_BUTTON_INDEX + position);

	        Coupon coupon = arrData.get(position);
	        txtCouponName.setText(coupon.couponName);
	        tvCouponDesc.setText(coupon.couponDescription);
//	        tvCouponUsesLimit.setText(Integer.toString(coupon.couponUsesLimit));
	        
	        btnSendCoupon.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = lastPosition = v.getId() - SEND_COUPON_BUTTON_INDEX;
					Coupon coupon = arrData.get(position);
					
//					String msgText = coupon.couponName;
//					String msgSenderId = GlobalData.currentUser.userID;
//					String msgReceiverId = userSend.userID;
//					String msgPubId = Integer.toString(GlobalData.ownPub.id);
//					String msgCouponId = Integer.toString(coupon.couponId);
//					String msgCouponUsesLimit = Integer.toString(coupon.couponUsesLimit);					
//					
////					new MyAsyncTask(CouponActivity.this).execute(MyAsyncTask.ACTION_CREATE_MESSAGE, msgSenderId, msgReceiverId, msgPubId, msgCouponId, msgCouponUsesLimit);
//					new GeneralAsyncTask(CouponActivity.this).execute(Action.OWNER_SEND_COUPON, msgText, msgSenderId, msgReceiverId, msgPubId, msgCouponId, msgCouponUsesLimit);
					
					String msgText = coupon.couponName;
					String msgSenderId = GlobalData.currentUser.userID;					
					String msgPubId = Integer.toString(GlobalData.ownPub.id);
					String msgCouponId = Integer.toString(coupon.couponId);
					String msgCouponUsesLimit = Integer.toString(coupon.couponUsesLimit);					
					
					for (User user : userList) {
						if (user != null) {
							String msgReceiverId = user.userID;
							new GeneralAsyncTask(CouponActivity.this)
								.execute(Action.OWNER_SEND_COUPON, msgText, msgSenderId, msgReceiverId, msgPubId, msgCouponId, msgCouponUsesLimit);
						}
					}

				}
	        });
			
	        return row;
		}
		
		public void decreaseCouponUsesLimit(int position) {
			arrData.get(position).couponUsesLimit--;
			this.notifyDataSetChanged();

		}
    }

    private void updateUserCoupons(int newCoupon) {
        GlobalData.currentUser.userCoupons = String.valueOf(newCoupon);
        txtUserName.setText(String.valueOf(newCoupon) + " coupons remaining.");
    }

    public void succesfulCouponSent() {
		adapter.decreaseCouponUsesLimit(lastPosition);	 // TODO
		Toast.makeText(this, "Coupon sent", Toast.LENGTH_SHORT).show();
        updateUserCoupons(Integer.valueOf(GlobalData.currentUser.userCoupons)-1);
	}
}

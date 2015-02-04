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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.han.xpatpub.R;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Message;
import com.squareup.picasso.Picasso;

public class InboxActivity extends AbstractedActivity {

//	Button btnPubName;
	private ArrayList<Message> arrData;
	private ListView lstInbox;
	
	private ImageView imgProfile;
	private TextView txtUserName;
	private Button btnBack;
	
	public static final int USE_BUTTON_INDEX = 4000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox);
	    
		initWidget();
		onReceive();
		initEvent();
	}

	@Override
	protected void onResume() {
		super.onResume();
		arrData = GlobalData.getUsableCoupons();
		lstInbox.setAdapter(new MessageAdapter(this, R.layout.row_advertise_1,
				arrData));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		arrData = null;
		lstInbox.setAdapter(null);
	}	
	
	private void initWidget() {
//		btnPubName = (Button) findViewById(R.id.inbox_pub_name_button);
		lstInbox = (ListView) findViewById(R.id.user_listView);
		
		imgProfile = (ImageView) findViewById(R.id.my_profile_imageView);
		txtUserName = (TextView) findViewById(R.id.myname_textView);
		btnBack = (Button) findViewById(R.id.pub_patron_back_button);
	}
	
	@Override
	public void onReceive() {		
		txtUserName.setText(GlobalData.currentUser.firstName + " " + GlobalData.currentUser.lastName);
		imgProfile.setImageBitmap(GlobalData.bmpProfile);
	}
	
	private void initEvent() {
        btnBack.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
        });
//		btnPubName.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				InboxActivity.this.startActivity(new Intent(InboxActivity.this, MessageActivity.class));
//			}
//        });
	}
	
	public class MessageAdapter extends ArrayAdapter<Message> {   			
    	int row_id;

		public MessageAdapter(Context context, int _row_id, ArrayList<Message> _arrData) {
			super(context, _row_id, _arrData);			
			row_id = _row_id;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        LayoutInflater inflater = InboxActivity.this.getLayoutInflater();
	        row = inflater.inflate(row_id, parent, false);
	        
	        TextView txtMsgTitle = (TextView) row.findViewById(R.id.row_name_textView);
	        TextView txtMsgDesc = (TextView) row.findViewById(R.id.row_desc_textView);
	        ImageView imgAdIcon = (ImageView) row.findViewById(R.id.pub_owner_icon_imageView);
	        Button btnUse = (Button) row.findViewById(R.id.change_pub_owner_icon_button);
	        
	        btnUse.setId(USE_BUTTON_INDEX + position);
	        btnUse.setText("USE");

	        Message message = arrData.get(position);
//	        Pub pub = GlobalData.getPub(message.msgPubID);
	        txtMsgTitle.setText(message.ownPub.name);
	        txtMsgDesc.setText(message.ownCoupon.couponDescription);
	        Picasso.with(InboxActivity.this).load(message.ownPub.iconUrl).into(imgAdIcon);

	        btnUse.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = v.getId() - USE_BUTTON_INDEX;
			        Message message = arrData.get(position);
//			        Pub pub = GlobalData.getPub(message.msgPubID);
//			        MessageActivity.pub = pub;
			        MessageActivity.curMessage = message;
			        InboxActivity.this.startActivity(new Intent(InboxActivity.this, MessageActivity.class));
				}
	        });
			
	        return row;
		}
    }
}

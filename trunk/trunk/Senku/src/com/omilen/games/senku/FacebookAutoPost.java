package com.omilen.games.senku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.Util;
import com.facebook.stream.Session;
import com.omilen.social.LoginButton;
import com.omilen.social.SessionEvents;
import com.omilen.social.SessionEvents.AuthListener;
import com.omilen.social.SessionEvents.LogoutListener;


public class FacebookAutoPost extends Activity implements OnClickListener {

    // Your Facebook Application ID must be set before running this example
    // See http://www.facebook.com/developers/createapp.php
    public static final String APP_ID = "";

    private LoginButton mLoginButton;
    private TextView mText;
    private Button closeButton;
   // private Dispatcher dispatcher;
    private Facebook mFacebook;
    private AsyncFacebookRunner mAsyncRunner;
   // private final StreamHandler streamHandler = new StreamHandler();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (APP_ID == null) {
            Util.showAlert(this, "Warning", "Facebook Applicaton ID must be specified before running this code");
        }
        StoreProperties.setContext(this);
        int facebookPost =Integer.valueOf(StoreProperties.getInstance().getProperty("facebook")); 
        if(facebookPost==-1){
        	showConfirmPostOnFacebookDialog();
        	facebookPost =Integer.valueOf(StoreProperties.getInstance().getProperty("facebook"));
        }
        if(facebookPost == 0){
        	this.finish();
        }
        
        setContentView(R.layout.facebook);
        mLoginButton = (LoginButton) findViewById(R.id.login);
        closeButton = (Button) findViewById(R.id.FacebookButtonClose);
        closeButton.setOnClickListener(this);
        mText = (TextView) FacebookAutoPost.this.findViewById(R.id.txt);
       
       	mFacebook = new Facebook(APP_ID);
       	mAsyncRunner = new AsyncFacebookRunner(mFacebook);

        //Session.restore(mFacebook, this);
       	Session.restore(this);       	
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());
        String[] permissions= new String[1];
        permissions[0] = "publish_stream";
        
        Bundle bund =  this.getIntent().getExtras();
        String message = "";
        if(bund!=null){        	
        	message = bund.get("message").toString();
        }
        
        mLoginButton.init(this, mAsyncRunner,message, mFacebook,permissions);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        mFacebook.authorizeCallback(requestCode, resultCode, data);
    }

    public class SampleAuthListener implements AuthListener {

        public void onAuthSucceed() {
        	Resources res = getResources();
            String wait = res.getString(R.string.facebook_please_wait);
            mText.setText(wait);            
        }

        public void onAuthFail(String error) {
            mText.setText("Login Failed: " + error);
            closeButton.setVisibility(View.VISIBLE);
        }
    }

    public class SampleLogoutListener implements LogoutListener {
        public void onLogoutBegin() {
            mText.setText("Logging out...");
        }

        public void onLogoutFinish() {
            mText.setText("You have logged out! ");           
        }
    }
    
   

	@Override
	public void onClick(View v) {
		this.finish();
		
	}

	private class ConfirmPostOnFacebookListener implements android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON1: {				
				StoreProperties.getInstance().setProperty("facebook", "1");
				return;
			}
			case AlertDialog.BUTTON2: {				
				StoreProperties.getInstance().setProperty("facebook", "0");
				return;
			}
			}

		}
    	
    
    }
}

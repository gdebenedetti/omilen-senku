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
import android.widget.ProgressBar;
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
    public static final String APP_ID = "123736441033967";

    private LoginButton mLoginButton;
    private TextView mText;
    private Button closeButton;
   // private Dispatcher dispatcher;
    private Facebook mFacebook;
    private AsyncFacebookRunner mAsyncRunner;
   // private final StreamHandler streamHandler = new StreamHandler();

    protected String[] permissions= new String[1];
    protected String message = "";
    protected ProgressBar progressbar = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (APP_ID == null) {
            Util.showAlert(this, "Warning", "Facebook Applicaton ID must be specified before running this code");
        }
        setContentView(R.layout.facebook);
        mLoginButton = (LoginButton) findViewById(R.id.login);
        closeButton = (Button) findViewById(R.id.FacebookButtonClose);
        closeButton.setOnClickListener(this);
        progressbar = (ProgressBar)findViewById(R.id.ProgressBarFacebookPost);
        mText = (TextView) FacebookAutoPost.this.findViewById(R.id.txt);

       	mFacebook = new Facebook(APP_ID);
       	mAsyncRunner = new AsyncFacebookRunner(mFacebook);
        //Session.restore(mFacebook, this);
       	Session.restore(this);
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());        
        progressbar.setVisibility(View.INVISIBLE);
        
        permissions[0] = "publish_stream";
        Bundle bund =  this.getIntent().getExtras();
        int enabled = 0;
        if(bund!=null){
        	message = bund.getString("message");
        	enabled = bund.getInt("postEnabled");
        }
        if(enabled == 1){
        	start();
        }else if(enabled == -1){
        	showConfirmPostOnFacebookDialog();
        }
    }
    
    public void start(){
    	progressbar.setVisibility(View.VISIBLE);
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
    
    private void showConfirmPostOnFacebookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);       
        builder.setTitle(R.string.postOnFacebook_dialog_title);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        ConfirmPostOnFacebookListener auxListener = new ConfirmPostOnFacebookListener();
        builder.setPositiveButton(R.string.dialogfb_yes, auxListener);
        builder.setNegativeButton(R.string.dialogfb_no,  auxListener);
        builder.setMessage(R.string.postOnFacebook_dialog_msg);
        builder.show();
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
				FacebookAutoPost.this.start();
				return;
			}
			case AlertDialog.BUTTON2: {
				StoreProperties.getInstance().setProperty("facebook", "0");
				FacebookAutoPost.this.finish();
				return;
			}
			}
		}
    }


}

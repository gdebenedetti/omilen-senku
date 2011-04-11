package com.omilen.games.senku;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.Util;
import com.facebook.stream.Session;
import com.omilen.social.SessionEvents.AuthListener;
import com.omilen.social.SessionEvents.LogoutListener;
import com.omilen.social.LoginButton;
import com.omilen.social.SessionEvents;


public class FacebookAutoPost extends Activity {

    // Your Facebook Application ID must be set before running this example
    // See http://www.facebook.com/developers/createapp.php
    public static final String APP_ID = "175729095772478";

    private LoginButton mLoginButton;
    private TextView mText;
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

        setContentView(R.layout.facebook);
        mLoginButton = (LoginButton) findViewById(R.id.login);
        mText = (TextView) FacebookAutoPost.this.findViewById(R.id.txt);
       
       	mFacebook = new Facebook(APP_ID);
       	mAsyncRunner = new AsyncFacebookRunner(mFacebook);

        //Session.restore(mFacebook, this);
       	Session.restore(this);       	
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());
        String[] permissions= new String[1];
        permissions[0] = "publish_stream";        
        mLoginButton.init(this, mAsyncRunner, mFacebook,permissions);

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

}

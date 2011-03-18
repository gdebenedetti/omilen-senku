package com.omilen.social;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.omilen.social.SessionEvents.AuthListener;
import com.omilen.social.SessionEvents.LogoutListener;
import com.facebook.stream.Dispatcher;
import com.facebook.stream.Session;
import com.facebook.stream.StreamHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class Ejemplo extends Activity {

    // Your Facebook Application ID must be set before running this example
    // See http://www.facebook.com/developers/createapp.php
    public static final String APP_ID = "175729095772478";

    private LoginButton mLoginButton;
    private TextView mText;
    private Button mRequestButton;
    private Button mPostButton;
    private Button mDeleteButton;
    private Button mUploadButton;
    private Button mMagicPost;
    private Dispatcher dispatcher;  

    private Facebook mFacebook;
    private AsyncFacebookRunner mAsyncRunner;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (APP_ID == null) {
            Util.showAlert(this, "Warning", "Facebook Applicaton ID must be " +
                    "specified before running this example: see Example.java");
        }

        setContentView(R.layout.main);
        mLoginButton = (LoginButton) findViewById(R.id.login);
        mText = (TextView) Ejemplo.this.findViewById(R.id.txt);
        mRequestButton = (Button) findViewById(R.id.requestButton);
        mPostButton = (Button) findViewById(R.id.postButton);
        mDeleteButton = (Button) findViewById(R.id.deletePostButton);
        mUploadButton = (Button) findViewById(R.id.uploadButton);
        mMagicPost =  (Button) findViewById(R.id.magicPostButton);

       	mFacebook = new Facebook(APP_ID);
       	mAsyncRunner = new AsyncFacebookRunner(mFacebook);

        //Session.restore(mFacebook, this);
       	Session.restore(this);       	
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());
        mLoginButton.init(this, mFacebook);

        mRequestButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	mAsyncRunner.request("me", new SampleRequestListener());
            }
        });
        mRequestButton.setVisibility(mFacebook.isSessionValid() ?
                View.VISIBLE :
                View.INVISIBLE);

        mUploadButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("method", "photos.upload");

                URL uploadFileUrl = null;
                try {
                    uploadFileUrl = new URL(
                        "http://www.facebook.com/images/devsite/iphone_connect_btn.jpg");
                } catch (MalformedURLException e) {
                	e.printStackTrace();
                }
                try {
                    HttpURLConnection conn= (HttpURLConnection)uploadFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    int length = conn.getContentLength();

                    byte[] imgData =new byte[length];
                    InputStream is = conn.getInputStream();
                    is.read(imgData);
                    params.putByteArray("picture", imgData);

                } catch  (IOException e) {
                    e.printStackTrace();
                }

                mAsyncRunner.request(null, params, "POST",
                        new SampleUploadListener(), null);
            }
        });
        mUploadButton.setVisibility(mFacebook.isSessionValid() ?
                View.VISIBLE :
                View.INVISIBLE);

        mPostButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mFacebook.dialog(Ejemplo.this, "feed",
                        new SampleDialogListener());
            }
        });
        mPostButton.setVisibility(mFacebook.isSessionValid() ?
                View.VISIBLE :
                View.INVISIBLE);

        mMagicPost.setVisibility(mFacebook.isSessionValid() ?
                View.VISIBLE :
                View.INVISIBLE);

        mMagicPost.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	//Comportamiento del boton magic Post
            	dispatcher = new Dispatcher(Ejemplo.this);
                dispatcher.addHandler("stream", StreamHandler.class);
                dispatcher.runHandler("stream");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        mFacebook.authorizeCallback(requestCode, resultCode, data);
    }

    public class SampleAuthListener implements AuthListener {

        public void onAuthSucceed() {
            mText.setText("You have logged in! ");
            mRequestButton.setVisibility(View.VISIBLE);
            mUploadButton.setVisibility(View.VISIBLE);
            mPostButton.setVisibility(View.VISIBLE);
            mMagicPost.setVisibility(View.VISIBLE);
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
            mRequestButton.setVisibility(View.INVISIBLE);
            mUploadButton.setVisibility(View.INVISIBLE);
            mPostButton.setVisibility(View.INVISIBLE);
            mMagicPost.setVisibility(View.INVISIBLE);
        }
    }

    public class SampleRequestListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
            try {
                // process the response here: executed in background thread
                Log.d("Facebook-Example", "Response: " + response.toString());
                JSONObject json = Util.parseJson(response);
                final String name = json.getString("name");

                // then post the processed result back to the UI thread
                // if we do not do this, an runtime exception will be generated
                // e.g. "CalledFromWrongThreadException: Only the original
                // thread that created a view hierarchy can touch its views."
                Ejemplo.this.runOnUiThread(new Runnable() {
                    public void run() {
                        mText.setText("Hello there, " + name + "!");
                    }
                });
            } catch (JSONException e) {
                Log.w("Facebook-Example", "JSON Error in response");
            } catch (FacebookError e) {
                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
            }
        }
    }

    public class SampleUploadListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
            try {
                // process the response here: (executed in background thread)
                Log.d("Facebook-Example", "Response: " + response.toString());
                JSONObject json = Util.parseJson(response);
                final String src = json.getString("src");

                // then post the processed result back to the UI thread
                // if we do not do this, an runtime exception will be generated
                // e.g. "CalledFromWrongThreadException: Only the original
                // thread that created a view hierarchy can touch its views."
                Ejemplo.this.runOnUiThread(new Runnable() {
                    public void run() {
                        mText.setText("Hello there, photo has been uploaded at \n" + src);
                    }
                });
            } catch (JSONException e) {
                Log.w("Facebook-Example", "JSON Error in response");
            } catch (FacebookError e) {
                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
            }
        }
    }
    public class WallPostRequestListener extends BaseRequestListener {
    	
       public void onComplete(final String response, final Object state) {
            Log.d("Facebook-Example", "Got response: " + response);
            String message = "<empty>";
            try {
                JSONObject json = Util.parseJson(response);
                message = json.getString("message");
            } catch (JSONException e) {
                Log.w("Facebook-Example", "JSON Error in response");
            } catch (FacebookError e) {
                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
            }
            final String text = "Your Wall Post: " + message;
            Ejemplo.this.runOnUiThread(new Runnable() {
                public void run() {
                    mText.setText(text);
                }
            });
        }
    }

    public class WallPostDeleteListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
            if (response.equals("true")) {
                Log.d("Facebook-Example", "Successfully deleted wall post");
                Ejemplo.this.runOnUiThread(new Runnable() {
                    public void run() {
                        mDeleteButton.setVisibility(View.INVISIBLE);
                        mText.setText("Deleted Wall Post");
                    }
                });
            } else {
                Log.d("Facebook-Example", "Could not delete wall post");
            }
        }
    }

    public class SampleDialogListener extends BaseDialogListener {

        public void onComplete(Bundle values) {
            final String postId = values.getString("post_id");
            if (postId != null) {
                Log.d("Facebook-Example", "Dialog Success! post_id=" + postId);
                mAsyncRunner.request(postId, new WallPostRequestListener());
                mDeleteButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        mAsyncRunner.request(postId, new Bundle(), "DELETE",
                                new WallPostDeleteListener(), null);
                    }
                });
                mDeleteButton.setVisibility(View.VISIBLE);
            } else {
                Log.d("Facebook-Example", "No wall post made");
            }
        }
    }

}

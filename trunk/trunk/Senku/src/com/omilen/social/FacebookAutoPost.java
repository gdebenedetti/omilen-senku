package com.omilen.social;

import com.omilen.games.senku.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.Util;
import com.facebook.stream.Session;
import com.omilen.social.SessionEvents.AuthListener;
import com.omilen.social.SessionEvents.LogoutListener;


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
        mLoginButton.init(this, mFacebook,permissions);

//        mRequestButton.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//            	//mAsyncRunner.request("me", new SampleRequestListener());
//                Bundle params = new Bundle();
//                params.putString("message", "Me gusta el Senku!");
//                params.putString("link","http://www.androidpit.com/en/android/market/apps/app/com.omilen.games.senku/Senkul");
//                params.putString("picture","http://fs01.androidpit.info/ass/x03/183503.jpg");
//                params.putString("name","Omilen Android Senku");
//                params.putString("description","Senku is simple addictive game for Android Devices, powered by Omilen IT Solutions");
//                params.putString("caption","Senku");
//                mAsyncRunner.request("me/feed",params,"POST",new SampleRequestListener(),null);
//            }
//        });
//        mRequestButton.setVisibility(mFacebook.isSessionValid() ?
//                View.VISIBLE :
//                View.INVISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        mFacebook.authorizeCallback(requestCode, resultCode, data);
    }

    public class SampleAuthListener implements AuthListener {

        public void onAuthSucceed() {
            mText.setText("You have logged in! ");            
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

//    public class SampleRequestListener extends BaseRequestListener {
//
//        public void onComplete(final String response, final Object state) {
//            try {
//                // process the response here: executed in background thread
//                Log.d("Facebook-Example", "Response: " + response.toString());
//                JSONObject json = Util.parseJson(response);
//                final String name = json.getString("name");
//
//                // then post the processed result back to the UI thread
//                // if we do not do this, an runtime exception will be generated
//                // e.g. "CalledFromWrongThreadException: Only the original
//                // thread that created a view hierarchy can touch its views."
//                FacebookAutoPost.this.runOnUiThread(new Runnable() {
//                    public void run() {
//                        mText.setText("Hello there, " + name + "!");
//                    }
//                });
//                /********************Make the POST*****************************/
////                Bundle params = new Bundle();
////                params.putString("message", "Me gusta el Senku!");
////                mAsyncRunner.request("stream.publish", params,new SampleRequestListenerFeed(),null);
//                
//                //String html;
//                //                    html = renderStatus(json,  "A "+name+" le gusta el Senku!");
//				//                    html = html.replace("'", "\\\'");
//				//callJs("onStatusUpdated('" + "A "+name+" le gusta el Senku!" + "');");
////            	String js = "onStatusUpdated('" + "A "+name+" le gusta el Senku!" + "');";
////            	streamHandler.getWebView().loadUrl("javascript:" + js);
//                
//                
//                /*************************************************************/
//            } catch (JSONException e) {
//                Log.w("Facebook-Example", "JSON Error in response");
//            } catch (FacebookError e) {
//                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
//            }
//        }
//    }
    
//    public class SampleRequestListenerFeed extends BaseRequestListener {
//
//        public void onComplete(final String response, final Object state) {
//            FacebookAutoPost.this.runOnUiThread(new Runnable() {
//			    public void run() {
//			        mText.setText("Listo el POST");
//			    }
//			});
//        }
//    }

//    public class SampleUploadListener extends BaseRequestListener {
//
//        public void onComplete(final String response, final Object state) {
//            try {
//                // process the response here: (executed in background thread)
//                Log.d("Facebook-Example", "Response: " + response.toString());
//                JSONObject json = Util.parseJson(response);
//                final String src = json.getString("src");
//
//                // then post the processed result back to the UI thread
//                // if we do not do this, an runtime exception will be generated
//                // e.g. "CalledFromWrongThreadException: Only the original
//                // thread that created a view hierarchy can touch its views."
//                FacebookAutoPost.this.runOnUiThread(new Runnable() {
//                    public void run() {
//                        mText.setText("Hello there, photo has been uploaded at \n" + src);
//                    }
//                });
//            } catch (JSONException e) {
//                Log.w("Facebook-Example", "JSON Error in response");
//            } catch (FacebookError e) {
//                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
//            }
//        }
//    }
//    public class WallPostRequestListener extends BaseRequestListener {
//    	
//       public void onComplete(final String response, final Object state) {
//            Log.d("Facebook-Example", "Got response: " + response);
//            String message = "<empty>";
//            try {
//                JSONObject json = Util.parseJson(response);
//                message = json.getString("message");
//            } catch (JSONException e) {
//                Log.w("Facebook-Example", "JSON Error in response");
//            } catch (FacebookError e) {
//                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
//            }
//            final String text = "Your Wall Post: " + message;
//            FacebookAutoPost.this.runOnUiThread(new Runnable() {
//                public void run() {
//                    mText.setText(text);
//                }
//            });
//        }
//    }

//    public class WallPostDeleteListener extends BaseRequestListener {
//
//        public void onComplete(final String response, final Object state) {
//            if (response.equals("true")) {
//                Log.d("Facebook-Example", "Successfully deleted wall post");
//                FacebookAutoPost.this.runOnUiThread(new Runnable() {
//                    public void run() {
//                        mDeleteButton.setVisibility(View.INVISIBLE);
//                        mText.setText("Deleted Wall Post");
//                    }
//                });
//            } else {
//                Log.d("Facebook-Example", "Could not delete wall post");
//            }
//        }
//    }

//    public class SampleDialogListener extends BaseDialogListener {
//
//        public void onComplete(Bundle values) {
//            final String postId = values.getString("post_id");
//            if (postId != null) {
//                Log.d("Facebook-Example", "Dialog Success! post_id=" + postId);
//                mAsyncRunner.request(postId, new WallPostRequestListener());
//                mDeleteButton.setOnClickListener(new OnClickListener() {
//                    public void onClick(View v) {
//                        mAsyncRunner.request(postId, new Bundle(), "DELETE",
//                                new WallPostDeleteListener(), null);
//                    }
//                });
//                mDeleteButton.setVisibility(View.VISIBLE);
//            } else {
//                Log.d("Facebook-Example", "No wall post made");
//            }
//        }
//    }

}

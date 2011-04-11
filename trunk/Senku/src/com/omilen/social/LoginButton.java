/*
 * Copyright 2010 Facebook, Inc.
 * Copyright 2011 Omilen IT Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.omilen.social;

import org.json.JSONException;
import org.json.JSONObject;

import com.omilen.games.senku.R;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.stream.Session;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


import com.omilen.social.SessionEvents.AuthListener;
import com.omilen.social.SessionEvents.LogoutListener;

public class LoginButton extends ImageButton {
    
    private Facebook mFb;
    private Handler mHandler;
    private SessionListener mSessionListener = new SessionListener();
    private String[] mPermissions;
    protected static Activity mActivity;
    protected static AsyncFacebookRunner mAsyncRunner;
    
    public LoginButton(Context context) {
        super(context);
    }
    
    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void init(final Activity activity,  final AsyncFacebookRunner asyncRunner, final Facebook fb) {
    	init(activity,asyncRunner, fb, new String[] {});
    }
    
    public void init(final Activity activity,  final AsyncFacebookRunner asyncRunner, final Facebook fb,
                     final String[] permissions) {
        mActivity = activity;
        mAsyncRunner = asyncRunner;
        mFb = fb;
        mPermissions = permissions;
        mHandler = new Handler();
        
        setBackgroundColor(Color.TRANSPARENT);
        setAdjustViewBounds(true);
        setImageResource(fb.isSessionValid() ?
                         R.drawable.logout_button : 
                         R.drawable.login_button);
        drawableStateChanged();
        
        SessionEvents.addAuthListener(mSessionListener);
        SessionEvents.addLogoutListener(mSessionListener);
        OnClickListener oncl = new ButtonOnClickListener();
        setOnClickListener(oncl);
        //JMR modification
        oncl.onClick(null);
        //END:JMR modification
    }
    
    private final class ButtonOnClickListener implements OnClickListener {
        
        public void onClick(View arg0) {
            if (mFb.isSessionValid()) {
                SessionEvents.onLogoutBegin();
                AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
                asyncRunner.logout(getContext(), new LogoutRequestListener());
            } else {
                mFb.authorize(mActivity, mPermissions,new LoginDialogListener());
            }
        }
    }

    private final class LoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
            SessionEvents.onLoginSuccess();
            //JMR: modification
            LoginButton.mAsyncRunner.request("me", new SampleRequestListener());
            Resources res = getResources();
            String name = res.getString(R.string.facebook_post_name);
            String description = res.getString(R.string.facebook_post_description);
            String caption = res.getString(R.string.facebook_post_caption);
                        
            Bundle params = new Bundle();            
            params.putString("message", "Me gusta el Senku!");
            params.putString("link","http://www.androidpit.com/en/android/market/apps/app/com.omilen.games.senku/Senkul");
            params.putString("picture","https://lh4.googleusercontent.com/_JP1lG3wnOcQ/TaKCGznWZ8I/AAAAAAAAABc/yZu12vodw_I/s800/icon2_for_facebook_senku.png");
            params.putString("name",name);
            params.putString("description",description);
            params.putString("caption",caption);
            LoginButton.mAsyncRunner.request("me/feed",params,"POST",new SampleRequestListener(),null);
            //END JMR modificaton
        }

        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
        }
        
        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
        }
    }
    
    private class LogoutRequestListener extends BaseRequestListener {
        public void onComplete(String response, final Object state) {
            // callback should be run in the original thread, 
            // not the background thread
            mHandler.post(new Runnable() {
                public void run() {
                    SessionEvents.onLogoutFinish();
                }
            });
        }
    }
    
    private class SessionListener implements AuthListener, LogoutListener {
        
        public void onAuthSucceed() {
            setImageResource(R.drawable.logout_button);
            Session.save(mFb, getContext());
           
        }

        public void onAuthFail(String error) {
        }
        
        public void onLogoutBegin() {           
        }
        
        public void onLogoutFinish() {
            Session.clear(getContext());
            setImageResource(R.drawable.login_button);
        }
    }
    
  public class SampleRequestListener extends BaseRequestListener {
    
            public void onComplete(final String response, final Object state) {
                	 LoginButton.mActivity.finish();
              }
        }
    
}

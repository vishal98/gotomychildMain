package com.mychild.view.CommonToApp;

import android.app.Activity;
import android.os.Bundle;

import com.mychild.threads.HttpConnectThread;
import com.mychild.utils.CustomHandler;
import com.pushbots.push.Pushbots;


/**
 * Created by Sandeep on 17-03-2015.
 */
public class BaseActivity extends Activity {
    public HttpConnectThread httpConnectThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        Pushbots.sharedInstance().init(BaseActivity.this);
        Pushbots.sharedInstance().setCustomHandler(CustomHandler.class);
    }
}

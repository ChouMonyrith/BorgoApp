package com.example.borgo;

import android.app.Application;
import com.example.borgo.manager.TokenManager;

public class BorgoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TokenManager.init(this);
    }
}
package com.example.borgo.utils;

import android.content.Context;
import android.util.Log;
import com.example.borgo.manager.TokenManager;

public class TokenUtils {
    private static final String TAG = "TokenUtils";
    
    public static void checkTokenStatus(Context context) {
        // Initialize TokenManager if not already done
        TokenManager.init(context);
        
        // Check if user ID exists
        int userId = TokenManager.getUserId();
        
        Log.d(TAG, "=== User Status Check ===");
        Log.d(TAG, "UserId: " + userId);
        Log.d(TAG, "========================");
    }
}
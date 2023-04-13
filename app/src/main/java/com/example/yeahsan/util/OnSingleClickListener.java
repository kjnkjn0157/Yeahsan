package com.example.yeahsan.util;

import android.os.SystemClock;
import android.view.View;

public abstract class OnSingleClickListener implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 300;

    private long mLastClickTime;

    public abstract void onSingleClick(View v);

    @Override
    public void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;

        // 중복 클릭인 경우
        if (elapsedTime <= MIN_CLICK_INTERVAL) {
            return;
        }

        onSingleClick(v);
    }
}

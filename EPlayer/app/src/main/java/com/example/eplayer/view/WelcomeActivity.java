package com.example.eplayer.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.example.eplayer.R;

/**
 * 欢迎页面Activity
 * @Author zhangyan
 */
public class WelcomeActivity extends Activity {

    private TextView skipStart;
    private MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (ActivityCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }

        skipStart = findViewById(R.id.textView);

        // 有广告
        if (true) {
            skipStart.setText(getResources().getString(R.string.skipFrontFourSecond));
            myCountDownTimer = new MyCountDownTimer(4000, 1000);
            myCountDownTimer.start();
        // 无广告
        } else {
            skipStart.setText(getResources().getString(R.string.skipFrontOneSecond));
            myCountDownTimer = new MyCountDownTimer(1000, 1000);
            myCountDownTimer.start();
        }

        skipStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以「 毫秒 」为单位倒计时的总数
         *                          例如 millisInFuture = 1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick()
         *                          例如: countDownInterval = 1000 ; 表示每 1000 毫秒调用一次 onTick()
         */

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            skipStart.setText(getResources().getString(R.string.skipFrontZeroSecond));
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            skipStart.setText(millisUntilFinished / 1000 + getResources().getString(R.string.skipFront));
        }
    }

    @Override
    protected void onDestroy() {
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
        super.onDestroy();

    }
}

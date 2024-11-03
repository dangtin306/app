package com.hm.hustmedia;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;
public class SplashActivity extends Activity {
    private static boolean splashLoaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!splashLoaded) {
            setContentView(R.layout.splash);
            VideoView videoView = (VideoView) findViewById(R.id.videoView);
//            String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";

//            videoView.setVideoPath("ok.mp4");
            String videopath = "android.resource://" + getPackageName() + "/" + R.raw.ok ;
            Uri uri = Uri.parse(videopath);
            videoView.setVideoURI(uri);
            videoView.start();
            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();

                }
            }, secondsDelayed * 7000);

            splashLoaded = true;
        }
        else {


            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("fromSplashActivity", true);
            startActivity(intent);
            finish();

        }
    }
}

package com.hm.hustmedia;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
public class splash extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkinternet);
        VideoView videoView = (VideoView) findViewById(R.id.videoView);
//            String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";

//            videoView.setVideoPath("ok.mp4");
        String videopath = "android.resource://" + getPackageName() + "/" + R.raw.internet;
        Uri uri = Uri.parse(videopath);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnCompletionListener ( new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
        Intent intent = getIntent();
        String value = intent.getStringExtra("urlpass");
        final TextView helloTextView = (TextView) findViewById(R.id.textView2);
        helloTextView.setText(value);
        if (value == null) {
            value = "https://hust.media/home/?=app5";
        } else {

        }
        Button openhome = (Button) findViewById(R.id.buttonhome); //get the id for button
        String finalValue = value;
        openhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("urlpass", finalValue);
                startActivity(intent);
            }
        });
    }
}

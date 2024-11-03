package com.hm.hustmedia;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class deeplink extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        if (data != null) {
            String url = intent.getDataString();
            if (url.indexOf("hustmedia://openhust") > -1 || url.indexOf("https://hust.media/openhust") > -1) {
                // Tìm vị trí của "openhust?app=" trong chuỗi
                int startIndex = url.indexOf("openhust?app=");
                if (startIndex != -1) {
                    String result = url.substring(startIndex + "openhust?app=".length());
//                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    startMainActivityWithUrl(result);
                } else {
                    // Nếu không tìm thấy
                    startMainActivityWithUrl("https://hust.media");
                }
            } else {
                Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                startMainActivityWithUrl(url);
            }
        } else {

            startMainActivityWithUrl("https://hust.media");
        }
    }

    private void startMainActivityWithUrl(String url) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("urlpass", url);
        startActivity(intent);
    }
}
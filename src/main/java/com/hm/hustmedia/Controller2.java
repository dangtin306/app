package com.hm.hustmedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Controller2 extends Activity  {
    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        Intent intent2 = new Intent(Controller2.this, MainActivity.class).putExtra("urlpass", "https://hust.media/home/?=app2");
        startActivity(intent2);
        finish();
    }

}

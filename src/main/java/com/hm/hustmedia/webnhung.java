package com.hm.hustmedia;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class webnhung extends Activity {



    WebView mainWebView2;

    String value234 = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.webnhung);


        mainWebView2 = findViewById(R.id.activity_mainn_webview);
        WebSettings webSettings = mainWebView2.getSettings();
        mainWebView2.getSettings().setBuiltInZoomControls(true);
        mainWebView2.getSettings().setSupportZoom(true);
        mainWebView2.getSettings().setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);


        mainWebView2.setWebChromeClient(new MyWebChromeClient()
                                       {
                                           private String currentUrl;

                                           public void onProgressChanged(WebView view, int progress)
                                           {


                                               String webUrl = view.getUrl() ;


                                                   mainWebView2.setWebViewClient(new com.hm.hustmedia.MyWebViewClient2());

                                                   mainWebView2.setWebViewClient(new MyWebViewClient2());

                                               Button openBrowser = (Button) findViewById(R.id.button2); //get the id for button
                                               openBrowser.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                                                       startActivity(browserIntent);
                                                   }
                                               });

                                           }

                                       }
        );

        Intent intent = getIntent();
        String sdadasdasd = intent.getStringExtra("urlback");
        String value = intent.getStringExtra("urlpass");
        mainWebView2.loadUrl(value);



        if (value != null &&  value != "") {
            value234 = sdadasdasd ;
        } else {
            value234 = "https://google.com";
        }

        Button openhome = (Button) findViewById(R.id.button); //get the id for button
        openhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("urlpass", "https://hust.media/home/");
                startActivity(intent);
                finish();
            }
        });
        Button openback = (Button) findViewById(R.id.button4);
        openback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("urlpass", value234);
                startActivity(intent);
                finish();
            }
        });
    }

    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri[]> mUploadMessage;

    @Override
    public void onBackPressed() {
        if(mainWebView2.canGoBack()) {
            mainWebView2.goBack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == FILECHOOSER_RESULTCODE) {

            if (null == mUploadMessage || intent == null || resultCode != RESULT_OK) {
                return;
            }

            Uri[] result = null;
            String dataString = intent.getDataString();

            if (dataString != null) {
                result = new Uri[]{ Uri.parse(dataString) };
            }

            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }


    // ====================
    // Web clients classes
    // ====================

    /**
     * Clase para configurar el webview
     */


    /**
     * Clase para configurar el chrome client para que nos permita seleccionar archivos
     */
    private class MyWebChromeClient extends WebChromeClient {

        // maneja la accion de seleccionar archivos
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

            // asegurar que no existan callbacks
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }

            mUploadMessage = filePathCallback;

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*"); // set MIME type to filter

            webnhung.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), webnhung.FILECHOOSER_RESULTCODE );

            return true;
        }
    }

}


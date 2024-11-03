package com.hm.hustmedia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
class MyWebViewClient2 extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(url.indexOf("id.zalo") > -1 ) return false;
        else if(url.indexOf("hust.media") > -1 ) {
            Context context = view.getContext();
            Intent intent = new Intent(context, MainActivity.class).putExtra("urlpass", url);
            context.startActivity(intent);
            return false;
        }
        else if(url.indexOf("intent") > -1 ) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
//        else if(url.indexOf("jr.zalo") > -1 ) return false;
        else  if(url.indexOf("adclick.g.doubleclick.net") > -1 ) return false;
        else  if(url.indexOf("facebook.com") > -1 ) return false;
        else  if(url.indexOf("?gclid=") > -1 ) return false;
        else  if(url.indexOf("adroll") > -1 ) return false;
        else  if(url.indexOf("a-ads.com") > -1 ) return false;
        String hostname;
        // YOUR HOSTNAME
        hostname = "hust.media";

        Uri uri = Uri.parse(url);
        if (url.startsWith("file:") || uri.getHost() != null && uri.getHost().endsWith(hostname)) {
            return false;
        }

        if(url.indexOf("accounts.google") > -1 ) return false;
        else if(url.indexOf("id.zalo.me") > -1 ) return false;
        else if(url.indexOf("zingmp3") > -1 ) return false;
        else if(url.indexOf("googleadservices") > -1 ) return false;
        else if(url.indexOf("baomoi.com") > -1 ) return false;
        else if(url.indexOf("adroll.com") > -1 ) return false;
        else if(url.indexOf("zalo://login/") > -1 ) {
            Context context = view.getContext();
            Intent intent2 = new Intent(context, MainActivity.class).putExtra("urlpass", url);
            context.startActivity(intent2);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
        else
        {
            return false;
        }


    }


}

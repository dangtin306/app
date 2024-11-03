package com.hm.hustmedia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.browser.customtabs.CustomTabsIntent;
class MyWebViewClient extends WebViewClient {


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        String hostname;
        // YOUR HOSTNAME
        hostname = "hust.media";

        Uri uri = Uri.parse(url);
        if (url.startsWith("file:") || uri.getHost() != null && uri.getHost().endsWith(hostname)) {
            return false;
        }  else if (url.indexOf("hustmedia://opendeephust") > -1
        ) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hust.media"));
            view.getContext().startActivity(intent);
            return true;
        } else if (url.indexOf("accounts.google") > -1
        ) {
            Context context = view.getContext();
            Intent intent = new Intent(context, webnhung.class).putExtra("urlpass", url);
            context.startActivity(intent);
            return false;
        }
//        else if(url.indexOf("jr.zalo") > -1 ) return false;
        else if (url.indexOf("adclick.g.doubleclick.net") > -1)
        {
            Context context = view.getContext();
            Intent intent = new Intent(context, webnhung.class).putExtra("urlpass", url);
            context.startActivity(intent);
            return false;
        }
        else if (url.indexOf("?gclid=") > -1)
        {
            Context context = view.getContext();
            Intent intent = new Intent(context, webnhung.class).putExtra("urlpass", url);
            context.startActivity(intent);
            return false;
        }
        else if (url.indexOf("adroll") > -1)
        {
            Context context = view.getContext();
            Intent intent = new Intent(context, webnhung.class).putExtra("urlpass", url);
            context.startActivity(intent);
            return false;
        }
        else if (url.indexOf("id.zalo.me") > -1) {
//            startActivity(new Intent(MainActivity.this, webnhung.class).putExtra("urlpass", webUrl));
            Context context = view.getContext();
            Intent intent = new Intent(context, webnhung.class).putExtra("urlpass", url);
            context.startActivity(intent);//error in this line
//            view.getContext().startActivity(intent);
             return false;
        } else if (url.indexOf("id.zalo") > -1) return false;
        else if (url.indexOf("zingmp3") > -1) return false;
        else if (url.indexOf("googleadservices") > -1)
        {
            Context context = view.getContext();
            Intent intent = new Intent(context, webnhung.class).putExtra("urlpass", url);
            context.startActivity(intent);
            return false;
        }
        else if (url.indexOf("tecom.pro") > -1) return false;
        else if (url.indexOf("tecom.media") > -1) return false;
        else if (url.indexOf("a-ads.com") > -1) {

// Assuming this code is within an activity or a fragment
            Context context = view.getContext();
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(url));
            return true;
        }
        else if (url.indexOf("adroll.com") > -1) {
            Context context = view.getContext();
            Intent intent = new Intent(context, webnhung.class).putExtra("urlpass", url);
            context.startActivity(intent);
            return false;
        } else if (url.indexOf("hust.media") > -1) {
            return false;

        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }


    }


}

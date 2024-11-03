package com.hm.hustmedia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends Activity {
    String adasdsadsa32342 = null;
    private VideoView videoView;

    SwipeRefreshLayout mySwipeRefreshLayout;
    private WebView mainWebView;
    private RelativeLayout mainLayout;
    private AdView mAdView;

    @SuppressLint({"MissingInflatedId", "HardwareIds"})
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @SuppressLint({"MissingInflatedId", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (!DetectConnection.checkInternetConnection(this)) {
            Intent intent = new Intent(this, splash.class);
            startActivity(intent);
        } else {
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            super.onCreate(savedInstanceState);

            setContentView(R.layout.splash);
            setContentView(R.layout.activity_main);

            mainLayout = findViewById(R.id.main_layout);
            mainLayout.setVisibility(View.GONE);
            mainWebView = findViewById(R.id.activity_main_webview);
            videoView = findViewById(R.id.videoView);
            WebSettings webSettings = mainWebView.getSettings();
            mainWebView.getSettings().setBuiltInZoomControls(true);
            mainWebView.getSettings().setSupportZoom(true);
            mainWebView.getSettings().setDisplayZoomControls(false);
            webSettings.setDomStorageEnabled(true);
            webSettings.setJavaScriptEnabled(true);
            CookieManager.getInstance().setAcceptThirdPartyCookies(mainWebView, true);
            mainWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
            mainWebView.getSettings().setLoadWithOverviewMode(true);
            mainWebView.getSettings().setAllowFileAccessFromFileURLs(true);
            mainWebView.getSettings().setUseWideViewPort(true);
            String currentUserAgent = mainWebView.getSettings().getUserAgentString();
            String newAgent = currentUserAgent + " hustmedia-android";
            mainWebView.getSettings().setUserAgentString(newAgent);
            mainWebView.addJavascriptInterface(new WebAppInterface(), "NativeAndroid");

            mainWebView.setWebChromeClient(new MyWebChromeClient() {

                private String currentUrl;

                public void onProgressChanged(WebView view, int progress) {

                    String webUrl = view.getUrl();

                    if (webUrl.indexOf("id.zalo") > -1 && webUrl.indexOf("hust.media") == -1) {

                        mainWebView.setWebViewClient(new MyWebViewClient());
                    } else {

                        mainWebView.setWebViewClient(new MyWebViewClient());

                    }
//                super.onProgressChanged(view, Integer.parseInt(webUrl));
//                mySwipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onPermissionRequest(final PermissionRequest request) {
                    request.grant(request.getResources());
                }

                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    //Tạo yêu cầu tải về
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                    // Cài đặt các thông số yêu cầu tải về
                    request.setMimeType(mimetype);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading file...");
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                    // Đặt đường dẫn cho file tải về
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));

                    // Lấy DownloadManager từ hệ thống
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                    // Đưa yêu cầu tải về vào hàng đợi tải về của DownloadManager
                    dm.enqueue(request);

                    // Hiển thị thông báo cho người dùng biết rằng đang tải về
                    Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
                }
            });


            mainWebView.getSettings().setJavaScriptEnabled(true);
            // Register the web view.
            MobileAds.registerWebView(mainWebView);

            Intent intent = getIntent();
            // MainActivity được khởi động từ một Activity khác
            String value = intent.getStringExtra("urlpass");

            if (value == null) {
                value = "https://hust.media/home/?verhustapp=app17";
                adasdsadsa32342 = "https://hust.media/home/?verhustapp=app17";
            } else {
                adasdsadsa32342 = value;
            }

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            boolean isOpenApp = sharedPreferences.getBoolean("openapp", true);
            if (isOpenApp == false && value == "https://hust.media/home/?verhustapp=app17") {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("openapp", true);
                editor.apply();
                String device_id = "";
                try {
                    device_id = getDeviceId(getApplicationContext());
                    // Log ra ANDROID_ID
                    Log.d("device_id", "Device ID: " + device_id);
                    adasdsadsa32342 = "https://hust.media/home/?verhustapp=app17&device_id=" + device_id + "&device_type=android";
                    mainWebView.loadUrl(adasdsadsa32342);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Find the VideoView in the splash screen layout
                            // Load the video from the raw folder
                            String videopath = "android.resource://" + getPackageName() + "/" + R.raw.ok;
                            Uri uri = Uri.parse(videopath);
                            videoView.setVideoURI(uri);
                            // Start the video
                            videoView.start();
                            // Delay for 7 seconds before starting the MainActivity
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    videoView.setVisibility(View.GONE);
                                }
                            }, 2700);
                        }
                    }, 500);
                    // Tìm AdView theo id và tải quảng cáo
                    mAdView = findViewById(R.id.adView);
                    AdRequest adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                    hideAdView(); // Ẩn AdView nếu URL chứa hostname
                } catch (SecurityException e) {
                    adasdsadsa32342 = "https://hust.media/home/?verhustapp=app17&device_id=" + device_id + "&device_type=android";
                    mainWebView.loadUrl(adasdsadsa32342);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Find the VideoView in the splash screen layout
                            // Load the video from the raw folder
                            String videopath = "android.resource://" + getPackageName() + "/" + R.raw.ok;
                            Uri uri = Uri.parse(videopath);
                            videoView.setVideoURI(uri);
                            // Start the video
                            videoView.start();
                            // Delay for 7 seconds before starting the MainActivity
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    videoView.setVisibility(View.GONE);
                                }
                            }, 2700);
                        }
                    }, 500);
                    e.printStackTrace();
                }
            } else {
                videoView.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TAG", "Value of adasdsadsa32342: " + adasdsadsa32342);
                        mainWebView.loadUrl(adasdsadsa32342);
                    }
                }, 300);
            }
            mySwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipeContainer);

            mySwipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            mainWebView.reload();
                        }
                    }
            );


        }


//handle downloading


        mainWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, 1);

                    }
                }
//                 if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                    Toast.makeText(getApplicationContext(), "Please grant permission to download files", Toast.LENGTH_SHORT).show();
//                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        requestPermissions(permissions, 1);
//                    }
//
//                }
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimeType);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file....");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Hust Media Downloading File", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mainLayout.setVisibility(View.VISIBLE);
        }
    }

    public class MyWebViewClient extends WebViewClient {

//        public boolean isOnline() {
//            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo netInfo = cm.getActiveNetworkInfo();
//            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
//                return true;
//            } else {
//                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
//                startActivity(intent);
//                return false;
//            }
//
//        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!DetectConnection.checkInternetConnection(MainActivity.this)) {
                Context context = view.getContext();
                Intent intent = new Intent(context, splash.class).putExtra("urlpass", url);
                context.startActivity(intent);
                return false;
            }
            String currentUrl = view.getUrl();

            String hostname;
            // YOUR HOSTNAME
            hostname = "hust.media";
            Uri uri = Uri.parse(url);
//            Log.d("url load", "Device ID: " + url);
//                Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
            if (url.indexOf("hustmedia://openhust") > -1
            ) {
                return false;
            } else if (url.endsWith(".mp4")) {
                view.loadUrl(url);
                return true;
            } else if (url.indexOf("open_rewarded_ads") > -1) {
//                 Lấy giá trị của tham số redirect_hust
                String redirectHustValue = uri.getQueryParameter("redirect_hust");
//                Toast.makeText(getApplicationContext(), redirectHustValue, Toast.LENGTH_LONG).show();
                Context context = view.getContext();
                Intent intent = new Intent(context, RewardActivity.class).putExtra("urlback", redirectHustValue);
                context.startActivity(intent);
                return true;
            } else if (url.indexOf("auth/login_google") > -1) {
                if ( url.contains("uiviewcontroller" ))
                {
                    Context context = view.getContext();
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, Uri.parse(url));
                }
                else
                {
                    // Assuming this code is within an activity or a fragment
                    String device_id = getDeviceId(getApplicationContext());
                    Context context = view.getContext();
                    if (url.indexOf("&apikey=") > -1) {
                        String apikey = uri.getQueryParameter("apikey");
                        Intent intent = new Intent(context, logingoogle.class).putExtra("device_id", device_id).putExtra("apikey", apikey);
                        context.startActivity(intent);//error in this line
                    } else {
                        Intent intent = new Intent(context, logingoogle.class).putExtra("device_id", device_id);
                        context.startActivity(intent);//error in this line
                    }
                }
                return true;
            } else if (url.contains("uiviewcontroller")
                    || url.contains("cdn.bmcdn")
                    || url.contains("appsflyer")
                    || url.contains("adskeeper.com")
                    || url.contains("criteo.com")
                    || url.contains("adjust.com")
                    || url.contains("accounts.google")
                    || url.contains("g.doubleclick.net")
                    || url.contains("appier.net")
                    || url.contains("?gclid=")
                    || url.contains("adroll.com")
                    || url.contains("&gclid=")
            ) {
// Assuming this code is within an activity or a fragment
                Context context = view.getContext();
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(context, Uri.parse(url));
                return true;
            } else if (url.contains("showadview")) {
                showAdView(); // Ẩn AdView nếu URL chứa ho
                return false;
            } else if (url.contains("hideadview")) {
                hideAdView(); // Ẩn AdView nếu URL chứa hostname
                return false;
            } else if (url.startsWith("file:") || (uri.getHost() != null && uri.getHost().endsWith(hostname))) {
                hideAdView(); // Ẩn AdView nếu URL chứa hostname
                return false;
            } else if (
                     url.indexOf("anime/play") > -1
                    || url.indexOf("payeer.com") > -1
                    || url.indexOf("perfectmoney") > -1
                    || url.indexOf("paypal.com") > -1
            ) {
                Context context = view.getContext();
                Intent intent = new Intent(context, webnhung.class).putExtra("urlpass", url).putExtra("urlback", currentUrl);
                context.startActivity(intent);
                return true;
            } else if (url.indexOf("id.zalo.me") > -1) {
//            startActivity(new Intent(MainActivity.this, webnhung.class).putExtra("urlpass", webUrl));
                Context context = view.getContext();
                Intent intent = new Intent(context, webnhung.class).putExtra("urlpass", url).putExtra("urlback", currentUrl);
                context.startActivity(intent);//error in this line
//            view.getContext().startActivity(intent);
                return true;
            } else if (url.indexOf("id.zalo") > -1) return false;
            else if (url.indexOf("zingmp3") > -1) return false;
            else if (url.indexOf("googleadservices") > -1) {
                Context context = view.getContext();
                Intent intent = new Intent(context, webnhung.class).putExtra("urlpass", url).putExtra("urlback", currentUrl);
                context.startActivity(intent);
                return true;
            } else if (url.contains("a-ads.com")) {
                Context context = view.getContext();
                Intent intent = new Intent(context, webnhung.class).putExtra("urlpass", url).putExtra("urlback", currentUrl);
                context.startActivity(intent);
                return true;
            } else if (url.indexOf("tecom.pro") > -1) return false;
            else if (url.indexOf("tecom.media") > -1) return false;
            else if (url.contains("hust.media")) {
                hideAdView(); // Ẩn AdView nếu URL chứa hostname
                return false;
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                return true;
            }
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mySwipeRefreshLayout.setRefreshing(false);
        }
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void copyToClipboard(String text) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("demo", text);
            clipboard.setPrimaryClip(clip);
        }
    }


    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri[]> mUploadMessage;

    @Override
    public void onBackPressed() {
        if (mainWebView.canGoBack()) {
            mainWebView.goBack();
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
                result = new Uri[]{Uri.parse(dataString)};
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

    private abstract class MyWebChromeClient extends WebChromeClient {

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

            MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), MainActivity.FILECHOOSER_RESULTCODE);

            return true;
        }

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyWebChromeClient() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        public abstract void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength);

    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void hideAdView() {
        AdView adView = (AdView) findViewById(R.id.adView);
        adView.setVisibility(View.GONE);
    }
    private void showAdView() {
        AdView adView = (AdView) findViewById(R.id.adView);
        adView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mainWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mainWebView.restoreState(savedInstanceState);
    }
}


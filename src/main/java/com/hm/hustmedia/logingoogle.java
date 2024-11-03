package com.hm.hustmedia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class logingoogle extends Activity {

    private static final int RC_SIGN_IN = 9001;  // Request code for sign-in activity
    private GoogleSignInClient mGoogleSignInClient;
    private AdView mAdView;
    private TextView loggedInEmailTextView; // TextView để hiển thị email đã đăng nhập
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_EMAIL = "email";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logingoogle);
        // Khởi tạo SharedPreferences
        try {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String savedEmail = prefs.getString(PREF_EMAIL, null);
            // Sử dụng savedEmail ở đây
            // Hiển thị email đã lưu (nếu có)
            // Tìm và thiết lập sự kiện cho nút Đăng xuất
            Button logoutButton = findViewById(R.id.logout_button);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signOut();
                }
            });
            if (savedEmail != null) {
                // Hiển thị nút Đăng xuất
                logoutButton.setVisibility(View.VISIBLE);
                // Tìm TextView để hiển thị email đã đăng nhập
                loggedInEmailTextView = findViewById(R.id.logged_in_email);
                loggedInEmailTextView.setText(savedEmail);
            } else {
                // Ẩn nút Đăng xuất nếu không có email đã lưu
                logoutButton.setVisibility(View.GONE);
            }
            String clientId = getString(R.string.server_client_id);
            // Configure Google Sign-In options
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(clientId)
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            // Find and show the Google Sign-In button
            SignInButton signInButton = findViewById(R.id.sign_in_button);
            signInButton.setVisibility(SignInButton.VISIBLE);

            // Set click listener for sign-in button
            signInButton.setOnClickListener(v -> {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            });

            Button backButton = findViewById(R.id.backhome);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Khi nút "Quay lại" được nhấn, kết thúc Activity hiện tại để quay lại màn hình trước đó
                    finish();
                }
            });

            // Tìm AdView theo id và tải quảng cáo
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } catch (Exception e) {
            // Xử lý lỗi ở đây
            e.printStackTrace();
            Toast.makeText(logingoogle.this, "Đã xảy ra lỗi lưu dữ liệu vui lòng thử lại", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void signOut() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(PREF_EMAIL, null);
        editor.apply();
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setVisibility(View.GONE);
        loggedInEmailTextView = findViewById(R.id.logged_in_email);
        loggedInEmailTextView.setText(null);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(logingoogle.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...)
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String name = account.getDisplayName();
                String email = account.getEmail();
                String idToken = account.getIdToken();
                Log.w("SignInActivity vip", name + email + idToken);
                Toast.makeText(getApplicationContext(), "Chờ xíu nhé.", Toast.LENGTH_SHORT).show();
                // Update UI or perform actions with user information (name, email, idToken)
                updateUI(name, email);
                if (email != null) {
                    // Lưu email vào SharedPreferences
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
                    editor.putString(PREF_EMAIL, email);
                    editor.apply();
                }
                if (idToken != null) {
                    Intent intent = getIntent();
                    String apikey = intent.getStringExtra("apikey");
                    String device_id = intent.getStringExtra("device_id");
                    // Xác định kiểu dữ liệu JSON
                    if (apikey != null) {
                        // Tạo JSON body từ idToken
                        String json = "{\"credential\": \"" + idToken + "\", \"chedo\": \"update\",\"apikey\": \"" + apikey + "\" }";
                        Log.w("json login", json);
                        sendApiRequest(idToken, json, device_id);
                    } else {
                        // Tạo JSON body từ idToken
                        String json = "{\"credential\": \"" + idToken + "\", \"chedo\": \"android\"}";
                        sendApiRequest(idToken, json, device_id);
                    }
                }
            } else {
                // Sign in failed, update UI or handle error
                updateUI(null, null);
                Toast.makeText(getApplicationContext(), "Đăng nhập thất bại vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            // Handle sign-in exception
            Toast.makeText(getApplicationContext(), "Đăng nhập thất bại Mã lỗi: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            Log.w("SignInActivity", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null, null);
            finish();
        }
    }

    // Hàm này gửi yêu cầu API đến URL với JSON body chứa idToken
    private void sendApiRequest(String idToken, String json, String device_id) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(json, JSON);

        // Tạo yêu cầu HTTP POST đến URL
        Request request = new Request.Builder()
                .url("https://hust.media/api/auth/login_google.php")
                .post(requestBody)
                .build();

        // Khởi tạo OkHttpClient và gửi yêu cầu
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Xử lý phản hồi từ máy chủ
                if (response.isSuccessful()) {
                    // Phản hồi thành công
                    String responseData = response.body().string();
                    Log.w("phản hồi api", responseData);
                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String redirect = jsonResponse.optString("redirect");
                        String apikey = jsonResponse.optString("apikey");
                        String message = jsonResponse.optString("message");
                        String status = jsonResponse.optString("status");
                        Log.d("Redirect:", redirect);
                        Log.d("Apikey:", apikey);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                // Kiểm tra nếu status là 1
                                if (status.equals("1")) {
                                    Log.d("device_id:", device_id);
                                    String url_redirect = "https://hust.media/api/auth/dang-nhap.php?apikey=" + apikey + "&device_id=" + device_id + "&device_type=android";
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("urlpass", url_redirect);
                                    startActivity(intent);
                                    finish();
                                    // Thực hiện các hành động bổ sung ở đây
                                } else {
                                    if (message != null && status != null) {
                                        finish();
                                    }
                                }
                            }
                        });
                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Lỗi api vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Lỗi phản hồi api vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Xử lý lỗi khi gửi yêu cầu
                e.printStackTrace();
            }
        });
    }

    private void updateUI(String name, String email) {
        // Update your UI elements with user information
        if (email != null) {
//            TextView welcomeText = findViewById(R.id.welcome_text);
//            welcomeText.setText("Welcome, " + name + "!");
        } else {
            // Sign in failed, display error message
//            Toast.makeText(this, "Đăng nhập thất bại, Sign in failed!", Toast.LENGTH_SHORT).show();
//            finish();
        }
    }
}

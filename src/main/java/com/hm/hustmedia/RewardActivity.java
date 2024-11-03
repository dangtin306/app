package com.hm.hustmedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class RewardActivity extends Activity {

    private RewardedAd rewardedAd;
    private final String TAG = "RewardActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        // Load quảng cáo thưởng
        loadRewardedAd();
    }

    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-4574266110812955/4380865030",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Xử lý lỗi.
                        Log.d(TAG, "Lỗi tải ads" + loadAdError.toString());
                        Toast.makeText(RewardActivity.this, "Lỗi tải ads" + loadAdError.toString(), Toast.LENGTH_SHORT)
                                .show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("urlpass", "https://hust.media/home/");
                        startActivity(intent);
                        finish();
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Quảng cáo đã được tải.");

                        // Hiển thị quảng cáo thưởng khi nó đã được tải
                        if (rewardedAd != null) {
                            showRewardedAd();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("urlpass", "https://hust.media/home/");
                            startActivity(intent);
                            finish();
                            Toast.makeText(RewardActivity.this, "Phần quà chưa sẵn sàng thử lại sau", Toast.LENGTH_SHORT)
                                    .show();
                            Log.d(TAG, "The rewarded ad wasn't ready yet.");
                        }
                    }
                });
    }

    private void showRewardedAd() {
        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "onAdShowedFullScreenContent.");
                    }


                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Intent intent = getIntent();
                        String urlback = intent.getStringExtra("urlback");
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;
                        Log.d(TAG, "onAdDismissedFullScreenContent");
                        Toast.makeText(RewardActivity.this, "Thanks", Toast.LENGTH_SHORT)
                                .show();
                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class).putExtra("urlpass", urlback);
                        startActivity(intent2);
                        finish();
                    }
                });
        Activity activityContext = RewardActivity.this;
        rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                // Handle the reward.
                Log.d(TAG, "The user earned the reward.");
//                addCoins(coinCount);
            }
        });
    }

}

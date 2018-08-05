package com.coresmash;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/* Important information to avoid confusion:
 *      First of all this class is tied with the Android platform. With that in mind it is really
 *  important to understand that there will be _TWO_ threads that will be working with this class.
 *  It is the Android's Main Thread or else the 'UI Thread' and then there is the Games' Thread or the LibGdx Thread.
 *  All the Android UI stuff (including ALL the ad types) are in the Android's thread and all the triggers
 *  for the ads to be shown are in the Game's thread. The Game's thread _CANNOT_ directly alter the Android UI
 *  that was initialized and is managed by the Android thread even though it *does* have access to them through this
 *  class. In order for the two threads to communicate, they use a Handler that is initialized in the Android's thread
 *  which works similar to the Windows loop. The Game's thread has to send messages or runnables that get queued
 *  in the Android thread and get executed on that thread.
 */

public final class AdmobManager implements AdManager {
    private static final int ADSHOW = 1;
    private static final int ADHIDE = 0;

    private AdView adView;
    private ProgressDialog dialog;
    private RewardedVideoAd rewardedVideoAd;
    private Handler handler;

    public AdmobManager(String id) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ADSHOW:
                        adView.setVisibility(View.VISIBLE);
                        break;
                    case ADHIDE:
                        adView.setVisibility(View.GONE);
                        break;
                }
            }
        };
    }

    public void init(final AndroidApplication context, final RelativeLayout layout) {
        MobileAds.initialize(context, "ca-app-pub-3940256099942544~3347511713");
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);

        dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading...");
        dialog.setCanceledOnTouchOutside(false);

        adView = new AdView(context);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.GONE);
                adView.requestLayout();
                Toast.makeText(context, "AdLoaded!", Toast.LENGTH_SHORT).show();
            }
        });

        AdRequest.Builder requestBuilder = new AdRequest.Builder();
        adView.loadAd(requestBuilder.build());

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);// Shows Ads on Center Bottom
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layout.addView(adView, adParams);
    }

    @Override
    public void show() {
        handler.sendEmptyMessage(ADSHOW);
    }

    @Override
    public void showAdForReward(final AdRewardListener listener) {
        toggle();
        handler.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                if (rewardedVideoAd.isLoaded()) {
                    rewardedVideoAd.show();
                } else {
                    dialog.show();
                    rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
                    rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                        @Override
                        public void onRewardedVideoAdLoaded() {
                            if (dialog.isShowing()) {
                                dialog.hide();
                                rewardedVideoAd.show();
                            }
                        }

                        @Override
                        public void onRewardedVideoAdOpened() {

                        }

                        @Override
                        public void onRewardedVideoStarted() {

                        }

                        @Override
                        public void onRewardedVideoAdClosed() {

                        }

                        @Override
                        public void onRewarded(RewardItem item) {
                            listener.reward(item.getType(), item.getAmount());
                        }

                        @Override
                        public void onRewardedVideoAdLeftApplication() {

                        }

                        @Override
                        public void onRewardedVideoAdFailedToLoad(int i) {

                        }

                        @Override
                        public void onRewardedVideoCompleted() {
                        }
                    });
                }
            }
        });
    }

    @Override
    public void toggle() {
        if (adView.isShown()) {
            handler.sendEmptyMessage(ADHIDE);
        } else {
            handler.sendEmptyMessage(ADSHOW);
        }
    }

    @Override
    public void hide() {
        handler.sendEmptyMessage(ADHIDE);
    }

}
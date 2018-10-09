package com.jjm.android.quizanoi.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.jjm.android.quizanoi.util.MCrypt;

import com.jjm.android.quizanoi.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardItem;

public class KiemBitCoinActivity extends AppCompatActivity implements RewardedVideoAdListener {
    private RewardedVideoAd mRewardedVideoAd;
    boolean play_flag = false;
    Button btn_watch_video;
    ProgressBar loading;
    TextView tv_bitcoin;
    Typeface type;
    String key_AES = "anoistudio.duoihinhlaichu";
    int score = 100;
    int tryTime =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiem_bitcoin);
        MobileAds.initialize(this,"ca-app-pub-4345988626634460~1408254906"); //tdp
//        MobileAds.initialize(this," ca-app-pub-3940256099942544~3347511713"); //google test
        type = Typeface.createFromAsset(getAssets(),"fonts/Bellerose.ttf");
        int tryTime =0;
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        this.btn_watch_video = findViewById(R.id.btn_watch_video);
        this.tv_bitcoin =  findViewById(R.id.tv_bitcoin);
        this.tv_bitcoin.setTypeface(type);
        this.loading =findViewById(R.id.progressBar);
        mRewardedVideoAd.loadAd(
                "ca-app-pub-4345988626634460/7809402917", new AdRequest.Builder() //tdp
//                "ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder() //google test
                        //.addTestDevice("9038137B1975D034E005EEE96541909F")
                        //.addTestDevice("5EAF8DE05E8974A7D5C369BD0A00C22B")
                        .build());
        this.btn_watch_video.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //play_flag = true;
                Load();
            }
        });
        this.btn_watch_video.setVisibility(View.INVISIBLE) ;
        Load_BitCoinTuBoNho();
    }
    @Override
    public void onRewarded(RewardItem reward) {
        Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +
                reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.
        this.score += 100;
        this.Save_BitCoinXuongBoNho();
        this.Load_BitCoinTuBoNho();
        //this.Save_CauHoiXuongBoNho();
//        mRewardedVideoAd.loadAd(
//                "ca-app-pub-4345988626634460/7809402917", new AdRequest.Builder() //tdp
////                "ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder() //google test
//                        .addTestDevice("9038137B1975D034E005EEE96541909F")
//                        .addTestDevice("5EAF8DE05E8974A7D5C369BD0A00C22B")
//                        .build());
        this.btn_watch_video.setVisibility(View.INVISIBLE) ;
        this.loading.setVisibility(View.VISIBLE) ;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //play_flag =false;
        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        this.btn_watch_video.setVisibility(View.INVISIBLE) ;
        this.loading.setVisibility(View.VISIBLE) ;
        mRewardedVideoAd.loadAd(
                "ca-app-pub-4345988626634460/7809402917", new AdRequest.Builder() //tdp
//                "ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder() //google test
                        //.addTestDevice("9038137B1975D034E005EEE96541909F")
                        //.addTestDevice("5EAF8DE05E8974A7D5C369BD0A00C22B")
                        .build());
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad" + errorCode, Toast.LENGTH_SHORT).show();
        this.btn_watch_video.setVisibility(View.INVISIBLE) ;
        this.loading.setVisibility(View.VISIBLE) ;
        try { Thread.sleep(1000); }
        catch (InterruptedException ex) { android.util.Log.d("YourApplicationName", ex.toString()); }

        if(tryTime<2) {
            tryTime ++;
            mRewardedVideoAd.destroy(KiemBitCoinActivity.this.getApplicationContext());
            mRewardedVideoAd.loadAd(
                    "ca-app-pub-4345988626634460/7809402917", new AdRequest.Builder() //tdp
//                "ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder() //google test
                            .addTestDevice("9038137B1975D034E005EEE96541909F")
                            .addTestDevice("5EAF8DE05E8974A7D5C369BD0A00C22B")
                            .build());
        }
        else
        {
            tryTime = 0;
            AlertDialog.Builder alertDialogBuildertb1 = new AlertDialog.Builder(this, 1);

            alertDialogBuildertb1.setTitle("Thông Báo");
            alertDialogBuildertb1.setIcon(R.drawable.e_6);
            ///alertDialogBuildertb1.setIcon(R.drawable.ic_appgame);
            alertDialogBuildertb1.setMessage("Có Lỗi Trong Quá Trình Tải Quảng Cáo ").setPositiveButton("Thử Lại", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    KiemBitCoinActivity.this.recreate();

                }
            }).setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    mRewardedVideoAd.destroy(KiemBitCoinActivity.this.getApplicationContext());
                    KiemBitCoinActivity.this.startActivity(new Intent(KiemBitCoinActivity.this.getApplicationContext(), MainActivity.class));
                    KiemBitCoinActivity.this.finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuildertb1.create();
            if (!isFinishing()) {
                alertDialog.show();
            }
        }
    }
    public void onBackPressed() {
        mRewardedVideoAd.destroy(KiemBitCoinActivity.this.getApplicationContext());
        KiemBitCoinActivity.this.startActivity(new Intent(KiemBitCoinActivity.this.getApplicationContext(), MainActivity.class));
        KiemBitCoinActivity.this.finish();
    }
    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        this.btn_watch_video.setVisibility(View.VISIBLE) ;
        this.loading.setVisibility(View.INVISIBLE) ;
        //if(play_flag == true)
        //{
           // mRewardedVideoAd.show();
        //}
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }
    public void Load() {

        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
//        mRewardedVideoAd.loadAd(
//                "ca-app-pub-4345988626634460/7809402917", new AdRequest.Builder() //tdp
//                        // "ca-app-pub-4345988626634460/7809402917", new AdRequest.Builder()
//                        .addTestDevice("9038137B1975D034E005EEE96541909F")
//                        .addTestDevice("5EAF8DE05E8974A7D5C369BD0A00C22B")
//                        .build());
    }

//    static final String FB_NA_ID = "149767005655368_149767695655299";
//    private static final int REQUEST_WRITE_STORAGE = 112;
//    static final String UNITY_ID = "1624453";
//    public CoverFlowAdapter adapter;
//    private AdView adview_2pics1word_kiemruby;
//    private CallbackManager callbackManager;
//    ConnectionDetector cd;
//    Boolean cdapp = Boolean.valueOf(false);
//    CountDownTimer checkgio;
//    public FeatureCoverFlow coverFlow;
//    String game1_icon = "";
//    String game1_link = "market://details?id=ahihi.studiogamevn.haihinhmotchu";
//    String game2_icon = "";
//    String game2_link = "market://details?id=ahihi.studiogamevn.hoingusieuhainao";
//    String game3_icon = "";
//    String game3_link = "market://details?id=ahihi.studiogamevn.haihinhmotchu";
//    String game4_icon = "";
//    String game4_link = "market://details?id=ahihi.studiogamevn.hoingusieuhainao";
//    public ArrayList<Game> games;
//    int index = 0;
//    Boolean issharegame = Boolean.valueOf(false);
//    Boolean issound = Boolean.valueOf(true);
//    String key_AES = "anoistudio.duoihinhlaichu";
//    String linkapp = "";
//    String linklaytudata = "";
//    String macdinh = "market://details?id=ahihi.studiogamevn.hoingusieuhainao";
//    MyFunctions myfunctions;
//    RelativeLayout re_cdgame;
//    RelativeLayout re_gamehaykhac;
//    RelativeLayout re_sharegame;
//    RelativeLayout re_xemvideo;
//    int score = 100;
//    MediaPlayer soundclick;
//    int trangthai = 0;
//    TextView tv_ruby;
//    private final UnityAdsListener unityAdsListener = new UnityAdsListener();
//    int viewadsunity = 0;



//    private class UnityAdsListener implements IUnityAdsListener {
//        private UnityAdsListener() {
//        }
//
//        public void onUnityAdsError(UnityAdsError arg0, String arg1) {
//        }
//
//        public void onUnityAdsFinish(String arg0, FinishState arg1) {
//            SharedPreferences pref = KiemBitCoinActivity.this.getSharedPreferences("MYDATAREF", 0);
//            KiemBitCoinActivity.this.viewadsunity = pref.getInt("KEY_LINKADS", 0);
//            KiemBitCoinActivity.this.Load_BitCoinTuBoNho();
//            KiemBitCoinActivity kiemBitCoinActivity = KiemBitCoinActivity.this;
//            kiemBitCoinActivity.viewadsunity++;
//            KiemBitCoinActivity.this.score += 20;
//            Editor editor = KiemBitCoinActivity.this.getSharedPreferences("MYDATAREF", 0).edit();
//            editor.putInt("KEY_LINKADS", KiemBitCoinActivity.this.viewadsunity);
//            editor.commit();
//            KiemBitCoinActivity.this.Save_BitCoinXuongBoNho();
//            KiemBitCoinActivity.this.Update_BitCoin();
//            Toast.makeText(KiemBitCoinActivity.this, "Tặng bạn 20 BitCoin cho việc xem hết video nà!", 0).show();
//        }
//
//        public void onUnityAdsReady(String arg0) {
//        }
//
//        public void onUnityAdsStart(String arg0) {
//        }
//    }
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFlags(1024, 1024);
//        FacebookSdk.sdkInitialize(this);
//        this.callbackManager = Factory.create();
//        UnityAds.initialize(this, UNITY_ID, this.unityAdsListener);
//        setContentView((int) R.layout.activity_kiem_bitcoin);
//        getSupportActionBar().hide();
//        this.re_cdgame = (RelativeLayout) findViewById(R.id.RelativeLayout_cd_game);
//        this.re_sharegame = (RelativeLayout) findViewById(R.id.RelativeLayout_sharegame);
//        this.re_xemvideo = (RelativeLayout) findViewById(R.id.RelativeLayout_reward_unityads);
//        this.re_gamehaykhac = (RelativeLayout) findViewById(R.id.RelativeLayout_gamehaykhac);
//        this.tv_ruby = (TextView) findViewById(R.id.tv_ruby);
//        SharedPreferences pref = getSharedPreferences("MYDATAREF", 0);
//        this.cdapp = Boolean.valueOf(pref.getBoolean("KEY_CDAPP", false));
//        this.linklaytudata = pref.getString("KEY_LINKAPP", this.macdinh);
//        this.issound = Boolean.valueOf(pref.getBoolean("KEY_ISSOUND", true));
//        this.issharegame = Boolean.valueOf(pref.getBoolean("KEY_SHARE_APP", false));
////        this.cd = new ConnectionDetector(this);
//        Load_BitCoinTuBoNho();
//        Load_CauHoiTuBoNho();
//        this.tv_ruby.setText(this.score + "");
//        this.adview_2pics1word_kiemruby = (AdView) findViewById(R.id.adView_2picstoword_kiemruby);
//        this.adview_2pics1word_kiemruby.loadAd(new Builder().addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB").build());
//
//        CheckQuyen();
//        this.re_cdgame.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                KiemBitCoinActivity.this.CdGame();
//            }
//        });
//        this.re_xemvideo.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                KiemBitCoinActivity.this.ShowUnityAds();
//            }
//        });
//        this.re_sharegame.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                KiemBitCoinActivity.this.ShareGame();
//            }
//        });
//        this.re_gamehaykhac.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                KiemBitCoinActivity.this.GameKhac();
//            }
//        });
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        this.callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//
//    protected void onResume() {
//        super.onResume();
//        if (this.trangthai == 1) {
//            AddBitCoinCDApp();
//        }
//        Update_BitCoin();
//        SetBackgroundCDapp();
//        TinhNgaySetBackground();
//    }
//
//    protected void onDestroy() {
//        super.onDestroy();
//        try {
//            if (this.soundclick != null) {
//                this.soundclick.release();
//                this.soundclick = null;
//            }
//        } catch (Exception e) {
//        }
//    }

    public void Load_BitCoinTuBoNho() {
        try {
            SharedPreferences pref = getSharedPreferences("MYDATAREF", 0);
            String score_default = MCrypt.encrypt(this.key_AES, "100");
            try {
                this.score = Integer.parseInt(MCrypt.decrypt(this.key_AES, pref.getString("KEY_LINKDOWMLOAD_ABC_KT", score_default)).trim());
            } catch (Exception e) {
                this.score = Integer.parseInt(MCrypt.decrypt(this.key_AES, score_default).trim());
            }
        } catch (Exception e2) {
        }
        tv_bitcoin.setText(this.score +"");
    }
//
    public void Save_BitCoinXuongBoNho() {
        Editor editor = getSharedPreferences("MYDATAREF", 0).edit();
        try {
            editor.putString("KEY_LINKDOWMLOAD_ABC_KT", MCrypt.encrypt(this.key_AES, String.valueOf(this.score)));
        } catch (Exception e) {
        }
        editor.putInt("KEY_RUBY_SCORE", this.score);
        editor.commit();
    }

//    public void Load_CauHoiTuBoNho() {
//        try {
//            SharedPreferences pref = getSharedPreferences("MYDATAREF", 0);
//            String index_default = MCrypt.encrypt(this.key_AES, AppEventsConstants.EVENT_PARAM_VALUE_NO);
//            try {
//                this.index = Integer.parseInt(MCrypt.decrypt(this.key_AES, pref.getString("KEY_LINKDOWMLOAD_XYZ_Q", index_default)).trim());
//            } catch (Exception e) {
//                this.index = Integer.parseInt(MCrypt.decrypt(this.key_AES, index_default).trim());
//            }
//        } catch (Exception e2) {
//        }
//    }

//    public void CdGame() {
//        if (this.issound.booleanValue() && this.soundclick != null) {
//            this.soundclick.start();
//        }
//        SharedPreferences pref = getSharedPreferences("MYDATAREF", 0);
//        this.cdapp = Boolean.valueOf(pref.getBoolean("KEY_CDAPP", false));
//        this.linklaytudata = pref.getString("KEY_LINKAPP", this.macdinh);
//        Load_BitCoinTuBoNho();
//        if (this.linkapp == "") {
//            this.linkapp = this.linklaytudata;
//        }
//        if (!this.cdapp.booleanValue()) {
//            try {
//                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.linkapp)));
//                this.trangthai = 1;
//            } catch (ActivityNotFoundException e) {
//                Toast.makeText(this, " unable to find market app", 1).show();
//            }
//        } else if (this.linklaytudata.equalsIgnoreCase(this.linkapp)) {
//            AlertDialog.Builder alertDialogBuildertb1 = new AlertDialog.Builder(this, 3);
//            alertDialogBuildertb1.setTitle("Cài Đặt Game");
//            alertDialogBuildertb1.setIcon(R.drawable.ic_appgame);
//            alertDialogBuildertb1.setMessage("Bạn đã cài đặt game này rồi, chờ game mới nha bạn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alertDialog1 = alertDialogBuildertb1.create();
//            if (!isFinishing()) {
//                alertDialog1.show();
//            }
//            this.re_cdgame.setVisibility(4);
//        } else {
//            try {
//                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.linkapp)));
//                this.trangthai = 1;
//            } catch (ActivityNotFoundException e2) {
//                Toast.makeText(this, " unable to find market app", 1).show();
//            }
//        }
//    }
//
//    private Boolean CheckCDGame(String linkapp) {
//        if (getPackageManager().getLaunchIntentForPackage(linkapp.substring(linkapp.lastIndexOf("=") + 1, linkapp.length()).trim()) != null) {
//            return Boolean.valueOf(true);
//        }
//        return Boolean.valueOf(false);
//    }

//    public void AddBitCoinCDApp() {
//        Load_BitCoinTuBoNho();
//        if (CheckCDGame(this.linkapp).booleanValue()) {
//            this.score += 100;
//            Save_BitCoinXuongBoNho();
//            Editor editor = getSharedPreferences("MYDATAREF", 0).edit();
//            editor.putBoolean("KEY_CDAPP", true);
//            editor.putString("KEY_LINKAPP", this.linkapp);
//            editor.commit();
//            this.checkgio = new CountDownTimer(2000, 1000) {
//                public void onTick(long millisUntilFinished) {
//                }
//
//                public void onFinish() {
//                    AlertDialog.Builder alertDialogBuildertb1 = new AlertDialog.Builder(KiemBitCoinActivity.this, 3);
//                    alertDialogBuildertb1.setTitle("Thưởng BitCoin");
//                    alertDialogBuildertb1.setIcon(R.drawable.ic_appgame);
//                    alertDialogBuildertb1.setMessage("Bạn được thưởng 100 BitCoin cho việc cài đặt game nà. Thanks bạn rất nhiều nha!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                            KiemBitCoinActivity.this.re_cdgame.setVisibility(View.GONE);
//                            KiemBitCoinActivity.this.Update_BitCoin();
//                            KiemBitCoinActivity.this.trangthai = 0;
//                        }
//                    });
//                    AlertDialog alertDialog1 = alertDialogBuildertb1.create();
//                    if (!KiemBitCoinActivity.this.isFinishing()) {
//                        alertDialog1.show();
//                    }
//                }
//            }.start();
//            return;
//        }
//        Toast.makeText(this, "Bạn chưa hoàn thành cài đặt game nà. Thử lại nào bạn!", 0).show();
//    }

//    private void Update_BitCoin() {
//        Load_BitCoinTuBoNho();
//        this.tv_ruby.setText(this.score + "");
//    }

//    private void SetBackgroundCDapp() {
//        if (this.index > 5) {
//            this.re_cdgame.setVisibility(View.VISIBLE);
//            if (!this.cdapp.booleanValue()) {
//                this.re_cdgame.setBackgroundResource(R.drawable.button_gamehaykhac_effect);
//                return;
//            } else if (this.linkapp == "") {
//                this.linkapp = this.linklaytudata;
//                this.re_cdgame.setVisibility(View.GONE);
//                return;
//            } else if (this.linklaytudata.equalsIgnoreCase(this.linkapp)) {
//                this.re_cdgame.setVisibility(View.GONE);
//                return;
//            } else {
//                this.re_cdgame.setVisibility(View.VISIBLE);
//                this.re_cdgame.setBackgroundResource(R.drawable.button_gamehaykhac_effect);
//                return;
//            }
//        }
//        this.re_cdgame.setVisibility(View.GONE);
//    }

//    public void ShowUnityAds() {
//        if (this.issound.booleanValue() && this.soundclick != null) {
//            this.soundclick.start();
//        }
//        this.viewadsunity = getSharedPreferences("MYDATAREF", 0).getInt("KEY_LINKADS", 0);
//        if (!this.cd.isConnectingToInternet()) {
//            AlertDialog.Builder alertDialogBuildertb1 = new AlertDialog.Builder(this, 3);
//            alertDialogBuildertb1.setTitle("Xem Video !");
//            alertDialogBuildertb1.setIcon(R.drawable.ic_appgame);
//            alertDialogBuildertb1.setMessage("Bạn vui lòng kiểm tra lại kết nối Internet đi nà bạn!").setPositiveButton("OK! Kiểm Tra Ngay", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alertDialog1 = alertDialogBuildertb1.create();
//            if (!isFinishing()) {
//                alertDialog1.show();
//            }
//        } else if (!UnityAds.isReady() || this.viewadsunity >= 15) {
//            AlertDialog.Builder alertDialogBuildertb2 = new AlertDialog.Builder(this, 3);
//            alertDialogBuildertb2.setTitle("Xem Video!");
//            alertDialogBuildertb2.setIcon(R.drawable.ic_appgame);
//            alertDialogBuildertb2.setMessage("Xin lỗi bạn nà. Hiện tại chưa có video để xem nà, quay lại vào lúc khác nhé bạn!").setPositiveButton(HTTP.CONN_CLOSE, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alertDialog2 = alertDialogBuildertb2.create();
//            if (!isFinishing()) {
//                alertDialog2.show();
//            }
//        } else {
//            UnityAds.show(this);
//        }
//    }

//    public void GameKhac() {
//        if (this.issound.booleanValue() && this.soundclick != null) {
//            this.soundclick.start();
//        }
//        try {
//            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=pub:Ahihi+Studio+GameVN")));
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(getApplicationContext(), " unable to find market app", 1).show();
//        }
//    }
//
//    public void ShareGame() {
//        if (this.issound.booleanValue() && this.soundclick != null) {
//            this.soundclick.start();
//        }
//        if (!this.cd.isConnectingToInternet()) {
//            AlertDialog.Builder alertDialogBuildertb2 = new AlertDialog.Builder(this, 3);
//            alertDialogBuildertb2.setTitle("Share Game");
//            alertDialogBuildertb2.setIcon(R.drawable.ic_appgame);
//            alertDialogBuildertb2.setMessage("Bạn vui lòng kiểm tra lại kết nối Internet nha bạn!").setPositiveButton("OK! Kiểm Tra Ngay", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alertDialog2 = alertDialogBuildertb2.create();
//            if (!isFinishing()) {
//                alertDialog2.show();
//            }
//        } else if (this.issharegame.booleanValue()) {
//            AlertDialog.Builder alertDialogBuildertb1 = new AlertDialog.Builder(this, 3);
//            alertDialogBuildertb1.setTitle("Share Game");
//            alertDialogBuildertb1.setIcon(R.drawable.ic_appgame);
//            alertDialogBuildertb1.setMessage("Hôm nay bạn đã share game rồi nà. Quay lại vào ngày mai nha bạn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.cancel();
//                    KiemBitCoinActivity.this.re_sharegame.setBackgroundResource(R.drawable.bt_export);
//                }
//            });
//            AlertDialog alertDialog1 = alertDialogBuildertb1.create();
//            if (!isFinishing()) {
//                alertDialog1.show();
//            }
//        } else {
//            share_Fb();
//        }
//    }
//
//    protected void share_Fb() {
//        ShareDialog shareDialog = new ShareDialog((Activity) this);
//        if (ShareDialog.canShow(ShareLinkContent.class)) {
//            shareDialog.show(((ShareLinkContent.Builder) new ShareLinkContent.Builder().setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=ahihi.studiogamevn.haihinhmotchu"))).build());
//        }
//        shareDialog.registerCallback(this.callbackManager, new FacebookCallback<Result>() {
//            public void onCancel() {
//                Toast.makeText(KiemBitCoinActivity.this, "Bạn chưa hoàn thành việc Share Game. Thử lại nào bạn!", 0).show();
//            }
//
//            public void onSuccess(Result result) {
//                KiemBitCoinActivity.this.AddBitCoin_ShareGame();
//            }
//
//            public void onError(FacebookException exception) {
//                Toast.makeText(KiemBitCoinActivity.this, "Bạn chưa hoàn thành việc Share Game. Thử lại nào bạn!", 0).show();
//            }
//        });
//    }
//
//    public void AddBitCoin_ShareGame() {
//        Load_BitCoinTuBoNho();
//        this.score += 100;
//        Editor editor = getSharedPreferences("MYDATAREF", 0).edit();
//        editor.putBoolean("KEY_SHARE_APP", true);
//        editor.commit();
//        Save_BitCoinXuongBoNho();
//        AlertDialog.Builder alertDialogBuildertb1 = new AlertDialog.Builder(this, 3);
//        alertDialogBuildertb1.setTitle("Thưởng BitCoin");
//        alertDialogBuildertb1.setIcon(R.drawable.ic_appgame);
//        alertDialogBuildertb1.setMessage("Bạn được Tặng 100 BitCoin cho việc Share game hàng ngày nà!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//                KiemBitCoinActivity.this.re_sharegame.setBackgroundResource(R.drawable.bt_export);
//            }
//        });
//        AlertDialog alertDialog1 = alertDialogBuildertb1.create();
//        if (!isFinishing()) {
//            alertDialog1.show();
//        }
//    }
//
//    private void TinhNgaySetBackground() {
//        this.issharegame = Boolean.valueOf(getSharedPreferences("MYDATAREF", 0).getBoolean("KEY_SHARE_APP", false));
//        if (this.issharegame.booleanValue()) {
//            this.re_sharegame.setBackgroundResource(R.drawable.bt_export);
//        } else {
//            this.re_sharegame.setBackgroundResource(R.drawable.button_gamehaykhac_effect);
//        }
//    }

//    private void settingDummyData() {
//        this.games = new ArrayList();
//        this.games.add(new Game(this.game1_icon, this.game1_link));
//        this.games.add(new Game(this.game2_icon, this.game2_link));
//        this.games.add(new Game(this.game3_icon, this.game3_link));
//        this.games.add(new Game(this.game4_icon, this.game4_link));
//    }

    public void CheckQuyen() {
        boolean hasPermission;
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            hasPermission = true;
        } else {
            hasPermission = false;
        }
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 112);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 112:
                if (grantResults.length > 0 && grantResults[0] == 0) {
                    onResume();
                    return;
                }
                return;
            default:
                return;
        }
    }
}

package com.jjm.android.quizanoi.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjm.android.quizanoi.R;
import com.jjm.android.quizanoi.model.GlobalVar;

///import com.google.android.gms.ads.AdRequest.Builder;
///import com.google.android.gms.ads.AdView;

public class KetQuaDungActivity extends AppCompatActivity {
///    private AdView adview_2pics1word_result;
    Animation animhoatHinh;
    int[] arrIMG = new int[]{R.drawable.ok_1, R.drawable.ok_2, R.drawable.ok_3, R.drawable.ok_4, R.drawable.ok_5, R.drawable.ok_6,R.drawable.ok_7,R.drawable.ok_1};
//    int[] arrIMG = new int[]{R.drawable.retro_ok};
    Button bt_choitiep;
    Button bt_gamekhac;
    String dapan = "";
    String laichu = "";
    Boolean issound = Boolean.valueOf(true);
    ImageView iv_image_result;
    //String[] loichucNN = new String[]{"!!MAY MẮN QUÁ AH!!", "CÂU SAU BÍ CHO COI!", "!!!GHÊ GHÊ!!!", "CHÓ NGÁP PHẢI RUỒI", "CÂU NÀY DỄ ẸC", "!!NHÀ NGÔN NGỮ HỌC!!", "THÁNH NÓI LÁI CMNR"};
    //String[] loichucNN = new String[]{"!!MAY MAÉN QUAÙ AH!!", "CAÂU SAU BÍ CHO COI!", "!!!GHEÂ GHEÂ!!!", "CHOÙ NGAÙP PHAÛI RUOÀI", "CAÂU NAØY DEÃ EÏC", "!!NHAØ NGOÂN NGÖÕ HOÏC!!", "THAÙNH NOÙI LAÙI CMNR"};
    String[] loichucNN = new String[]{"!!KHOÂNG LAØM KHOÙ ÑÖÔÏC ROÀI!!", "CAÂU SAU XEM THEÁ NAØO!", "!!!GHEÂ GHEÂ!!!", "CÔØ HOÙ NGAÙP PHAÛI RÔØ UOÀI", "CAÂU NAØY DEÃ EÏC", "!!GS NGOÂN NGÖÕ HOÏC!!", "THAÙNH NOÙI LAÙI CMNR"};
    int maxi = 6;
    int min = 0;
    MediaPlayer soundclick;
    TextView tv_dapancuoicung;
    TextView tv_laichu;
    TextView tvloichuc;
    TextView tv_congdiem;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_ket_qua_dung);
        getSupportActionBar().hide();
        this.bt_choitiep = (Button) findViewById(R.id.bt_choitiep);
//        this.bt_gamekhac = (Button) findViewById(R.id.bt_gamekhac);
        this.tvloichuc = (TextView) findViewById(R.id.tvloichuc);
        this.tv_dapancuoicung = (TextView) findViewById(R.id.tv_dapancuoicung);
        this.tv_laichu = (TextView) findViewById(R.id.tv_laichu);
        this.iv_image_result = (ImageView) findViewById(R.id.iv_image_result);
        tv_congdiem = findViewById(R.id.tvcongdiem);
        SharedPreferences pref = getSharedPreferences("MYDATAREF", 0);
        this.dapan = GlobalVar.dapan;
        this.laichu = GlobalVar.laichu;
        this.issound = Boolean.valueOf(pref.getBoolean("KEY_ISSOUND", true));
        try {
//            this.soundclick = MediaPlayer.create(this, R.raw.soundclick);
        } catch (Exception e) {
        }
        int random = this.min + ((int) (Math.random() * ((double) ((this.maxi - this.min) + 1))));
        Typeface type_congrat = Typeface.createFromAsset(getAssets(),"fonts/VCANUN.TTF");
        this.tvloichuc.setTypeface(type_congrat);
        this.tvloichuc.setText(this.loichucNN[random]);

        this.iv_image_result.setImageResource(this.arrIMG[random]);
        Typeface type_answer = Typeface.createFromAsset(getAssets(),"fonts/VCANUN.TTF");
        this.tv_dapancuoicung.setTypeface(type_answer,Typeface.BOLD);
        this.tv_dapancuoicung.setText(this.dapan);
        this.tv_laichu.setTypeface(type_answer,Typeface.BOLD);
        this.tv_laichu.setText(this.laichu);

        Typeface type_congdiem = Typeface.createFromAsset(getAssets(),"fonts/Bellerose.ttf");
        tv_congdiem.setTypeface(type_congdiem,Typeface.BOLD);

//        this.animhoatHinh = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
//        this.tvloichuc.startAnimation(this.animhoatHinh);
//        LoadBannerAds();
        this.bt_choitiep.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                KetQuaDungActivity.this.ChoiTiep();
            }
        });
//        this.bt_gamekhac.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                KetQuaDungActivity.this.GameKhac();
//            }
//        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Quiz_new.class));
        finish();
    }

    protected void onResume() {
        super.onResume();
//        if (this.adview_2pics1word_result != null) {
//            this.adview_2pics1word_result.resume();
//        }
        this.issound = Boolean.valueOf(getSharedPreferences("MYDATAREF", 0).getBoolean("KEY_ISSOUND", true));
    }

    protected void onPause() {
        super.onPause();
//        if (this.adview_2pics1word_result != null) {
//            this.adview_2pics1word_result.pause();
//        }
    }

    protected void onDestroy() {
        super.onDestroy();
        try {
            if (this.soundclick != null) {
                this.soundclick.release();
                this.soundclick = null;
            }
        } catch (Exception e) {
        }
//        if (this.adview_2pics1word_result != null) {
//            this.adview_2pics1word_result.destroy();
//        }
    }

    public void ChoiTiep() {
        if (this.issound.booleanValue() && this.soundclick != null) {
            this.soundclick.start();
        }
        startActivity(new Intent(this, Quiz_new.class));
        finish();
    }

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

    public void LoadBannerAds() {
//        this.adview_2pics1word_result = (AdView) findViewById(R.id.adView_2picstoword_result);
//        this.adview_2pics1word_result.loadAd(new Builder().addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB").build());
    }
}

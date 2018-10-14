package com.jjm.android.quizanoi.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.facebook.appevents.AppEventsConstants;
import com.jjm.android.quizanoi.R;
import org.apache.http.protocol.HTTP;
import com.jjm.android.quizanoi.handler.ConnectionDetector;
import com.jjm.android.quizanoi.model.CauHoi;
import com.jjm.android.quizanoi.model.GlobalVar;
import com.jjm.android.quizanoi.model.QuanlyCauHoi;
import com.jjm.android.quizanoi.util.MCrypt;

public class MainActivity extends AppCompatActivity {

    Button btn_play;
    ///Button btn_bitcoin;
    Button bt_chiasegame;
    Button bt_reset;
    ConnectionDetector cd;
    TextView archivement;
    TextView bitscore;

    //int version = 3;
    int score = 30 ;
    List<CauHoi> ds_cauhoi;
  
    Typeface type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setContentView((int) R.layout.welcome);
        this.btn_play = (Button) findViewById(R.id.btn_play);
        this.btn_play.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.ChoiGame();
            }
        });

        ///this.btn_bitcoin = (Button) findViewById(R.id.btn_kiem_bitcoin);
        ///this.btn_bitcoin.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                KiemBitCoin();
//            }
//        });

        this.bt_chiasegame = (Button) findViewById(R.id.btn_sharegame);
        this.bt_chiasegame.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ChiaSeGame();
            }
        });

        this.bt_reset = (Button) findViewById(R.id.btn_reset);
        this.bt_reset.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ResetGame();
            }
        });
        type = Typeface.createFromAsset(getAssets(),"fonts/Bellerose.ttf");
        this.cd = new ConnectionDetector(this);
        archivement = findViewById(R.id.tv_arch);
        bitscore = findViewById(R.id.tv_bitscore);
        Load_CauHoiTuBoNho();
        LoadCauHoi();
        Load_BitCoinTuBoNho();
        String arch = GlobalVar.finished_question_list.split(",").length - 1+"";
        GlobalVar.allQuestion = this.ds_cauhoi.size();
        this.archivement.setTypeface(type);
        this.archivement.setText(arch+ "/"+GlobalVar.allQuestion+"");
        this.bitscore.setTypeface(type);
        this.bitscore.setText(this.score+"");

    }
    public void ResetGame() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, 1);
        alertDialogBuilder.setTitle("Chơi Lại Từ Đầu");
        alertDialogBuilder.setIcon(R.drawable.e_2);

//		Dialog dialog;
//        dialog=new Dialog(this);
//        //SET TITLE
//        dialog.setTitle("Player");
//        dialog.setContentView(R.layout.custom_dialog);
        //alertDialogBuilder.setIcon(R.drawable.ic_appgame);
        alertDialogBuilder.setMessage("Game Sẽ Bắt Đầu Lại Từ Đầu, Bạn Chắc Chắn Chứ").setPositiveButton("Chắc Chắn", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor editor = getSharedPreferences("MYDATAREF", 0).edit();
                score = 30;
                GlobalVar.finished_question_list ="\"0000\"";
                GlobalVar.skipQuestionList ="\"0000\"";

                try {
                    editor.putString("BIT_SCORE", MCrypt.encrypt( GlobalVar.key_AES, String.valueOf( score)));
                    editor.putString("FINISHED_QUESTION", MCrypt.encrypt(GlobalVar.key_AES, String.valueOf( GlobalVar.finished_question_list)));
                    editor.putString("SKIP_QUESTION", MCrypt.encrypt(GlobalVar.key_AES, String.valueOf( GlobalVar.skipQuestionList )));
                } catch (Exception e) {

                }

                editor.commit();
                MainActivity.this.recreate();
            }
        }).setNegativeButton("Lỡ Tay Bấm Nhầm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.create().show();



    }
    public void Load_CauHoiTuBoNho() {
        try {
            SharedPreferences pref = getSharedPreferences("MYDATAREF", 0);
            String index_default = MCrypt.encrypt(GlobalVar.key_AES, AppEventsConstants.EVENT_PARAM_VALUE_NO);
            try {
                //this.index = Integer.parseInt(MCrypt.decrypt(GlobalVar.key_AES, pref.getString("KEY_LINKDOWMLOAD_XYZ_Q", index_default)).trim());
                //this.index = 0;
                GlobalVar.finished_question_list =MCrypt.decrypt(GlobalVar.key_AES, pref.getString("FINISHED_QUESTION", index_default));
                GlobalVar.skipQuestionList = MCrypt.decrypt(GlobalVar.key_AES, pref.getString("SKIP_QUESTION", index_default));
            } catch (Exception e) {
                //this.index = Integer.parseInt(MCrypt.decrypt(GlobalVar.key_AES, index_default).trim());
                GlobalVar.finished_question_list ="\"0000\"";
                GlobalVar.skipQuestionList ="\"0000\"";
            }
        } catch (Exception e2) {
        }

    }
    public void Load_BitCoinTuBoNho() {
        try {
            SharedPreferences pref = getSharedPreferences("MYDATAREF", 0);
            String score_default = MCrypt.encrypt(GlobalVar.key_AES, "30");
            try {
                this.score = Integer.parseInt(MCrypt.decrypt(GlobalVar.key_AES, pref.getString("BIT_SCORE", score_default)).trim());
            } catch (Exception e) {
                this.score = Integer.parseInt(MCrypt.decrypt(GlobalVar.key_AES, score_default).trim());
            }
        } catch (Exception e2) {
        }
    }
    public void LoadCauHoi() {
        QuanlyCauHoi db = new QuanlyCauHoi(this);
        try {
            db.createDataBase(GlobalVar.DBVersion);
        } catch (IOException e) {
            AlertDialog.Builder alertDialogBuildertb1 = new AlertDialog.Builder(this, 1);
            alertDialogBuildertb1.setTitle("Loi DB");
            alertDialogBuildertb1.setMessage("Loi");
        }
        this.ds_cauhoi = new ArrayList();
        this.ds_cauhoi = db.layNCauHoi("\"0000\"","\"0000\"");
        //if (this.index < this.ds_cauhoi.size()) {
        if (GlobalVar.finished_question_list.split(",").length -1 < this.ds_cauhoi.size()) {

            //HienThi(0);
        } else {
            AlertDialog.Builder alertDialogBuildertb1 = new AlertDialog.Builder(this, 1);
            alertDialogBuildertb1.setTitle("Chúc Mừng!!");
            ///alertDialogBuildertb1.setIcon(R.drawable.ic_appgame);
            alertDialogBuildertb1.setMessage("Hết Câu Hỏi Rồi! Chờ Bản Cập Nhật Nhé").setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog alertDialog = alertDialogBuildertb1.create();
            if (!isFinishing()) {
                alertDialog.show();
            }
        }
        db.close();
    }
    public void ChoiGame() {
//        if (this.issound.booleanValue() && this.soundclick != null) {
//            this.soundclick.start();
//        }
        startActivity(new Intent(this, Quiz_new.class));
        finish();
    }
    public void KiemBitCoin() {
        startActivity(new Intent(this, KiemBitCoinActivity.class));
        //finish();
    }

    private void shareAppFacebook() {
        try {
            Intent share = new Intent("android.intent.action.SEND");
            share.setType(HTTP.PLAIN_TEXT_TYPE);
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            share.putExtra("android.intent.extra.SUBJECT", "Game Vừa Hài Vừa Khó!!!");
            //share.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=com.jjm.android.quizanoi");
            share.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=com.jjm.android.quizanoi \n https://itunes.apple.com/vn/app/chat-h%E1%BA%B9n-h%C3%B2-jaumo/id522681493?l=vi&mt=8");
            startActivity(Intent.createChooser(share, "Chia Sẻ -Lái Chữ- Đến Mọi Người!"));
        } catch (Exception e) {
        }
    }

    public void ChiaSeGame() {

        if (this.cd.isConnectingToInternet()) {
            try {
                shareAppFacebook();
                return;
            } catch (Exception e) {
                return;
            }
        }
        Builder alertDialogBuildertb1 = new Builder(this, 3);
        alertDialogBuildertb1.setTitle("Thông Báo");
        ///alertDialogBuildertb1.setIcon(R.drawable.retro);
        alertDialogBuildertb1.setMessage("Kiểm Tra Internet Nhé").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialogBuildertb1.create().show();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void LoadSound() {

    }



}

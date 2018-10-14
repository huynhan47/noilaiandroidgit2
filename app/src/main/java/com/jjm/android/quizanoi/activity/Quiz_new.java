package com.jjm.android.quizanoi.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import roboguice.inject.InjectExtra;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.inject.Inject;
import com.jjm.android.quizanoi.Config;
import com.jjm.android.quizanoi.R;

import com.jjm.android.quizanoi.model.DataSource;
import com.jjm.android.quizanoi.model.GlobalVar;
import com.jjm.android.quizanoi.model.Question;
import com.jjm.android.quizanoi.util.SoundPoolAssistant;
import com.jjm.android.quizanoi.util.Task;
import com.jjm.android.quizanoi.model.CauHoi;
import com.jjm.android.quizanoi.handler.TextViewAndButton;

import com.jjm.android.quizanoi.model.QuanlyCauHoi;
import com.jjm.android.quizanoi.util.MCrypt;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import android.util.Log;
public class Quiz_new extends AppCompatActivity implements RewardedVideoAdListener {

	public static final String CATEGORY_ID_EXTRA = "categoryId";
	public static final String MODE_EXTRA = "mode";
	private static final String QUESTION_INDEX_KEY = "questionIndex";
	private static final String SEED_KEY = "seed";
	private static final String NUM_ANSWERED_KEY = "numAnswered";
	private static final String NUM_CORRECT_KEY = "numCorrect";

	// Audio Quiz
	private SoundPoolAssistant mSoundPool;
	private Question[] mQuestions;
	private int mQuestionIndex;
	private long mSeed;
	private int mNumAnswered;
	private int mNumCorrect;
	private Task mNextQuestionTask;
	private Handler mHandler = new Handler();
	private boolean mHighScore;
	private float mScore;

	Animation anim_Hint;
	Animation animhoatHinh;
	ArrayList<Button> arrBT;
	ArrayList<String> arrKyTuDung = new ArrayList();
	ArrayList<String> arrKyTuSai = new ArrayList();
	String[] arrSaveSuggest;
	ArrayList<TextView> arrTV;
	ArrayList<TextViewAndButton> arrTandB = new ArrayList();
	String[] arrTaoKiTuButonRanDom = new String[16];
	String[] arrTaoThemKiTuDapAn = new String[16];
	String[] arrdapan;
	Button bt_WORD1;
	Button bt_WORD2;
	Button bt_chiase;
	Button bt_goiy;
	Button bt_quaylai;
	Button bt_removekitusai;
	Button bt_skip;
	Button share_question;
	CauHoi cauhientai;
	CountDownTimer checkgio;
	int chiso = 0;
	List<CauHoi> ds_cauhoi;
	String image = "";
	File imagepath;
	int index = 0;
	String finished_question_list = "\"0000\"";
	//String skip_question_list = "\"0000\"";
	Boolean isSuggest = Boolean.valueOf(false);
	Boolean issound = Boolean.valueOf(true);
	ImageView iv_expand;
	ImageView ivhinh1;
	ImageView ivhinh2;
	ImageView QuynhMCImage;
	String key_AES = "anoistudio.duoihinhlaichu";
	Boolean ktapp = Boolean.valueOf(false);
	int maxi = 7;
	int min = 0;
	int score = 150;
	int slopen = 0;
	MediaPlayer soundclick;
	MediaPlayer soundloss;
	MediaPlayer soundwin;
	TableRow tbr1;
	TableRow tbr2;
	TableRow tbr3;
	TableRow tbr4;
	int time = 0;
	String[] traloisaiNN = new String[]{"Sôi Rài Sôi Rài",
            "Thử Lại Bạn Ơi! Sai Rồi",
            "Sai Rồi! Thử Sử Dụng Các Trợ Giúp Khi Khó Khăn Nha",
            "Sai Rồi, Thử Nhờ Ông Thần Facebook Xem Sao Bạn Ơi! Nút Share Kìa Bạn",
			"Sôi Rài Sôi Rài",
			"Thử Lại Bạn Ơi! Sai Rồi",
			"Sai Rồi! Thử Sử Dụng Các Trợ Giúp Khi Khó Khăn Nha",
			"Sai Rồi, Thử Nhờ Ông Thần Facebook Xem Sao Bạn Ơi! Nút Share Kìa Bạn"};
	String[] QuynhMCArray = {"quynhmc_1","quynhmc_2","quynhmc_3","quynhmc_4","quynhmc_5","quynhmc_6","quynhmc_7","quynhmc_8"};
	TextView tv_cauhoi;
	TextView tv_firstword;
	TextView tv_score;
	TextView tv_secondword;
	int version = 2;
	Boolean word1 = Boolean.valueOf(false);
	Boolean word2 = Boolean.valueOf(false);
	int[] Hinh_Troll = new int[]{R.drawable.e_1, R.drawable.e_2, R.drawable.e_3,R.drawable.e_4, R.drawable.e_5,R.drawable.e_6,R.drawable.e_7,R.drawable.e_8};
	int soluong=7;
	Typeface type;
	private InterstitialAd interstitial;
	private RewardedVideoAd mRewardedVideoAd;
	private AdView adBanner;
	@Inject
	private Config mConfig;

	@Inject
	private DataSource mDataSource;
//
//	@InjectView(R.id.progress)
//	private TextView mProgressTextView;
//
//	@InjectView(R.id.fragmentContainer)
//	private ViewGroup mFragmentContainer;
//
//	@InjectView(R.id.messages)
//	private View mSoundWarning;
//
//	@InjectView(R.id.score)
//	private TextView mScoreTextView;
//
	@InjectExtra(CATEGORY_ID_EXTRA)
	private long mCategoryId;
//
//	@InjectExtra(MODE_EXTRA)
//	private int mMode;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz1pic);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.tbr1 = (TableRow) findViewById(R.id.tableRow1);
		this.tbr2 = (TableRow) findViewById(R.id.tableRow2);
		this.tbr3 = (TableRow) findViewById(R.id.tableRow3);
		this.tbr4 = (TableRow) findViewById(R.id.tableRow4);

		this.ivhinh1 = (ImageView) findViewById(R.id.imageView_hinhanh1);
		this.QuynhMCImage = (ImageView) findViewById(R.id.QuynhMCImage);
		this.tv_score = (TextView) findViewById(R.id.tvdiemso);
		this.tv_cauhoi = (TextView) findViewById(R.id.tv_cauhoi);

		type = Typeface.createFromAsset(getAssets(),"fonts/Bellerose.ttf");

		this.arrTV = new ArrayList();
		this.arrBT = new ArrayList();

		//Reset Only for Debug
		///this.index = 79;
		//Save_CauHoiXuongBoNho();
		//Reset Only for Debug

		getAllTextViewAndButton();
		EventButton();
		EventTextview();
		Load_CauHoiTuBoNho();
		Load_BitCoinTuBoNho();
		LoadCauHoi();
		KTApp();
		LoadQCBanner();
		LoadQCFullScreen();
		//confirmFireMissiles();
		this.share_question = findViewById(R.id.btn_share_question);
		this.share_question.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ChiaSeCauHoi();
			}
		});

		this.bt_skip = findViewById(R.id.bt_skiplevel);
		this.bt_skip.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Skip();
			}
		});

		bt_quaylai= findViewById(R.id.btn_back);
		bt_quaylai.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Back_Game();
			}
		});

		String arch = GlobalVar.finished_question_list.split(",").length -1 +"";
		this.tv_cauhoi.setTypeface(type);
		this.tv_cauhoi.setText(arch+ "/"+GlobalVar.allQuestion);
		//
		mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
		mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.loadAd(
//				"ca-app-pub-4345988626634460/7809402917", new AdRequest.Builder() //tdp
//                "ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder() //google test
                "ca-app-pub-8204407936442788/7465721700", new AdRequest.Builder() //Video Android Paper Hero Prd
                        .addTestDevice("9038137B1975D034E005EEE96541909F")
                        .addTestDevice("5EAF8DE05E8974A7D5C369BD0A00C22B")
                        .build());
		adBanner = findViewById(R.id.adView_2picstoword);

        //adBanner.setAdSize(AdSize.SMART_BANNER);
		//adBanner.setAdUnitId("ca-app-pub-8204407936442788/9006034085");
		AdRequest adrequest = new AdRequest.Builder() //Video Android Paper Hero Prd
				.addTestDevice("9038137B1975D034E005EEE96541909F")
				.addTestDevice("5EAF8DE05E8974A7D5C369BD0A00C22B")
				.build();
		adBanner.loadAd(adrequest);
	}
	public void TaoDapAnNgauNhien() {
		int i;
		//String[] arrtemp = "I_L_o_v_e_Y_o_u".split("_");
		String[] arrtemp = this.cauhientai.kitu.split("_");
		int lenght = arrtemp.length;
		this.arrdapan = new String[lenght];
		for (i = 0; i < arrtemp.length; i++) {
			this.arrTaoThemKiTuDapAn[i] = arrtemp[i];
		}
		Random random = new Random();
		for (i = lenght; i < this.arrTaoThemKiTuDapAn.length; i++) {
			this.arrTaoThemKiTuDapAn[i] = String.valueOf((char) (random.nextInt(26) + 65));
		}
		Random rd = new Random();
		ArrayList<Integer> number = new ArrayList();
		int count = 0;
		while (count != this.arrTaoKiTuButonRanDom.length) {
			int n = rd.nextInt(this.arrTaoThemKiTuDapAn.length);
			if (!number.contains(Integer.valueOf(n))) {
				number.add(Integer.valueOf(n));
				this.arrTaoKiTuButonRanDom[n] = this.arrTaoThemKiTuDapAn[count];
				count++;
			}
		}
		this.arrKyTuDung.clear();
		for (i = 0; i < this.cauhientai.soluongkitu; i++) {
			this.arrKyTuDung.add(this.arrTaoThemKiTuDapAn[i]);
		}
		this.arrSaveSuggest = new String[this.arrKyTuDung.size()];
		for (i = 0; i < this.arrSaveSuggest.length; i++) {
			this.arrSaveSuggest[i] = " ";
		}
		this.arrKyTuSai.clear();
		for (i = this.cauhientai.soluongkitu; i < 16; i++) {
			this.arrKyTuSai.add(this.arrTaoThemKiTuDapAn[i]);
		}
		SetTextButton();
		ShowTextView();
//		LoadSuggest();
	}
	public void ShowTextView() {
		for (int i = 0; i < this.cauhientai.soluongkitu; i++) {
			((TextView) this.arrTV.get(i)).setVisibility(View.VISIBLE);
			TextViewAndButton tb = new TextViewAndButton();
			tb.textview = (TextView) this.arrTV.get(i);
			this.arrTandB.add(tb);
		}
	}

	public void getAllTextViewAndButton() {
		int i;
		for (i = 0; i < this.tbr1.getChildCount(); i++) {
			TextView tv = (TextView) this.tbr1.getChildAt(i);
			tv.setVisibility(View.GONE);
			tv.setText("");
			this.arrTV.add(tv);
		}
		for (i = 0; i < this.tbr2.getChildCount(); i++) {
			TextView tv = (TextView) this.tbr2.getChildAt(i);
			tv.setVisibility(View.GONE);
			tv.setText("");
			this.arrTV.add(tv);
		}
		for (i = 0; i < this.tbr3.getChildCount(); i++) {
			Button tv2 = (Button) this.tbr3.getChildAt(i);
			tv2.setVisibility(View.VISIBLE);
			tv2.setTypeface(type);
			this.arrBT.add(tv2);
		}
		for (i = 0; i < this.tbr4.getChildCount(); i++) {
			Button tv2 = (Button) this.tbr4.getChildAt(i);
			tv2.setVisibility(View.VISIBLE);
			tv2.setTypeface(type);
			this.arrBT.add(tv2);
		}
	}
	public void SetTextButton() {
		for (int i = 0; i < this.arrTaoKiTuButonRanDom.length; i++) {
			((Button) this.arrBT.get(i)).setText(this.arrTaoKiTuButonRanDom[i]);
		}
	}
	public void EventButton() {
		for (int i = 0; i < this.arrBT.size(); i++) {
			final Button bt = (Button) this.arrBT.get(i);
			bt.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (!Quiz_new.this.isSuggest.booleanValue()) {
						int i = 0;
						while (i < Quiz_new.this.arrTandB.size()) {
							TextViewAndButton tandb = (TextViewAndButton) Quiz_new.this.arrTandB.get(i);
							if (tandb.isconnect.booleanValue()) {
								i++;
							} else {
								tandb.button = bt;
								tandb.isconnect = Boolean.valueOf(true);
								bt.setVisibility(View.INVISIBLE);
								tandb.textview.setText(bt.getText());
								tandb.textview.setTypeface(type); //Set font for Answer
								tandb.textview.setBackgroundResource(R.drawable.stroke_answer);
								//Quiz_new.this.arrTandB.set(i, tandb);
								Quiz_new quiz = Quiz_new.this;
								quiz.chiso++;
								quiz = Quiz_new.this;
								quiz.time++;
								if (Quiz_new.this.chiso == Quiz_new.this.cauhientai.soluongkitu) {
										Quiz_new.this.XyLyKetQua();
									return;
								}
								return;
							}
						}
					}
				}
			});
		}
	}
	public void EventTextview() {
		for (int i = 0; i < this.arrTV.size(); i++) {
			final int temp = i;
			((TextView) this.arrTV.get(i)).setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (Quiz_new.this.issound.booleanValue() && Quiz_new.this.soundclick != null) {
						Quiz_new.this.soundclick.start();
					}
					TextViewAndButton tandb = (TextViewAndButton) Quiz_new.this.arrTandB.get(temp);
					if (tandb.isconnect.booleanValue()) {
						tandb.textview.setText("");
						tandb.textview.setBackgroundResource(R.drawable.stroke);//Revert answer set styles
						tandb.isconnect = Boolean.valueOf(false);
						tandb.button.setVisibility(View.VISIBLE);
						//Quiz_new.this.arrTandB.set(temp, tandb);
						Quiz_new quiz_new = Quiz_new.this;
						quiz_new.chiso--;
					}
					if (Quiz_new.this.isSuggest.booleanValue()) {
//						Quiz_new.this.HintButtonPress(temp, tandb);
//						Quiz_new.this.arrSaveSuggest[temp] = tandb.textview.getText().toString();
//						Quiz_new.this.SaveSugget();
						if (Quiz_new.this.chiso == Quiz_new.this.cauhientai.soluongkitu) {
							Quiz_new.this.XyLyKetQua();
						}
					}
					for (int i = 0; i < Quiz_new.this.arrTV.size(); i++) {
						((TextView) Quiz_new.this.arrTV.get(i)).clearAnimation();
					}
				}
			});
		}
	}


	public void LoadCauHoi() {
//		QuanlyCauHoi db = new QuanlyCauHoi(this);
//		try {
//			db.createDataBase(this.version);
//		} catch (IOException e) {
//			AlertDialog.Builder alertDialogBuildertb1 = new AlertDialog.Builder(this, 1);
//			alertDialogBuildertb1.setTitle("Loi DB");
//			alertDialogBuildertb1.setMessage("Loi");db
//		}db
		this.ds_cauhoi = new ArrayList();
		this.ds_cauhoi = GlobalVar.db.layNCauHoi(GlobalVar.finished_question_list,GlobalVar.skipQuestionList);
		//if (this.index < this.ds_cauhoi.size()) {
		if (GlobalVar.finished_question_list.split(",").length -1 <  GlobalVar.db.QuestionCount()) {

		HienThi(0);
		} else {
			Back_Game();
			//AlertDialog.Builder alertDialogBuildertb1 = new AlertDialog.Builder(this, 1);
			//alertDialogBuildertb1.setTitle("Chúc Mừng!");
			///alertDialogBuildertb1.setIcon(R.drawable.ic_appgame);
			//alertDialogBuildertb1.setMessage("Bạn đã hoàn thành hết các câu hỏi. Các câu hỏi mới sẽ được cập nhật vào phiên bản mới.").setPositiveButton("OK! Chiến thôi", new DialogInterface.OnClickListener() {
			//	public void onClick(DialogInterface dialog, int id) {
			//		dialog.cancel();
			//		Back_Game();
			//	}
			//});

			//AlertDialog alertDialog = alertDialogBuildertb1.create();
			//if (!isFinishing()) {
			//	alertDialog.show();
			//}
		}
		GlobalVar.db.close();
	}
	public void HienThi(int vitri) {
		this.cauhientai = (CauHoi) this.ds_cauhoi.get(vitri);
		//this.tv_cauhoi.setText("Level " + (this.index + 1));
		this.tv_score.setTypeface(type);
		this.tv_score.setText(this.score + "");
		try {
			this.ivhinh1.setImageBitmap(getBitmapFromAssets("h_" + this.cauhientai._id + ".png"));
			//this.ivhinh2.setImageBitmap(getBitmap0FromAssets("h" + this.cauhientai._id + "_2.jpg"));

			Random ran = new Random();
			int x = ran.nextInt(8);
            Log.d("DEbug","x ne: " + String.valueOf(x));
            Log.d("DEbug","file ne: " + QuynhMCArray[x] + ".png");
			this.QuynhMCImage.setImageBitmap(getBitmapFromAssets(QuynhMCArray[x] + ".png"));
			//this.QuynhMCImage.setImageBitmap(getBitmapFromAssets("quynhmc.png"));
		}
		catch (Exception e) {
			String a =e.toString();
		}
		if (this.word1.booleanValue()) {
			//this.tv_firstword.setText(this.cauhientai.firstword + "");
		} else {
			//this.tv_firstword.setText("");
		}

		GlobalVar.skipQuestionList.replace(","+ "\"" + cauhientai._id + "\"'","");
		TaoDapAnNgauNhien();
	}

	public Bitmap getBitmapFromAssets(String fileName) {
		InputStream istr = null;
		try {
			istr = getAssets().open("Pics/" + fileName);
		} catch (FileNotFoundException e) {

		} catch (Exception e2) {
		}
		return BitmapFactory.decodeStream(istr);
	}
	public void XyLyKetQua() {
		int k;
		String tempDataBase = "";
		for (String str : this.cauhientai.kitu.split("_")) {
			tempDataBase = tempDataBase + str;
		}
		String temp = "";
		for (k = 0; k < this.arrTV.size(); k++) {
			temp = temp + ((TextView) this.arrTV.get(k)).getText();
		}
		int l;
		if (temp.toLowerCase().trim().equalsIgnoreCase(tempDataBase.toLowerCase())) {
			Load_BitCoinTuBoNho();
			this.index++;
			GlobalVar.finished_question_list += (",\"" + this.cauhientai._id + "\""); // ("0001","0002"...); // Add To Finish List
			//GlobalVar.skipQuestionList.replace(this.cauhientai._id+"","0000"); //Remove If Question In Skip List
			this.score += 10;
			Save_BitCoinXuongBoNho();
			Save_CauHoiXuongBoNho();
			///Save_TimeXuongBoNho();

			SharedPreferences.Editor edit = getSharedPreferences("MYDATAREF", 0).edit();
//			edit.putString("KEYSUGGEST_CH", "");
//			edit.putBoolean("KEY_WORD1", false);
//			edit.putBoolean("KEY_WORD2", false);
//			edit.putString("KEY_ANWSER", this.cauhientai.dapan );

			GlobalVar.laichu = this.cauhientai.laichu;
			GlobalVar.dapan = this.cauhientai.dapan;
			GlobalVar.interAdsIndex++;
			Quiz_new.this.startActivity(new Intent(Quiz_new.this.getApplicationContext(), KetQuaDungActivity.class));
			if (GlobalVar.interAdsIndex % 5 == 0 && Quiz_new.this.interstitial.isLoaded()) {
				Quiz_new.this.interstitial.show();
			}
			//openToast(this.traloisaiNN[this.min + ((int) (Math.random() * ((double) ((this.maxi - this.min) + 1))))], this.Hinh_Troll[this.min + ((int) (Math.random() * ((double) ((this.maxi - this.min) + 1))))]);

		}
	}
	public void Save_CauHoiXuongBoNho() {
		SharedPreferences.Editor editor = getSharedPreferences("MYDATAREF", 0).edit();
		try {
			editor.putString("KEY_LINKDOWMLOAD_XYZ_Q", MCrypt.encrypt(this.key_AES, String.valueOf(this.index)));
			editor.putString("FINISHED_QUESTION", MCrypt.encrypt(this.key_AES, String.valueOf(GlobalVar.finished_question_list)));
			editor.putString("SKIP_QUESTION", MCrypt.encrypt(this.key_AES, String.valueOf(GlobalVar.skipQuestionList)));
		} catch (Exception e) {
		}
		//editor.putInt("KEY_CAUHOI", this.index);
		editor.commit();
	}
	private void openToast(String text, int image) {
		try {
			View layout = getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
			Toast toast = new Toast(this);
			ImageView iv_custum_toast = (ImageView) layout.findViewById(R.id.iv_custum_toast);
			((TextView) layout.findViewById(R.id.tv_custum_toast)).setText("" + text);
			iv_custum_toast.setImageResource(image);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setGravity(16, 10, 10);
			toast.setView(layout);
			toast.show();
		} catch (Exception e) {
		}
	}
	public void Load_CauHoiTuBoNho() {
		try {
			SharedPreferences pref = getSharedPreferences("MYDATAREF", 0);
			//String index_default = MCrypt.encrypt(this.key_AES, AppEventsConstants.EVENT_PARAM_VALUE_NO);
			try {
				//this.index = Integer.parseInt(MCrypt.decrypt(this.key_AES, pref.getString("KEY_LINKDOWMLOAD_XYZ_Q", index_default)).trim());
				//this.index = 0;
				GlobalVar.finished_question_list =MCrypt.decrypt(this.key_AES, pref.getString("FINISHED_QUESTION", "0000"));
				GlobalVar.skipQuestionList = MCrypt.decrypt(this.key_AES, pref.getString("SKIP_QUESTION", "0000"));
			} catch (Exception e) {
				//this.index = Integer.parseInt(MCrypt.decrypt(this.key_AES, index_default).trim());
				GlobalVar.finished_question_list ="\"0000\"";
				GlobalVar.skipQuestionList = "\"0000\"";
			}
		} catch (Exception e2) {
		}
	}
	public void Load_BitCoinTuBoNho() {
		try {
			SharedPreferences pref = getSharedPreferences("MYDATAREF", 0);
			String score_default = MCrypt.encrypt(this.key_AES, "100");
			try {
				this.score = Integer.parseInt(MCrypt.decrypt(this.key_AES, pref.getString("BIT_SCORE", score_default)).trim());
			} catch (Exception e) {
				this.score = Integer.parseInt(MCrypt.decrypt(this.key_AES, score_default).trim());
			}
		} catch (Exception e2) {
		}
	}

	public void Save_BitCoinXuongBoNho() {
		SharedPreferences.Editor editor = getSharedPreferences("MYDATAREF", 0).edit();
		try {
			editor.putString("BIT_SCORE", MCrypt.encrypt(this.key_AES, String.valueOf(this.score)));
		} catch (Exception e) {
		}
		//editor.putInt("BIT_SCORE", this.score);
		editor.commit();
	}

	public void KTApp() {
		Load_CauHoiTuBoNho();
//		if (this.index != 0 && this.index % 6 == 0 && !this.ktapp.booleanValue()) {
//			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, 1);
//			alertDialogBuilder.setTitle("Đánh Giá Game!");
//			alertDialogBuilder.setIcon(R.drawable.ic_appgame);
//			alertDialogBuilder.setMessage("Nếu thấy game hay đừng quên đánh giá game để game ngày một phát triển nà bạn. Thanks bạn rất nhiều nà, I love you chịt chịt!").setPositiveButton("Có! Game hay", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {
//					dialog.cancel();
//					Quiz_new.this.openToast("Thanks bạn rất nhiều nà!", R.drawable.e_3);
//					SharedPreferences.Editor edit = Quiz_new.this.getSharedPreferences("MYDATAREF", 0).edit();
//					edit.putBoolean("KEY_KTAPP", true);
//					edit.commit();
//					Quiz_new.this.launchMarket();
//				}
//			}).setNegativeButton("Để sau đê", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {
//					dialog.cancel();
//					Quiz_new.this.openToast("Sau thế nà bạn! game hok được hay à bạn!", R.drawable.e_8);
//				}
//			});
//			alertDialogBuilder.create().show();
//		}
	}
	public void LoadQCBanner() {
//		this.adview_2pics1word = (AdView) findViewById(R.id.adView_2picstoword);
//		this.adview_2pics1word.loadAd(new AdRequest.Builder()
//				//.addTestDevice("9038137B1975D034E005EEE96541909F")
//				//.addTestDevice("5EAF8DE05E8974A7D5C369BD0A00C22B")
//				.build());
	}
    public void LoadQCFullScreen() {
        this.interstitial = new InterstitialAd(this);
        //unknow this.interstitial.setAdUnitId("ca-app-pub-4345988626634460/5985115542");
		this.interstitial.setAdUnitId("ca-app-pub-8204407936442788/4768896864"); //Android Prd Inter Paper Hero
        this.interstitial.loadAd(new AdRequest.Builder()
				.addTestDevice("9038137B1975D034E005EEE96541909F")
				.addTestDevice("5EAF8DE05E8974A7D5C369BD0A00C22B")
				.build());

}
	protected void onResume() {
		super.onResume();
		Load_BitCoinTuBoNho();
		this.tv_score.setText(this.score + "");
		if (this.adBanner != null) {
			this.adBanner.resume();
		}
		this.issound = Boolean.valueOf(getSharedPreferences("MYDATAREF", 0).getBoolean("KEY_ISSOUND", true));
	}
	public void Back_Game() {
		AlertDialog.Builder alertDialogBuildertb1 = new AlertDialog.Builder(this, 1);
		alertDialogBuildertb1.setTitle("Thoát Game");
//		alertDialogBuildertb1.setIcon(R.drawable.ic_appgame);
//		alertDialogBuildertb1.setMessage("Bạn có muốn thoát game không?").setPositiveButton("THOÁT", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				dialog.cancel();
				Quiz_new.this.startActivity(new Intent(Quiz_new.this, MainActivity.class));
				if (Quiz_new.this.slopen != 0 && Quiz_new.this.slopen % 4 == 0 && Quiz_new.this.interstitial.isLoaded()) {
					Quiz_new.this.interstitial.show();
				}
				Quiz_new.this.slopen++;
				SharedPreferences.Editor editor = Quiz_new.this.getSharedPreferences("MYDATAREF", 0).edit();
				editor.putInt("KEY_OPEN", Quiz_new.this.slopen);
				editor.commit();
				Quiz_new.this.finish();
//			}
//		}).setNegativeButton("CHƠI TIẾP", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				dialog.cancel();
//			}
//		});
//		alertDialogBuildertb1.create().show();
	}

	public void onBackPressed() {
		Back_Game();
	}

	protected void onPause() {
		super.onPause();
		if (this.adBanner != null) {
			this.adBanner.pause();
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		try {
			if (this.soundclick != null) {
				this.soundclick.release();
				this.soundclick = null;
			}
			if (this.soundwin != null) {
				this.soundwin.release();
				this.soundwin = null;
			}
			if (this.soundloss != null) {
				this.soundloss.release();
				this.soundloss = null;
			}
		} catch (Exception e) {
		}
		if (this.adBanner != null) {
			this.adBanner.destroy();
		}
	}
	public void ChiaSeCauHoi() {
		if (this.issound.booleanValue() && this.soundclick != null) {
			this.soundclick.start();
		}
		if (this.isSuggest.booleanValue()) {
			//ClearAnimHint();
		}
		this.isSuggest = Boolean.valueOf(false);
		CheckQuyen();
	}

	public Bitmap takeScreenshot() {
		View rootView = getWindow().getDecorView().getRootView();
		rootView.setDrawingCacheEnabled(false);
		rootView.setDrawingCacheEnabled(true);
		return rootView.getDrawingCache();
	}

	public void ShareImage() {
		///Toast.makeText(getApplicationContext(), "Quyết Định Sáng Suốt! Vừa Hỏi Bạn Bè Vừa Giới Thiệu Cho Bạn Cùng Chơi", 0).show();
		openToast("Quyết Định Sáng Suốt! Vừa Hỏi Bạn Bè Vừa Giới Thiệu Cho Bạn Cùng Chơi", R.drawable.e_6);
		try {
			Uri imageuri;
			File file = saveBitmap(takeScreenshot());
			if (Build.VERSION.SDK_INT >= 24) {
				imageuri = FileProvider.getUriForFile(this, "noilai.quiz", file);
			} else {
				imageuri = Uri.fromFile(file);
			}
			Intent intent = new Intent("android.intent.action.SEND");
			intent.putExtra("android.intent.extra.SUBJECT", "Share Your Friends!");
			intent.putExtra("android.intent.extra.TITLE", "Help Help!");
			intent.putExtra("android.intent.extra.STREAM", imageuri);
			intent.setType("image/*");
			startActivity(intent);
		}
		catch (Exception e) {
			return;
		}
	}

	public File saveBitmap(Bitmap bitmap) {
		if (Environment.getExternalStorageState().equals("mounted")) {
			File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PicsToWord");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			this.imagepath = new File(dir.getPath() + "/haihinhmotchu.jpg");
			try {
				FileOutputStream fos = new FileOutputStream(this.imagepath);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e2) {
			}
		}
		return this.imagepath;
	}

	public void CheckQuyen() {
		boolean hasPermission;
		if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
			hasPermission = true;
		} else {
			hasPermission = false;
		}
		if (hasPermission) {
			ShareImage();
			return;
		}
		ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 112);
	}

	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 112:
				if (grantResults.length > 0 && grantResults[0] == 0) {
					onResume();
					ShareImage();
					return;
				}
				return;
			default:
				return;
		}
	}
	public void Skip() {
		Load_BitCoinTuBoNho();
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, 1);
		alertDialogBuilder.setTitle("Tạm Bỏ Qua");
        alertDialogBuilder.setIcon(R.drawable.e_6);

//		Dialog dialog;
//        dialog=new Dialog(this);
//        //SET TITLE
//        dialog.setTitle("Player");
//        dialog.setContentView(R.layout.custom_dialog);
		//alertDialogBuilder.setIcon(R.drawable.ic_appgame);
		alertDialogBuilder.setMessage("Dùng 10 Bitcon Để Tạm Bỏ Qua! Sau Khi Trả Lời Hết Sẽ Quay Lại!!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				 if (Quiz_new.this.score > 0 ) {
					//Quiz_new.this.index++;
					 GlobalVar.skipQuestionList += (",\""+ Quiz_new.this.cauhientai._id  +"\""); // ("0001","0002"...);
					Quiz_new.this.score -= 10 ;
					Quiz_new.this.Save_BitCoinXuongBoNho();
					Quiz_new.this.Save_CauHoiXuongBoNho();
					Quiz_new.this.recreate();
				} else {
					dialog.cancel();
					AlertDialog.Builder alertDialogBuildertb2 = new AlertDialog.Builder(Quiz_new.this, 3);
					alertDialogBuildertb2.setTitle("BitCoin");

					alertDialogBuildertb2.setIcon(R.drawable.e_5);
					alertDialogBuildertb2.setMessage("Thiếu Bitcoin Oy, Xem Video Thôi!").setPositiveButton("OK!", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
							mRewardedVideoAd.show();
							//Quiz_new.this.startActivity(new Intent(Quiz_new.this.getApplicationContext(), KiemBitCoinActivity.class));
						}
					});
					alertDialogBuildertb2.create().show();
				}
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		alertDialogBuilder.create().show();

	}
	@Override
	public void onRewarded(RewardItem reward) {
		//Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +
		//		reward.getAmount(), Toast.LENGTH_SHORT).show();
		// Reward the user.
		//this.score += 100;
		///this.Save_BitCoinXuongBoNho();
		///this.Load_BitCoinTuBoNho();
		//this.Save_CauHoiXuongBoNho();
//        mRewardedVideoAd.loadAd(
//                "ca-app-pub-4345988626634460/7809402917", new AdRequest.Builder() //tdp
////                "ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder() //google test
//                        .addTestDevice("9038137B1975D034E005EEE96541909F")
//                        .addTestDevice("5EAF8DE05E8974A7D5C369BD0A00C22B")
//                        .build());
		///this.btn_watch_video.setVisibility(View.INVISIBLE) ;
		//this.loading.setVisibility(View.VISIBLE) ;
	}

	@Override
	public void onRewardedVideoAdLeftApplication() {
		//Toast.makeText(this, "onRewardedVideoAdLeftApplication",
		//		Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRewardedVideoAdClosed() {
		//play_flag =false;
		//Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
		//this.btn_watch_video.setVisibility(View.INVISIBLE) ;
		//this.loading.setVisibility(View.VISIBLE) ;
		mRewardedVideoAd.loadAd(
//				"ca-app-pub-4345988626634460/7809402917", new AdRequest.Builder() //tdp
//                "ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder() //google test
				"ca-app-pub-8204407936442788/7465721700", new AdRequest.Builder() //Video Android Paper Hero Prd
						.addTestDevice("9038137B1975D034E005EEE96541909F")
						.addTestDevice("5EAF8DE05E8974A7D5C369BD0A00C22B")
						.build());
	}

	@Override
	public void onRewardedVideoAdFailedToLoad(int errorCode) {
		//Toast.makeText(this, "onRewardedVideoAdFailedToLoad" + errorCode, Toast.LENGTH_SHORT).show();
//		this.btn_watch_video.setVisibility(View.INVISIBLE) ;
//		this.loading.setVisibility(View.VISIBLE) ;
//		try { Thread.sleep(1000); }
//		catch (InterruptedException ex) { android.util.Log.d("YourApplicationName", ex.toString()); }
//
//		irdf(tryTime<2) {
//			tryTime ++;
//			mRewardedVideoAd.destroy(this.getApplicationContext());
//			mRewardedVideoAd.loadAd(
//					//"ca-app-pub-4345988626634460/7809402917", new AdRequest.Builder() //tdp
//					//"ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder() //google test
//					"ca-app-pub-8204407936442788/7465721700", new AdRequest.Builder() //Video Android Paper Hero P
//
//							.addTestDevice("9038137B1975D034E005EEE96541909F")
//							.addTestDevice("5EAF8DE05E8974A7D5C369BD0A00C22B")
//							.build());
//		}
//		else
//		{
//			tryTime = 0;
//			AlertDialog.Builder alertDialogBuildertb1 = new AlertDialog.Builder(this, 1);
//
//			alertDialogBuildertb1.setTitle("Thông Báo");
//			alertDialogBuildertb1.setIcon(R.drawable.e_6);
//			///alertDialogBuildertb1.setIcon(R.drawable.ic_appgame);
//			alertDialogBuildertb1.setMessage("Có Lỗi Trong Quá Trình Tải Quảng Cáo ").setPositiveButton("Thử Lại", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {
//					dialog.cancel();
//					KiemBitCoinActivity.this.recreate();
//
//				}
//			}).setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {
//					dialog.cancel();
//					mRewardedVideoAd.destroy(KiemBitCoinActivity.this.getApplicationContext());
//					KiemBitCoinActivity.this.startActivity(new Intent(this.getApplicationContext(), MainActivity.class));
//					KiemBitCoinActivity.this.finish();
//				}
//			});
//			AlertDialog alertDialog = alertDialogBuildertb1.create();
//			if (!isFinishing()) {
//				alertDialog.show();
//			}
//		}
	}

	@Override
	public void onRewardedVideoAdLoaded() {
		//Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onRewardedVideoAdOpened() {
		//Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRewardedVideoStarted() {
		//Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
	}
}

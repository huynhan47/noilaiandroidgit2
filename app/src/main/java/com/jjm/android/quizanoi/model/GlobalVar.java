package com.jjm.android.quizanoi.model;

import com.jjm.android.quizanoi.MyApplication;

public class GlobalVar {
    public static int allQuestion = 1;
    public static String skipQuestionList= "\"0000\"";
    public static QuanlyCauHoi db = new QuanlyCauHoi(MyApplication.getAppContext());
    public static int DBVersion = 5;
    public static String finished_question_list="\"0000\"";
    public static String key_AES = "anoistudio.duoihinhlaichu";
    public static String dapan ="";
    public static String laichu ="";
    public static int interAdsIndex = 0;

}

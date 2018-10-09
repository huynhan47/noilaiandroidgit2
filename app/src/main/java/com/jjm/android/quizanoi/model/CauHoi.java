package com.jjm.android.quizanoi.model;

public class CauHoi {
    public String _id;
    public String firstword;
    public String kitu;
    public String secondword;
    public int soluongkitu;
    public String dapan ;
    public String audioIndex;
    public String laichu;
    public CauHoi(String _id, String kitu, String firstword, String secondword, String dapan, int soluongkitu, String laichu ) {
        this._id = _id;
        this.kitu = kitu;
        this.firstword = firstword;
        this.secondword = secondword;
        this.dapan  = dapan ;
        this.soluongkitu = soluongkitu;
        this.audioIndex ="";
        this.laichu = laichu;
    }
    public CauHoi()
    {

    }
    public String getAudio() {
        return audioIndex;
    }
}

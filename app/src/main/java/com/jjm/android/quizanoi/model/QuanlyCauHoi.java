package com.jjm.android.quizanoi.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.jjm.android.quizanoi.MyApplication;
import com.jjm.android.quizanoi.model.DataSource;
import com.jjm.android.quizanoi.util.MCrypt;

public class QuanlyCauHoi extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    //private static String DB_NAME = "db_tdp.sqlite";
    private static String DB_NAME = "laichu1.db";
    //private static String DB_PATH = "/data/data/com.jjm.android.quizanoi/databases/";
    private static String DB_PATH = "";
    private static final String TABLE_NAME = "laichu";
    int curentdatabase ;
    Context myContext;
    private SQLiteDatabase myDataBase;

    public QuanlyCauHoi(Context context) {
        super(context, DB_NAME, null,3);
        this.myContext = context;
        DB_PATH = myContext.getApplicationInfo().dataDir+"/databases/";

        try {
            SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("MYDATAREF", 0);

            try {
                this.curentdatabase =  pref.getInt("DBVersion", 1);
            } catch (Exception e) {
                this.curentdatabase = 1;
            }
        } catch (Exception e2) {
        }
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDataBase() throws SQLiteException {
        this.myDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, 1);
    }

    public synchronized void close() {
        if (this.myDataBase != null) {
            this.myDataBase.close();
        }
        super.close();
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, 1);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        if (checkDB != null) {
            return true;
        }
        return false;
    }

    public void createDataBase(int version) throws IOException {
        if (!checkDataBase()) {
            getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        } else if (version > this.curentdatabase) {
            getReadableDatabase();
            try {
                copyDataBase();

            } catch (IOException e2) {
                throw new Error("Error copying database lan thu n");
            }
        }
        SharedPreferences.Editor editor = MyApplication.getAppContext().getSharedPreferences("MYDATAREF", 0).edit();
        try {
            editor.putInt("DBVersion",version);
            editor.commit();
        } catch (Exception e) {
        }
        editor.commit();
    }

    private void copyDataBase() throws IOException {
        try {
            InputStream myInput = this.myContext.getAssets().open(DB_NAME);
            OutputStream myOutput = new FileOutputStream(DB_PATH + DB_NAME);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = myInput.read(buffer);
                if (length > 0) {
                    myOutput.write(buffer, 0, length);
                } else {
                    myOutput.flush();
                    myOutput.close();
                    myInput.close();
                    return;
                }
            }
        } catch (Exception e) {
        }
    }
public int QuestionCount ()
{
    String sqlCount = "select * from laichu";
    Cursor contro = getReadableDatabase().rawQuery(sqlCount.toString(), null);
    return contro.getCount();

}
    public List<CauHoi> layNCauHoi(String finish_question_list, String skip_question_list) {
        List<CauHoi> ds_cauhoi = new ArrayList();
        String[] tableColumns = new String[] {
                "status"
        };
        String whereClause = "status = ?";
        String whereClause2 = "id not in = ?";
        String[] whereArgs = new String[] {
                "F"
        };
        ///DataSource.finish_question_list = "\"0007\",\"0009\"";
        StringBuilder sb_query = new StringBuilder();
        sb_query.append("select * from laichu where id not in (");
        sb_query.append(finish_question_list);
        sb_query.append(")");
        sb_query.append("union all");
        sb_query.append("select * from laichu where id  in (");
        sb_query.append(finish_question_list);
        sb_query.append(")");

        String sqlSkipList = "select * from laichu where id  in ( " + skip_question_list + " )";

        StringBuilder sb_queryQuestion = new StringBuilder();
        sb_queryQuestion.append("select * from laichu where id not in ( " + finish_question_list + ","+ skip_question_list + " )");
        //sb_queryQuestion.append(" union all ")  ;
        //sb_queryQuestion.append("select * from laichu where id not in ( " + skip_question_list + " )");
        try {
            //Cursor contro = getReadableDatabase().query(TABLE_NAME,null, whereClause, whereArgs, null, null, null, null);
            //Cursor contro = getReadableDatabase().query(TABLE_NAME,null, null, null, null, null, null, null);

            Cursor contro = getReadableDatabase().rawQuery(sb_queryQuestion.toString(), null);
            Log.v("DB","Finish List and Ski[  Ne: "+sb_queryQuestion.toString());
            if(contro.getCount() == 0) {
                contro = getReadableDatabase().rawQuery(sqlSkipList, null);
                Log.v("DB","SkipList Ne: "+sqlSkipList);
            }
            contro.moveToFirst();

            do {
                CauHoi x = new CauHoi();
                x._id = contro.getString(0);
                x.kitu = contro.getString(2);
                //x.firstword = contro.getString(2);
                //x.secondword = contro.getString(3);
                x.dapan = contro.getString(6);
                x.laichu = contro.getString(5);
                x.soluongkitu = Integer.parseInt(contro.getString(3));
                ds_cauhoi.add(x);
                GlobalVar.skipQuestionList = GlobalVar.skipQuestionList.replace(",\""+ x._id +"\"","");

                SharedPreferences.Editor editor = MyApplication.getAppContext().getSharedPreferences("MYDATAREF", 0).edit();
                editor.putString("SKIP_QUESTION", MCrypt.encrypt(GlobalVar.key_AES, String.valueOf( GlobalVar.skipQuestionList )));
                editor.commit();

            } while (contro.moveToNext());
        }
        catch (Exception e) {
            Log.e("InitializeTask", "error initializing app", e);
        }
        return ds_cauhoi;
    }
}

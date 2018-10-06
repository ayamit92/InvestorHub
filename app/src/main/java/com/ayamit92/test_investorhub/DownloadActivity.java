package com.ayamit92.test_investorhub;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DownloadActivity extends AppCompatActivity {
    //    int mutualFundCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        //Intent intent=getIntent();
        //mutualFundCount=intent.getIntExtra("mutualFundCount",-1);
        //Log.i("count",String.valueOf(mutualFundCount));

        SQLiteDatabase myDatabase=this.openOrCreateDatabase("Mutual_Fund",MODE_PRIVATE,null);

        ListView listView=(ListView) findViewById(R.id.downloadView);
        ArrayList<String> mf=new ArrayList<String>();
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS daily_values (mutual_fund VARCHAR,nav FLOAT(6))");
        Cursor c=myDatabase.rawQuery("SELECT * from daily_values",null);

        int mutualFundIndex=c.getColumnIndex("mutual_fund");
        int navIndex=c.getColumnIndex("nav");
        c.moveToFirst();
        for (int i=0;i<c.getCount();i++)
        {
//       if(c.getString(1) == null)
//       {Log.i("mutualfund",String.valueOf(i));}
            mf.add(c.getString(mutualFundIndex)+" "+c.getFloat(navIndex));
            //c.getDouble(navIndex)...this would have worked as well
            c.moveToNext();
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mf);
        listView.setAdapter(arrayAdapter);
    }
}

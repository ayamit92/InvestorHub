package com.ayamit92.test_investorhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class IndividualMutualFundActivity extends AppCompatActivity {
    int mutualFundId;
    String mutualFundName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_mutual_fund);
        ListView listView=(ListView) findViewById(R.id.individualMutualFundListView);

        Intent intent=getIntent();
        mutualFundId=intent.getIntExtra("mutualFundId",-1);

        if (mutualFundId!=-1) {
            mutualFundName = PortfolioActivity.mutualFundList.get(mutualFundId);
        }
        //Log.i("yo",mutualFundName);

        SQLiteDatabase myDatabase=this.openOrCreateDatabase("Mutual_Fund",MODE_PRIVATE,null);
        //myDatabase.execSQL("CREATE TABLE IF NOT EXISTS transactions (mutual_fund VARCHAR,amount FLOAT(6),date VARCHAR)");
        // String record_new=mutual_fund+","+amount+","+date;
        final ArrayList<String> mutualFundHistory=new ArrayList<String>();
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS transactions (mutual_fund VARCHAR,amount FLOAT(6),nav FLOAT(6),units FLOAT(6),date VARCHAR)");
        Cursor c=myDatabase.rawQuery("SELECT * from transactions where mutual_fund='"+mutualFundName+"'",null);
        int mutualFundIndex=c.getColumnIndex("mutual_fund");
        int amountIndex=c.getColumnIndex("amount");
        int navIndex=c.getColumnIndex("nav");
        int unitsIndex=c.getColumnIndex("units");
        int dateIndex=c.getColumnIndex("date");

       // do
        int count=c.getCount();
        c.moveToFirst();
        for (int i=0;i<count;i++)
        {
            //mutualFundHistory.add(c.getString(mutualFundIndex)+","+c.getFloat(amountIndex)+","+c.getString(dateIndex));
            if (i==count-1) {
                mutualFundHistory.add(c.getString(mutualFundIndex));
                mutualFundHistory.add("Amount:"+String.valueOf(c.getFloat(amountIndex)));
                mutualFundHistory.add("Nav:"+String.valueOf(c.getFloat(navIndex)));
                mutualFundHistory.add("Units:"+String.valueOf(c.getFloat(unitsIndex)));
                mutualFundHistory.add(c.getString(dateIndex));
            }
            else
            {
                mutualFundHistory.add(c.getString(mutualFundIndex));
                mutualFundHistory.add("Amount:"+String.valueOf(c.getFloat(amountIndex)));
                mutualFundHistory.add("Nav:"+String.valueOf(c.getFloat(navIndex)));
                mutualFundHistory.add("Units:"+String.valueOf(c.getFloat(unitsIndex)));
                mutualFundHistory.add(c.getString(dateIndex));
                mutualFundHistory.add("");
            }
            c.moveToNext();
            }
        //while (!c.isAfterLast());
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mutualFundHistory);
        listView.setAdapter(arrayAdapter);
    }
}
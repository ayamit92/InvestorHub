package com.ayamit92.test_investorhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PortfolioActivity extends AppCompatActivity {
    static ArrayList<String> mutualFundList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        SQLiteDatabase myDatabase=this.openOrCreateDatabase("Mutual_Fund",MODE_PRIVATE,null);

        ListView listView=(ListView) findViewById(R.id.portfolioList);

        mutualFundList.clear();
        //above step added as if we move from main screen to portfolio and then come back and then again go to portfolio, entries are repeated. This is
        // because since our list is static it retains the data and that data is again added to list. So, we need to clear it before every run for portfolio.
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS transactions (mutual_fund VARCHAR,amount FLOAT(6),nav FLOAT(6),units FLOAT(6),date VARCHAR)");
        Cursor c=myDatabase.rawQuery("SELECT distinct mutual_fund from transactions",null);
        int mutualFundIndex=c.getColumnIndex("mutual_fund");
        c.moveToFirst();

        for (int i=0;i<c.getCount();i++)
        {
            //Log.i("mutual fund name",c.getString(mutualFundIndex));
            mutualFundList.add(c.getString(mutualFundIndex));
            c.moveToNext();
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mutualFundList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent (getApplicationContext(),IndividualMutualFundActivity.class);
                intent.putExtra("mutualFundId",i);
                startActivity(intent);

            }
        });
    }
}
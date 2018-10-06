package com.ayamit92.test_investorhub;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        SQLiteDatabase myDatabase=this.openOrCreateDatabase("Mutual_Fund",MODE_PRIVATE,null);
        ListView listView=(ListView) findViewById(R.id.historyView);
        final ArrayList<String> history=new ArrayList<String>();
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS history (record VARCHAR)");
        Cursor c=myDatabase.rawQuery("SELECT * from history",null);
        int recordIndex=c.getColumnIndex("record");
        c.moveToFirst();
        int count=c.getCount();

        for (int i=0;i<count;i++)
        {
            Log.i("record",c.getString(recordIndex));
            history.add(c.getString(recordIndex));
            c.moveToNext();
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,history);
        listView.setAdapter(arrayAdapter);
    }
}

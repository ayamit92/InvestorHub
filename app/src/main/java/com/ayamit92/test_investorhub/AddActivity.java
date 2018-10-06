package com.ayamit92.test_investorhub;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    Button submitButton;
    public void submit (View view)
    {
        try{
            SQLiteDatabase myDatabase=this.openOrCreateDatabase("Mutual_Fund",MODE_PRIVATE,null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS transactions (mutual_fund VARCHAR,amount FLOAT(6),nav FLOAT(6),units FLOAT(6),date VARCHAR)");
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS history (record VARCHAR)");
            //float precision
            //add primary key to this table as well and use that to delete particular record from list and update in this table

            EditText textView=(EditText) findViewById(R.id.editText);
            String mutual_fund = textView.getText().toString();
            EditText textView2=(EditText) findViewById(R.id.editText2);
            Float amount = Float.parseFloat(textView2.getText().toString());
            EditText textView3=(EditText) findViewById(R.id.editText3);
            String date = textView3.getText().toString();
            EditText textView4=(EditText) findViewById(R.id.editText4);
            Float nav = Float.parseFloat(textView4.getText().toString());
            Float units=amount/nav;

            Log.i("Info",mutual_fund+" "+amount+" "+nav+" "+units+" "+date);
            String record_new=mutual_fund+","+amount+","+nav+","+units+","+date;
            myDatabase.execSQL("INSERT INTO transactions (mutual_fund,amount,nav,units,date) VALUES ('"+mutual_fund+"',"+amount+","+nav+","+units+",'"+date+"')");
            myDatabase.execSQL("INSERT INTO history (record) VALUES ('"+record_new+"')");
//            Cursor c=myDatabase.rawQuery("SELECT * from transactions",null);
//            int mutualfundIndex=c.getColumnIndex("mutual_fund");
//            int amountIndex=c.getColumnIndex("amount");
//            int dateIndex=c.getColumnIndex("date");
//            c.moveToFirst();
//            while (c!=null)
//            {
//                Log.i("mutual_fund",c.getString(mutualfundIndex));
//                Log.i("amount",Float.toString(c.getInt(amountIndex)));
//                Log.i("date",c.getString(dateIndex));
//                c.moveToNext();
//            }
            Intent intent=new Intent (getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),"Submitted successfully!", Toast.LENGTH_LONG).show();
        Intent intent=new Intent (getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        submitButton = (Button) findViewById(R.id.submit);
    }
}

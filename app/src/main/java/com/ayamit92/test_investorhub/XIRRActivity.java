package com.ayamit92.test_investorhub;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class XIRRActivity extends AppCompatActivity {
    public static final Double tol = 0.001;

    public Double dateDiff(Date d1, Date d2){
        Double day = 24*60*60*1000*1.0;
        return (d1.getTime() - d2.getTime())/day;
    }
    public  Double f_xirr(Double p, Date dt, Date dt0, Double x) {
        return p * Math.pow((1.0 + x), (dateDiff(dt0,dt) / 365.0));
    }

    public  Double df_xirr(Double p, Date dt, Date dt0, Double x) {
        return (1.0 / 365.0) * dateDiff(dt0,dt) * p * Math.pow((x + 1.0), ((dateDiff(dt0,dt) / 365.0) - 1.0));
    }

    public  Double total_f_xirr(ArrayList<Double> payments, ArrayList<Date> days, Double x) {
        Double resf = 0.0;

        for (int i = 0; i < payments.size(); i++) {
            resf = resf + f_xirr(payments.get(i), days.get(i), days.get(0), x);
        }

        return resf;
    }

    public  Double total_df_xirr(ArrayList<Double> payments, ArrayList<Date> days, Double x) {
        Double resf = 0.0;

        for (int i = 0; i < payments.size(); i++) {
            resf = resf + df_xirr(payments.get(i), days.get(i), days.get(0), x);
        }

        return resf;
    }

    public  Double Newtons_method(Double guess, ArrayList<Double> payments, ArrayList<Date> days) {
        Double x0 = guess;
        Double x1 = 0.0;
        Double err = 1e+100;

        while (err > tol) {
            x1 = x0 - total_f_xirr(payments, days, x0) / total_df_xirr(payments, days, x0);
            err = Math.abs(x1 - x0);
            x0 = x1;
        }

        return x0;
    }

    private  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public  Date strToDate(String str){
        try {
            return sdf.parse(str);
        } catch (ParseException ex) {
            return null;
        }
    }

    public Double today_nav(String actual_name)
    {
     Double value=0.0;
        SQLiteDatabase myDatabase=this.openOrCreateDatabase("Mutual_Fund",MODE_PRIVATE,null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS daily_values (mutual_fund VARCHAR,nav FLOAT(6))");
        Cursor c=myDatabase.rawQuery("SELECT * from daily_values where mutual_fund='"+actual_name+"'",null);

        int navIndex=c.getColumnIndex("nav");
        c.moveToFirst();
        for (int i=0;i<c.getCount();i++)
        {
            value=c.getDouble(navIndex);
            c.moveToNext();
        }


        return value;
    }

    public  String calculate_xirr(String mutual_fund_name, String actual_name) {

        SQLiteDatabase myDatabase=this.openOrCreateDatabase("Mutual_Fund",MODE_PRIVATE,null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS transactions (mutual_fund VARCHAR,amount FLOAT(6),nav FLOAT(6),units FLOAT(6),date VARCHAR)");
        Cursor c=myDatabase.rawQuery("SELECT * from transactions where mutual_fund='"+mutual_fund_name+"'",null);
        int amountIndex=c.getColumnIndex("amount");
        int dateIndex=c.getColumnIndex("date");
        int unitsIndex=c.getColumnIndex("units");
        ArrayList<Double> payments=new ArrayList<Double>();
        ArrayList<Date> days=new ArrayList<Date>();
        c.moveToFirst();

        Double units=0.0;
        for (int i=0;i<c.getCount();i++)
        {
            payments.add((c.getDouble(amountIndex)));
            Log.i("amount",c.getString(amountIndex));
            days.add(strToDate(c.getString(dateIndex)));
            Log.i("date",c.getString(dateIndex));
            units=units+c.getDouble(unitsIndex);
            Log.i("units",c.getString(unitsIndex));
            c.moveToNext();
        }


        Double today_nav=0.0;
        today_nav=today_nav(actual_name);

        Double today_value=14993.49;
        today_value=units*today_nav;
        today_value=-1.0*today_value;
        payments.add(today_value);

        String today_date="24/08/2017";
        days.add(strToDate(today_date));

        Double xir= Newtons_method(0.1, payments, days);
        xir=xir*100;
        String xi=String.format("%.2f",xir);
        xi=xi+"%";
        return xi;
    }


    TextView dsp;
    TextView idfc;
    TextView tata_india;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xirr);
        dsp=(TextView) findViewById(R.id.textView3);
        idfc=(TextView) findViewById(R.id.textView8);
        tata_india=(TextView) findViewById(R.id.textView10);

        String xir_dsp= calculate_xirr("DSP BlackRock","DSP BlackRock Tax Saver Fund - Direct Plan (G)");
        String xir_idfc= calculate_xirr("IDFC","IDFC Tax Advantage (ELSS) Fund - Direct Plan (G)");
        String xir_tata_india= calculate_xirr("Tata India","Tata India Tax Savings Fund - Direct Plan (G)");

        dsp.setText(xir_dsp);
        idfc.setText(xir_idfc);
        tata_india.setText(xir_tata_india);
    }
}

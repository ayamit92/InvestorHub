package com.ayamit92.test_investorhub;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    Button downloadButton;
    Button portfolioButton;
    public void download(View view)
    {
        DownloadTask task = new DownloadTask();
        String result = null;
        SQLiteDatabase myDatabase=this.openOrCreateDatabase("Mutual_Fund",MODE_PRIVATE,null);
        myDatabase.execSQL("DROP TABLE IF EXISTS daily_values");
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS daily_values (mutual_fund VARCHAR,nav FLOAT(6))");
        try {
            result = task.execute("http://www.moneycontrol.com/india/mutualfunds/mfinfo/19/04/latestnav/CEE_fc/1/").get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        int count=0;
        Pattern pBlock=Pattern.compile("<td align=\"left\"><p align=\"left\"><a href=(.*?)</span></td>");
        Matcher mBlock=pBlock.matcher(result);

        Pattern p=Pattern.compile("<td align=\"right\">(.*?)</td>");
        Pattern pp=Pattern.compile("li class=\"fundhd\">Scheme: (.*?)</li>");
        Matcher m;
        Matcher mm;
        String nav="";
        String mutual_fund="";
        String line;
        StringBuffer query = new StringBuffer();
        query.append("INSERT INTO daily_values (mutual_fund,nav) VALUES ");

        while(mBlock.find())
        {
            m=p.matcher(mBlock.group(1));
            mm=pp.matcher(mBlock.group(1));
            while(m.find())
            {
                nav=m.group(1);
            }
            while(mm.find())
            {
                mutual_fund=mm.group(1);
            }
            line="('"+mutual_fund+"',"+nav+")";
            Log.i("NAV+MF",line);
            if (count==0)
            {query.append(line);}
            else
            {query.append(","+line);}
            count++;
        }
        Log.i("Contents Of URL", result);

        String query_string=query.toString();

        Log.i("Contents Of URL", query_string);
        myDatabase.execSQL(query_string);
        //       myDatabase.execSQL("INSERT INTO daily_values (mutual_fund,nav) VALUES ('xyz',11.29)");
        Intent intent=new Intent (getApplicationContext(),DownloadActivity.class);
        //       intent.putExtra("mutualFundCount",count);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"Download Successful", Toast.LENGTH_LONG).show();
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();

//               Reading the webpage line by line make the retrieval much much faster than reading it by char by char !!
                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                StringBuffer result = new StringBuffer();

                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            }
            catch(Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }
    }

    public void add (View view)
    {
        Intent intent=new Intent (getApplicationContext(),AddActivity.class);
        startActivity(intent);
    }

    public void portfolio (View view)

    {
        Intent intent=new Intent (getApplicationContext(),PortfolioActivity.class);
        startActivity(intent);
    }

    public void history (View view)

    {
        Intent intent=new Intent (getApplicationContext(),HistoryActivity.class);
        startActivity(intent);
    }

    public void xirr (View view)

    {
        Intent intent=new Intent (getApplicationContext(),XIRRActivity.class);
        startActivity(intent);
    }

    public void formatDB (View view)

    {
        SQLiteDatabase myDatabase=this.openOrCreateDatabase("Mutual_Fund",MODE_PRIVATE,null);
        myDatabase.execSQL("DROP TABLE IF EXISTS daily_values");
        myDatabase.execSQL("DROP TABLE IF EXISTS transactions");
        myDatabase.execSQL("DROP TABLE IF EXISTS history");
        Toast.makeText(getApplicationContext(),"DB formatted!", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadButton = (Button) findViewById(R.id.button6);
        portfolioButton = (Button) findViewById(R.id.button4);
        //git commit
    }
}

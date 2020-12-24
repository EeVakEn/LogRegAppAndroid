package com.eevaken.logregapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

public class CourseActivity extends AppCompatActivity {
    StringBuilder str;
    TextView textView;
    TextView dateTV;
    Button btn;

    //private String[][] data;
    Button restart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
         str = new StringBuilder();
        textView = (TextView)findViewById(R.id.dataTV);
        dateTV = (TextView)findViewById(R.id.dateTime);
        Date date = new Date();

        dateTV.setText(String.valueOf(date));
        dateTV.setMovementMethod(new ScrollingMovementMethod());
        init();
        btn = findViewById(R.id.restartBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });



    }

    private void init(){



        new Thread(new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        }).start();




    }

    private void getWeb(){
        try {
            final Document doc = Jsoup.connect("https://www.banki.ru/products/currency/cb/").get();

            Elements table_rows =
                    doc
                    .getElementsByTag("tbody")
                    .get(0)
                    .children();
            //data = new String[rows.size()][5];

            Elements elems;
            for (Element row:table_rows) {
                elems = row.children();
                for (Element elem:elems){
                    str.append(elem.text()).append("\n");
                }
                str.append("\n");

            }
            textView.setText(str.toString());


//            data = new String[rows.size()][5];
//
//            Elements elems;
//
//            int i = 0;
//            for (Element row:rows) {
//                elems = row.children();
//                int j = 0;
//                for (Element elem:elems){
//                    data[i][j]=(elem.text());
//                    j++;
//                }
//                i++;
//            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.example.filmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.textView);
    }

    public void ButtonClick(View v) {
        class MyTask extends AsyncTask<Void, Void, Void> {

            String title;

            @Override
            protected Void doInBackground(Void... params) {

                Document doc = null;
                try {
                    //Считываем заглавную страницу http://harrix.org
                    doc = Jsoup.connect("https://www.kinonews.ru/news/").get();
                } catch (IOException e) {
                    //Если не получилось считать
                    e.printStackTrace();
                }

                //Если всё считалось, что вытаскиваем из считанного html документа заголовок
                if (doc!=null)
                    title = doc.html();
                else
                    title = "Ошибка";

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                textView.setText(title);
            }
        }
        MyTask mt = new MyTask();
        mt.execute();
    }
}
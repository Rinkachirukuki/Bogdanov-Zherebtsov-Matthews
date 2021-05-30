package com.example.filmapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class MainActivity extends AppCompatActivity {


    private Button button;
    private TextView textView;
    private ListView listView;

    String titles[] = new String[20];
    String descriptions[] = new String[20];
    String dates[] = new String[20];
    int page=1;

    class DisplayTask extends AsyncTask<Void, Void, Void> {

        List<String> s_titles = new ArrayList<String>();
        List<String> s_texts = new ArrayList<String>();
        List<String> s_dates = new ArrayList<String>();
        int parse_page;

        public DisplayTask() {
        }

        public DisplayTask(int parse_page) {
            this.parse_page = parse_page;
        }

        public List<String> getS_titles() {
            return s_titles;
        }

        @Override
        protected Void doInBackground(Void... params) {

            Document doc = null;
            String url="https://www.kinonews.ru/news" + ((parse_page>1) ? ("p"+parse_page) : "");
            try {

                doc = Jsoup.connect(url).get();

                Elements e_titles = doc.getElementsByClass("anons-title-new");
                Elements e_texts = doc.getElementsByClass("anons-text");
                Elements e_dates = doc.getElementsByClass("anons-date-new");

                for (int i = 0; i < e_titles.size(); i++) {

                    s_titles.add(e_titles.get(i).select("a").get(0).text());
                    s_texts.add(e_texts.get(i).text());
                    s_dates.add(e_dates.get(i).text());

                }

            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            s_titles.toArray(titles);
            s_texts.toArray(descriptions);
            s_dates.toArray(dates);

            listView.invalidateViews();

        }
    }
    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String itemTitle[];
        String itemDescription[];
        String itemDate[];

        MyAdapter(Context c, String title[], String description[], String date[]) {
            super(c, R.layout.list_item, R.id.item_title, title);
            this.context = c;
            this.itemTitle = title;
            this.itemDescription = description;
            this.itemDate = date;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View item = layoutInflater.inflate(R.layout.list_item, parent, false);

            TextView myTitle = item.findViewById(R.id.item_title);
            TextView myDescription = item.findViewById(R.id.item_description);
            TextView myDate = item.findViewById(R.id.item_date);

            myTitle.setText(itemTitle[position]);
            myDescription.setText(itemDescription[position]);
            myDate.setText(itemDate[position]);

            return item;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);
        button = (Button)findViewById(R.id.button);

        MyAdapter adapter = new MyAdapter(this, titles, descriptions, dates);

        listView.setAdapter(adapter);
        DisplayTask mt = new DisplayTask();
        mt.execute();


    }

    public void ButtonClick(View v) {


        DisplayTask mt = new DisplayTask();
        mt.execute();
    }

}



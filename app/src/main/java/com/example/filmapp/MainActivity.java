package com.example.filmapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ListAdapter adapter;

    ArrayList<Item> listItems=new ArrayList<>();

    boolean isScrolling = false;

    int currentItems, totalItems, scrollOutItems;

    int page=1;

    class DisplayTask extends AsyncTask<Void, Void, Void> {

        ArrayList<Item> items = new ArrayList<>();

        int parse_page = 1;

        String exception = "";

        public DisplayTask() {
        }

        public DisplayTask(int parse_page) {
            this.parse_page = parse_page;
        }

        @Override
        protected Void doInBackground(Void... params) {

            Document doc = null;
            String url = "https://www.kinonews.ru/news_p" + parse_page;
            try {
                doc = Jsoup.connect(url).get();

                Elements e_titles = doc.getElementsByClass("anons-title-new");
                Elements e_texts = doc.getElementsByClass("anons-text");
                Elements e_dates = doc.getElementsByClass("anons-date-new");
                Elements e_pictures = doc.getElementsByClass("newsimg");

                for (int i = 0; i < e_titles.size(); i++) {

                    Item item = new Item();

                    item.title = e_titles.get(i).select("a").get(0).text();
                    item.description = e_texts.get(i).text();
                    item.date = e_dates.get(i).text();
                    item.picture = DownloadImage(e_pictures.get(i).absUrl("src"));

                    items.add(item);
                }

            } catch (Exception e) {
                exception = e.getMessage();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (exception != ""){
                Toast toast = Toast.makeText(getApplicationContext(),
                        exception, Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            listItems.addAll(items);
            adapter.notifyDataSetChanged();
        }

        private InputStream OpenHttpConnection(String urlString) throws IOException {
            InputStream in = null;
            int response = -1;

            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");

            try {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }
            } catch (Exception ex) {
                throw new IOException("Error connecting");
            }
            return in;
        }

        private Bitmap DownloadImage(String URL) throws IOException {
            Bitmap bitmap = null;
            InputStream in = null;

            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();

            return bitmap;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull  RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems+scrollOutItems==totalItems)){
                    isScrolling=false;
                    page++;
                    fetch_data();
                }
            }
        });

        layoutManager=new LinearLayoutManager(this);
        adapter=new ListAdapter(this, listItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        DisplayTask task = new DisplayTask();

        task.execute();
    }

    public void ButtonClick(View v) {
        DisplayTask mt = new DisplayTask();
        mt.execute();

    }
    private void fetch_data() {
        DisplayTask mt = new DisplayTask(page);
        mt.execute();
    }
}



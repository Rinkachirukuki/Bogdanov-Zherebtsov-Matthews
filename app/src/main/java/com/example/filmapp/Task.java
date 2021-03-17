package com.example.filmapp;

import android.os.AsyncTask;

public class Task extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {

        //Тут пишем основной код

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        //Тут выводим итоговые данные
    }
}
package com.example.filmapp;

import android.graphics.Bitmap;

public class Item {
    public String title;
    public String description;
    public String date;
    public Bitmap picture;

    public Item() {
    }

    public Item(String titles, String descriptions, String dates, Bitmap picture) {
        this.title = titles;
        this.description = descriptions;
        this.date = dates;
        this.picture = picture;

    }
}

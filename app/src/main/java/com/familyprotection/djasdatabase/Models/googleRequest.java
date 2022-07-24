package com.familyprotection.djasdatabase.Models;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class googleRequest extends Thread {

    private ArrayList<Bitmap> imgRealList;
    private final String keyword;

    private final RequestConsumer requestConsumer;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    public googleRequest(@NonNull String keyword, @NonNull RequestConsumer requestConsumer){
        this.keyword = keyword;
        this.requestConsumer = requestConsumer;
    }

    @Override
    public void run() {
        String newKeyword = keyword.toLowerCase(Locale.ROOT);
        newKeyword = newKeyword.replace(' ','+');
        String url = "https://www.google.gr/search?bih=427&biw=1835&hl=el&gbv=1&tbm=isch&og=&ags=&q="+ newKeyword;
        try {
            Document document = Jsoup.connect(url).get();
            imgRealList = new ArrayList<>();
            Elements imgList = document.select("img");
            for (int i=1;i<imgList.size();i++) {
                if(i==8)
                    break;
                String imgSrc = imgList.get(i).absUrl("src");
                InputStream input = new java.net.URL(imgSrc).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                imgRealList.add(bitmap);
            }
            uiHandler.post(() -> requestConsumer.onRequestResult(imgRealList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

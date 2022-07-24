package com.familyprotection.djasdatabase.Models;

import android.graphics.Bitmap;

import androidx.annotation.MainThread;

import java.util.List;

@MainThread
public interface RequestConsumer {
    void onRequestResult(List<Bitmap> bitmaps);
}

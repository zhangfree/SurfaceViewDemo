package com.example.SurfaceViewDemo;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangji on 5/11/15.
 */
public class BowknotEffectEntity {

    static Map<Integer,Bitmap> bmpMap = new HashMap<Integer, Bitmap>();

    public int posX;
    public int posY;
    public int width;
    public int height;
    public float speed;
    public Bitmap bitmap;

    public BowknotEffectEntity(Bitmap bmp, int x, int y, int w, int h) {
        posX = x;
        posY = y;
        width = w;
        height = h;
        bitmap = Bitmap.createScaledBitmap(bmp, width, height, true);
    }

}

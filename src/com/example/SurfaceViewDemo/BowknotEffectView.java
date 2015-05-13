package com.example.SurfaceViewDemo;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangji on 5/11/15.
 */
public class BowknotEffectView extends SurfaceView implements SurfaceHolder.Callback {

    private Matrix mMatrix = new Matrix();

    private SurfaceHolder mHolder;

    private Thread mThread;


    private List<BowknotEffectEntity> entities = new ArrayList<BowknotEffectEntity>();

    public BowknotEffectView(Context context) {
        super(context);
        init();
    }

    public BowknotEffectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BowknotEffectView(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
        init();
    }

    private void init() {
        mHolder = this.getHolder();
        mHolder.addCallback(this);
    }

    private void initStarEffect() {
        entities.clear();

        Bitmap effectBmp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.effect_item);

        int effectBmpWidth = 40;
        int effectBmpHeight = 40;
        for (int i = 0;i < 32;i++) {
            double scale = Math.random();
            int entityWidth = (int)(scale * effectBmpWidth) + effectBmpWidth;
            int entityHeight = (int)(scale * effectBmpHeight) + effectBmpHeight;
            int entityPosX = (int)(Math.random() * (getWidth()-effectBmpWidth));
            int entityPosY = 0 - (effectBmpHeight + (int)(Math.random() * effectBmpHeight) * (i / 4));
            int entitySpeed = (int)(Math.random()*20 + 20);
            BowknotEffectEntity entity = new BowknotEffectEntity(effectBmp, entityPosX, entityPosY, entityWidth, entityHeight);
            entity.speed = entitySpeed;
            entities.add(entity);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        starEffect();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        endEffect();
    }



    public void starEffect() {
        initStarEffect();
        mThread = new Thread(new DrawThread());
        mThread.start();
    }

    public void endEffect() {
        if (mThread != null) {
            mThread.interrupt();
        }
    }

    class DrawThread implements Runnable {

        private int interval;

        @Override
        public void run() {
            Canvas canvas = null;
            Date date;
            while (true) {
                date = new Date();
                try {
                        canvas = mHolder.lockCanvas();
                        // 初始化画布
                        Paint paint = new Paint();
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawPaint(paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

                        for (int i = 0; i < entities.size(); ++i) {
                            BowknotEffectEntity cacheEntity = entities.get(i);
                            if (cacheEntity.posY > getHeight()){
                                cacheEntity.posY = -cacheEntity.height;
                            }else{
                                cacheEntity.posY += cacheEntity.speed;
                            }
                            mMatrix.setTranslate(-cacheEntity.width / 2, -cacheEntity.height / 2);
                            mMatrix.postTranslate(cacheEntity.width / 2 + cacheEntity.posX, cacheEntity.height
                                    / 2 + cacheEntity.posY);
                            canvas.drawBitmap(cacheEntity.bitmap, mMatrix, null);
                        }
                        interval = (int)(new Date().getTime() - date.getTime());
                        Thread.sleep(Math.max(0, 10 - interval));

                }catch (Exception exception) {

                }finally {
                    // 解锁画布
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}

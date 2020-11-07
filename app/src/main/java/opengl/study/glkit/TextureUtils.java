package opengl.study.glkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by burt on 2016. 6. 24..
 */
public class TextureUtils {

    public static int loadTexture(Context context, int drawableID) {
        return loadTexture(context, drawableID, true);
    }

    public static synchronized int loadTexture(final Context context, final int drawableID, boolean isBottomLeftOrigin) {
        Log.e("TextureUtils", "loadTexture start");
        final Bitmap[] bitmap = {null};
        final Bitmap[] flippedBitmap = {null};

        final Thread glthread = Thread.currentThread();
        int textureName = 0;
        try {
            Log.e("TextureUtils", "loadTexture 0 drawableID=" + drawableID);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("TextureUtils", "BitmapFactory.decodeResource bitmap[0]=" + (bitmap[0] == null));
                    bitmap[0] = BitmapFactory.decodeResource(context.getResources(), drawableID);
                    Log.e("TextureUtils", "BitmapFactory.decodeResource end bitmap[0]=" + (bitmap[0] == null));
                    LockSupport.unpark(glthread);
                }
            }).start();
            LockSupport.park();
            Log.e("TextureUtils", "loadTexture 1");
            if (isBottomLeftOrigin) {
                final Matrix flip = new Matrix();
                flip.postScale(1f, -1f);
                Log.e("TextureUtils", "loadTexture 2");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TextureUtils", "Bitmap.createBitmap flippedBitmap[0]="+ (flippedBitmap[0] == null));
                        flippedBitmap[0] = Bitmap.createBitmap(bitmap[0], 0, 0, bitmap[0].getWidth(), bitmap[0].getHeight(), flip, false);
                        Log.e("TextureUtils", "Bitmap.createBitmap end flippedBitmap[0]=" + (flippedBitmap[0] == null));
                        LockSupport.unpark(glthread);
                    }
                }).start();
                LockSupport.park();
                Log.e("TextureUtils", "loadTexture 3");
                textureName = loadTexture(flippedBitmap[0]);
                Log.e("TextureUtils", "loadTexture 4 textureName="+textureName);
            } else {
                Log.e("TextureUtils", "loadTexture 5");
                textureName = loadTexture(bitmap[0]);
                Log.e("TextureUtils", "loadTexture 6");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TextureUtils", "loadTexture error");
        } finally {

            if (bitmap[0] != null) {
                bitmap[0].recycle();
                bitmap[0] = null;
            }

            if (flippedBitmap[0] != null) {
                flippedBitmap[0].recycle();
                flippedBitmap[0] = null;
            }
            Log.e("TextureUtils", "loadTexture finally");
        }
        Log.e("TextureUtils", "loadTexture end");
        return textureName;
    }

    public static int loadTexture(Bitmap bitmap) {

        int textureName[] = new int[1];
        Log.e("TextureUtils", "loadTexture 11");
        GLES20.glGenTextures(1, textureName, 0);
        Log.e("TextureUtils", "loadTexture 12");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureName[0]);
        Log.e("TextureUtils", "loadTexture 13");

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        Log.e("TextureUtils", "loadTexture 14");
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        Log.e("TextureUtils", "loadTexture 15");

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        Log.e("TextureUtils", "loadTexture 16");

        return textureName[0];
    }
}

package opengl.study.demos._1hello;

import android.opengl.GLSurfaceView;

import android.opengl.GLES20;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RedAlertOGLRenderer implements GLSurfaceView.Renderer{


    double redValue = 1.0f;
    private static final double DURATION_OF_FLASH = 1000.0; // 1 second

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor((float)redValue, 0.0f, 0.0f, 1.0f);
        Log.e("","onSurfaceCreated");
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

        Log.e("","onSurfaceChanged");
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.e("","onDrawFrame");
        GLES20.glClearColor((float)redValue, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        redValue = ((Math.sin(System.currentTimeMillis() * 2 * Math.PI / DURATION_OF_FLASH) * 0.5) + 0.5);
    }

}

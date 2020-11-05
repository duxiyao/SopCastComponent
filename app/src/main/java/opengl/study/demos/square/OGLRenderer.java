package opengl.study.demos.square;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by burt on 2016. 6. 15..
 */
public class OGLRenderer implements GLSurfaceView.Renderer {

    private Context context;
    private Square square;
    private Point point;
    Line line;
    Triangle triangle;
    Star star;

    public OGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        square = new Square(context);
        point = new Point(context);
        line = new Line(context);
        triangle = new Triangle(context);
        star = new Star(context);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        GLES20.glViewport(0, 0, w, h);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
//        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1f);
//        GLES20.glClearColor(0f, 0.0f, 0.0f, 1f);
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.5f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

//        square.draw();
        point.draw();
//        line.draw();
//        triangle.draw();
//        star.draw();
    }

}

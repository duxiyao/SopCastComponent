package opengl.study.demos.colorindex;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.laifeng.sopcastdemo.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.study.demos.square.Line;
import opengl.study.demos.square.Point;
import opengl.study.demos.square.Star;
import opengl.study.demos.square.Triangle;
import opengl.study.glkit.ShaderProgram;
import opengl.study.glkit.ShaderUtils;

/**
 * Created by burt on 2016. 6. 15..
 */
public class OGLRenderer implements GLSurfaceView.Renderer {

    private Context context;
    Square square;
    ColoredSquare coloredSquare;
    //需要切换的gl3.0
    VertexArrayObjectSquare vertexArrayObjectSquare;
    MySquare mySquare;

    public OGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        square = new Square(context);
        coloredSquare = new ColoredSquare(context);
        vertexArrayObjectSquare = new VertexArrayObjectSquare(context);

        ShaderProgram shader = new ShaderProgram(
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.color_index_vertex_shader),
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.color_index_frag_shader)
        );
        mySquare = new MySquare(shader);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        GLES20.glViewport(0, 0, w, h);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
//        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1f);
        GLES20.glClearColor(0f, 0.0f, 0.0f, 1f);
//        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.5f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

//        square.draw();
//        coloredSquare.draw();
//        vertexArrayObjectSquare.draw();
        mySquare.draw();
    }

}

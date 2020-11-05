package opengl.study.demos.move;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.renderscript.Float3;

import com.laifeng.sopcastdemo.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.study.glkit.ShaderProgram;
import opengl.study.glkit.ShaderUtils;

public class OGLRenderer implements GLSurfaceView.Renderer {

    private Context context;
    private Square square;

    public OGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        ShaderProgram shader = new ShaderProgram(
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.move_vertex_shader),
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.color_index_frag_shader)
        );
        square = new Square(shader);
//        square.setPosition(new Float3(0.5f, -0.5f, 0.0f));
        square.setPosition(new Float3(0f, 0f, 0.0f));
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        GLES20.glViewport(0, 0, w, h);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        square.draw();
    }

}
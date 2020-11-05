package opengl.study.demos._4move;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.renderscript.Float3;
import android.renderscript.Matrix4f;

import com.laifeng.sopcastdemo.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.study.glkit.ShaderProgram;
import opengl.study.glkit.ShaderUtils;

public class MoveRender implements GLSurfaceView.Renderer {

    private static final float ONE_SEC = 1000.0f; // 1 second

    private Context context;
    private MoveSquare square;
    TransformSquare transformSquare;
    private long lastTimeMillis = 0L;

    public MoveRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        ShaderProgram shader = new ShaderProgram(
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.move_vertex_shader),
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.color_index_frag_shader)
        );
        square = new MoveSquare(shader);
        transformSquare = new TransformSquare(shader);

        square.setPosition(new Float3(0.0f, 0.0f, 0.0f));
        transformSquare.setPosition(new Float3(0.0f, 0.0f, 0.0f));

        lastTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        GLES20.glViewport(0, 0, w, h);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        long currentTimeMillis = System.currentTimeMillis();
//        updateWithDelta(currentTimeMillis - lastTimeMillis);
        updateWithDelta2(currentTimeMillis - lastTimeMillis);
        lastTimeMillis = currentTimeMillis;
    }

    public void updateWithDelta(long dt) {

        final float secsPerMove = 2.0f * ONE_SEC;
        square.setPosition(new Float3(
                (float)(Math.sin(System.currentTimeMillis() * 2 * Math.PI / secsPerMove)),
                square.position.y,
                square.position.z)
        );

        square.draw(dt);
    }

    public void updateWithDelta2(long dt) {

        final float secsPerMove = 2.0f * ONE_SEC;
        float movement = (float)(Math.sin(System.currentTimeMillis() * 2 * Math.PI / secsPerMove));

        Matrix4f camera = new Matrix4f();
        camera.translate(0.0f, -1.0f * movement, 0.0f);
        camera.rotate(360.0f * movement, 0.0f, 0.0f, 1.0f);
        camera.scale(movement, movement, movement);
        transformSquare.setCamera(camera);


        transformSquare.draw(dt);
    }
}

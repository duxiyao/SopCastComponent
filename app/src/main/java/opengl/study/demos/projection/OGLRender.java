package opengl.study.demos.projection;

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

public class OGLRender implements GLSurfaceView.Renderer {

    private static final float ONE_SEC = 1000.0f; // 1 second

    private Context context;
    private Square square;
    private Cube cube;
    private long lastTimeMillis = 0L;

    public OGLRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        ShaderProgram shader = new ShaderProgram(
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.projection_vertex_shader),
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.color_index_frag_shader)
        );

        square = new Square(shader);
        cube = new Cube(shader);

        square.setPosition(new Float3(0.0f, 0.0f, 0.0f));
        cube.setPosition(new Float3(0.0f, 0.0f, 0.0f));

        lastTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        GLES20.glViewport(0, 0, w, h);

        if (square != null) {
            Matrix4f perspective = new Matrix4f();
            perspective.loadPerspective(85.0f, (float) w / (float) h, 1.0f, -150.0f);
            square.setProjection(perspective);
        }

        if (cube != null) {
            Matrix4f perspective = new Matrix4f();
            perspective.loadPerspective(85.0f, (float) w / (float) h, 1.0f, -150.0f);
            cube.setProjection(perspective);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        long currentTimeMillis = System.currentTimeMillis();
//        updateWithDelta(currentTimeMillis - lastTimeMillis);
//        updateWithDelta2(currentTimeMillis - lastTimeMillis);
        updateWithDelta3(currentTimeMillis - lastTimeMillis);
        lastTimeMillis = currentTimeMillis;
    }

    public void updateWithDelta(long dt) {

        final float secsPerMove = 2.0f * ONE_SEC;
        float movement = (float) (Math.sin(System.currentTimeMillis() * 2 * Math.PI / secsPerMove));

        // move camera
        Matrix4f camera = new Matrix4f();
        camera.translate(0.0f, -1.0f * movement, -15.0f);
        camera.rotate(360.0f * movement, 0.0f, 0.0f, 1.0f);
        camera.scale(movement, movement, movement);
        square.setCamera(camera);
        square.draw(dt);

    }

    //透明立方体
    public void updateWithDelta2(long dt) {

        Matrix4f camera2 = new Matrix4f();
        camera2.translate(0.0f, 0.0f, -5.0f);
        cube.setCamera(camera2);
        cube.setRotationY((float) (cube.rotationY + Math.PI * dt / (ONE_SEC * 0.1f)));
        cube.setRotationZ((float) (cube.rotationZ + Math.PI * dt / (ONE_SEC * 0.1f)));
        cube.draw(dt);
    }

    //不透明立方体
    public void updateWithDelta3(long dt) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        Matrix4f camera2 = new Matrix4f();
        camera2.translate(0.0f, 0.0f, -5.0f);
        cube.setCamera(camera2);
        cube.setRotationY((float) (cube.rotationY + Math.PI * dt / (ONE_SEC * 0.1f)));
        cube.setRotationZ((float) (cube.rotationZ + Math.PI * dt / (ONE_SEC * 0.1f)));
        cube.draw(dt);
    }
}

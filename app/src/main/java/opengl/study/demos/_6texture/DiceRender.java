package opengl.study.demos._6texture;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.renderscript.Float3;
import android.renderscript.Matrix4f;
import android.util.Log;

import com.laifeng.sopcastdemo.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.study.glkit.ShaderProgram;
import opengl.study.glkit.ShaderUtils;
import opengl.study.glkit.TextureUtils;

public class DiceRender implements GLSurfaceView.Renderer {

    private static final float ONE_SEC = 1000.0f; // 1 second

    private Context context;
    private DiceCube   cube;
    private long lastTimeMillis = 0L;
    int textureName;
    public DiceRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.e("---","onSurfaceCreated");

        ShaderProgram shader = new ShaderProgram(
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.dice_vertex_shader),
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.dice_frag_shader)
        );

        Log.e("DiceRender","loadTexture");
        int textureName = TextureUtils.loadTexture(context, R.mipmap.dice);
        Log.e("DiceRender","loadTexture end textureName="+textureName);
        cube = new DiceCube(shader);
        Log.e("DiceRender","loadTexture end 1");
        cube.setPosition(new Float3(0.0f, 0.0f, 0.0f));
        Log.e("DiceRender","loadTexture end 2");
        cube.setTexture(textureName);
        Log.e("DiceRender","loadTexture end 3");

        lastTimeMillis = System.currentTimeMillis();
        Log.e("DiceRender","onSurfaceCreated end textureName="+textureName);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        Log.e("---","onSurfaceChanged");
        GLES20.glViewport(0, 0, w, h);

        Matrix4f perspective = new Matrix4f();
        perspective.loadPerspective(85.0f, (float)w / (float)h, 1.0f, -150.0f);

        if(cube != null) {
            cube.setProjection(perspective);
        }
        Log.e("DiceRender","onSurfaceChanged");
    }

    /**
     * GLSurfaceView has default 16bit depth buffer
     */
    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.e("---","onDrawFrame");
        Log.e("DiceRender","onDrawFrame start");
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        long currentTimeMillis = System.currentTimeMillis();
        updateWithDelta(currentTimeMillis - lastTimeMillis);
        lastTimeMillis = currentTimeMillis;
        Log.e("DiceRender","onDrawFrame end");
        //libc: Fatal signal 11 (SIGSEGV), code 2 (SEGV_ACCERR), fault addr 0xd8982ce0 in tid 3073 (GLThread 5
    }

    public void updateWithDelta(long dt) {

        Matrix4f camera2 = new Matrix4f();
        camera2.translate(0.0f, 0.0f, -5.0f);
        cube.setCamera(camera2);
        cube.setRotationY((float)( cube.rotationY + Math.PI * dt / (ONE_SEC * 0.1f) ));
        cube.setRotationZ((float)( cube.rotationZ + Math.PI * dt / (ONE_SEC * 0.1f) ));
        cube.draw(dt);
    }

}

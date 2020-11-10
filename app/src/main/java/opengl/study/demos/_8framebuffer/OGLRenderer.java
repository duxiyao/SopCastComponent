package opengl.study.demos._8framebuffer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.renderscript.Float3;
import android.renderscript.Matrix4f;

import com.laifeng.sopcastdemo.R;
import com.laifeng.sopcastsdk.camera.CameraHolder;
import com.laifeng.sopcastsdk.video.GlUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.study.demos._6texture.Cube;
import opengl.study.demos._6texture.MaskedSquare;
import opengl.study.glkit.ShaderProgram;
import opengl.study.glkit.ShaderUtils;
import opengl.study.glkit.TextureUtils;

public class OGLRenderer implements GLSurfaceView.Renderer {

    private static final float ONE_SEC = 1000.0f; // 1 second

    private Context context;
    private FBSquare fbSquare;

    private long lastTimeMillis = 0L;

    public OGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        ShaderProgram shader = new ShaderProgram(
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.framebuffer_vertex_shader),
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.framebuffer_frag_shader)
        );
        ShaderProgram beautyShader = new ShaderProgram(
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.beauty_vertex_shader),
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.beauty_frag_shader)
        );

        int face = TextureUtils.loadTexture(context, R.mipmap.face);

        fbSquare = new FBSquare(shader);
        fbSquare.setPosition(new Float3(0.0f, 0.0f, 0.0f));
        fbSquare.setTexture(face);

        lastTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        GLES20.glViewport(0, 0, w, h);
        Matrix4f perspective = new Matrix4f();
        perspective.loadPerspective(105.0f, (float) w / (float) h, 1.0f, -150.0f);

        if (fbSquare != null) {
            fbSquare.createEffectTexture(w,h,context);
            fbSquare.setProjection(perspective);
        }
    }

    /**
     * GLSurfaceView has default 16bit depth buffer
     */
    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        long currentTimeMillis = System.currentTimeMillis();
        updateWithDelta(currentTimeMillis - lastTimeMillis);
        lastTimeMillis = currentTimeMillis;
    }

    public void updateWithDelta(long dt) {
        Matrix4f camera = new Matrix4f();
        camera.translate(-0.0f, 0.0f, -1f);
        fbSquare.setCamera(camera);
//        fbSquare.draw();
        fbSquare.drawFB();
    }

}

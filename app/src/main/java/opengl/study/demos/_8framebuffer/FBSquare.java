package opengl.study.demos._8framebuffer;

import android.content.Context;
import android.opengl.GLES20;

import com.laifeng.sopcastsdk.video.GlUtil;
import com.laifeng.sopcastsdk.video.effect.BeautyEffect;

import opengl.study.demos.model.FrameBufferModel;
import opengl.study.glkit.ShaderProgram;

public class FBSquare extends FrameBufferModel {

    static final float vertices[] = {
            1.0f, -1.0f, 0f, 1f, 0f,
            1.0f, 1.0f, 0f, 1f, 1f,
            -1.0f, 1.0f, 0f, 0f, 1f,
            -1.0f, -1.0f, 0f, 0f, 0f

    };

    static final short indices[] = {
            0, 1, 2,
            2, 3, 0
    };

    private final int[] mFboId = new int[]{0};
    private final int[] mRboId = new int[]{0};
    private final int[] mTexId = new int[]{0};

    public FBSquare(ShaderProgram shader) {
        super("FBSquare", shader, vertices, indices);
    }

    private int w, h;

    public void createEffectTexture(int w, int h, Context context) {
        this.w = w;
        this.h = h;
        GlUtil.checkGlError("initFBO_S");

        GLES20.glGenFramebuffers(1, mFboId, 0);
        GLES20.glGenTextures(1, mTexId, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexId[0]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, w, h, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFboId[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mTexId[0], 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

//        GLES20.glGenFramebuffers(1, mFboId, 0);
//        GLES20.glGenRenderbuffers(1, mRboId, 0);
//        GLES20.glGenTextures(1, mTexId, 0);
//
//        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, mRboId[0]);
//        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER,
//                GLES20.GL_DEPTH_COMPONENT16, w, h);
//
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFboId[0]);
//        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER,
//                GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, mRboId[0]);
//
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexId[0]);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
//                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
//                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
//                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
//                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
//
//        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,
//                w, h, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
//
//        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
//                GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mTexId[0], 0);
//
//        if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) !=
//                GLES20.GL_FRAMEBUFFER_COMPLETE) {
//            throw new RuntimeException("glCheckFramebufferStatus()");
//        }
        GlUtil.checkGlError("initFBO_E");
    }

    private boolean fbdrawed = false;

    private final float[] mTexMtx = GlUtil.createIdentityMtx();
    /**
     * 使用fbo的文理绘制
     */
    public void drawFB(){

        if (!fbdrawed) {
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFboId[0]);
            GLES20.glViewport(0, 0, w, h);
            GLES20.glClearColor(0f, 1f, 0f, 1f);
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            draw(textureName);
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            fbdrawed = true;
        }
//        draw(mTexId[0]);
        drawbf(mTexId[0]);
    }
}

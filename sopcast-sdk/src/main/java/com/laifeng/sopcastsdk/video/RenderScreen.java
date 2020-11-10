package com.laifeng.sopcastsdk.video;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.laifeng.sopcastsdk.camera.CameraData;
import com.laifeng.sopcastsdk.camera.CameraHolder;
import com.laifeng.sopcastsdk.entity.Watermark;
import com.laifeng.sopcastsdk.entity.WatermarkPosition;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;

/**
 * @Title: RenderScreen
 * @Package com.laifeng.sopcastsdk.video
 * @Description:
 * @Author Jim
 * @Date 16/9/14
 * @Time 下午2:15
 * @Version
 */
public class RenderScreen {
    private final FloatBuffer mNormalVtxBuf = GlUtil.createVertexBuffer();
    private final FloatBuffer mNormalTexCoordBuf = GlUtil.createTexCoordBuffer();
    private final float[] mPosMtx = GlUtil.createIdentityMtx();

    private int mFboTexId;

    private int mProgram = -1;
    private int maPositionHandle = -1;
    private int maTexCoordHandle = -1;
    private int muPosMtxHandle = -1;
    private int muSamplerHandle = -1;

    private int mScreenW = -1;
    private int mScreenH = -1;

    private FloatBuffer mCameraTexCoordBuffer;

    private Bitmap mWatermarkImg;
    private Watermark mWatermark;
    private FloatBuffer mWatermarkVertexBuffer;
    private int mWatermarkTextureId = -1;
    private float mWatermarkRatio = 1.0f;

    public RenderScreen(int id) {
        mFboTexId = id;
        initGL();
    }

    public void setSreenSize(int width, int height) {
        mScreenW = width;
        mScreenH = height;

        initCameraTexCoordBuffer();
    }

    public void setTextureId(int textureId) {
        mFboTexId = textureId;
    }

    public void setVideoSize(int width, int height) {
        mWatermarkRatio = mScreenW / ((float) width);
        if (mWatermark != null) {
            initWatermarkVertexBuffer();
        }
    }

    private void initCameraTexCoordBuffer() {
        int cameraWidth, cameraHeight;
        CameraData cameraData = CameraHolder.instance().getCameraData();
        int width = cameraData.cameraWidth;
        int height = cameraData.cameraHeight;
        if (CameraHolder.instance().isLandscape()) {
            cameraWidth = Math.max(width, height);
            cameraHeight = Math.min(width, height);
        } else {
            cameraWidth = Math.min(width, height);
            cameraHeight = Math.max(width, height);
        }

        float hRatio = mScreenW / ((float) cameraWidth);
        float vRatio = mScreenH / ((float) cameraHeight);

        float ratio;
        if (hRatio > vRatio) {
            ratio = mScreenH / (cameraHeight * hRatio);
            final float vtx[] = {
                    //UV
                    0f, 0.5f + ratio / 2,
                    0f, 0.5f - ratio / 2,
                    1f, 0.5f + ratio / 2,
                    1f, 0.5f - ratio / 2,
            };
            ByteBuffer bb = ByteBuffer.allocateDirect(4 * vtx.length);
            bb.order(ByteOrder.nativeOrder());
            mCameraTexCoordBuffer = bb.asFloatBuffer();
            mCameraTexCoordBuffer.put(vtx);
            mCameraTexCoordBuffer.position(0);
        } else {
            ratio = mScreenW / (cameraWidth * vRatio);
            final float vtx[] = {
                    //UV
                    0.5f - ratio / 2, 1f,
                    0.5f - ratio / 2, 0f,
                    0.5f + ratio / 2, 1f,
                    0.5f + ratio / 2, 0f,
            };
            ByteBuffer bb = ByteBuffer.allocateDirect(4 * vtx.length);
            bb.order(ByteOrder.nativeOrder());
            mCameraTexCoordBuffer = bb.asFloatBuffer();
            mCameraTexCoordBuffer.put(vtx);
            mCameraTexCoordBuffer.position(0);
        }

    }

    public void setWatermark(Watermark watermark) {
        mWatermark = watermark;
        mWatermarkImg = watermark.markImg;
        initWatermarkVertexBuffer();
    }

    private void initWatermarkVertexBuffer() {
        if (mScreenW <= 0 || mScreenH <= 0) {
            return;
        }

        int width = (int) (mWatermark.width * mWatermarkRatio);
        int height = (int) (mWatermark.height * mWatermarkRatio);
        int vMargin = (int) (mWatermark.vMargin * mWatermarkRatio);
        int hMargin = (int) (mWatermark.hMargin * mWatermarkRatio);

        boolean isTop, isRight;
        if (mWatermark.orientation == WatermarkPosition.WATERMARK_ORIENTATION_TOP_LEFT
                || mWatermark.orientation == WatermarkPosition.WATERMARK_ORIENTATION_TOP_RIGHT) {
            isTop = true;
        } else {
            isTop = false;
        }

        if (mWatermark.orientation == WatermarkPosition.WATERMARK_ORIENTATION_TOP_RIGHT
                || mWatermark.orientation == WatermarkPosition.WATERMARK_ORIENTATION_BOTTOM_RIGHT) {
            isRight = true;
        } else {
            isRight = false;
        }

        float leftX = (mScreenW / 2.0f - hMargin - width) / (mScreenW / 2.0f);
        float rightX = (mScreenW / 2.0f - hMargin) / (mScreenW / 2.0f);

        float topY = (mScreenH / 2.0f - vMargin) / (mScreenH / 2.0f);
        float bottomY = (mScreenH / 2.0f - vMargin - height) / (mScreenH / 2.0f);

        float temp;

        if (!isRight) {
            temp = leftX;
            leftX = -rightX;
            rightX = -temp;
        }
        if (!isTop) {
            temp = topY;
            topY = -bottomY;
            bottomY = -temp;
        }
        final float watermarkCoords[] = {
                leftX, bottomY, 0.0f,
                leftX, topY, 0.0f,
                rightX, bottomY, 0.0f,
                rightX, topY, 0.0f
        };
        ByteBuffer bb = ByteBuffer.allocateDirect(watermarkCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mWatermarkVertexBuffer = bb.asFloatBuffer();
        mWatermarkVertexBuffer.put(watermarkCoords);
        mWatermarkVertexBuffer.position(0);
    }

    public void draw() {
        if (mScreenW <= 0 || mScreenH <= 0) {
            return;
        }
        GlUtil.checkGlError("draw_S");

        GLES20.glViewport(0, 0, mScreenW, mScreenH);

        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(mProgram);
        mNormalVtxBuf.position(0);
        GLES20.glVertexAttribPointer(maPositionHandle,
                3, GLES20.GL_FLOAT, false, 4 * 3, mNormalVtxBuf);
        GLES20.glEnableVertexAttribArray(maPositionHandle);

        mCameraTexCoordBuffer.position(0);
        GLES20.glVertexAttribPointer(maTexCoordHandle,
                2, GLES20.GL_FLOAT, false, 4 * 2, mCameraTexCoordBuffer);
        GLES20.glEnableVertexAttribArray(maTexCoordHandle);

        GLES20.glUniformMatrix4fv(muPosMtxHandle, 1, false, mPosMtx, 0);
        GLES20.glUniform1i(muSamplerHandle, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFboTexId);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        //绘制纹理
        drawWatermark();
        runPendingOnDrawTasks();

        GlUtil.checkGlError("draw_E");
    }

    private void drawWatermark() {
        if (mWatermarkImg == null) {
            return;
        }
        mWatermarkVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(maPositionHandle,
                3, GLES20.GL_FLOAT, false, 4 * 3, mWatermarkVertexBuffer);
        GLES20.glEnableVertexAttribArray(maPositionHandle);

        mNormalTexCoordBuf.position(0);
        GLES20.glVertexAttribPointer(maTexCoordHandle,
                2, GLES20.GL_FLOAT, false, 4 * 2, mNormalTexCoordBuf);
        GLES20.glEnableVertexAttribArray(maTexCoordHandle);

        if (mWatermarkTextureId == -1) {
            int[] textures = new int[1];
            GLES20.glGenTextures(1, textures, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mWatermarkImg, 0);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                    GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                    GLES20.GL_CLAMP_TO_EDGE);
            mWatermarkTextureId = textures[0];
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mWatermarkTextureId);

        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    private void initGL() {
        GlUtil.checkGlError("initGL_S");

        final String vertexShader =
                //
                "attribute vec4 position;\n" +
                        "attribute vec4 inputTextureCoordinate;\n" +
                        "uniform   mat4 uPosMtx;\n" +
                        "varying   vec2 textureCoordinate;\n" +
                        "void main() {\n" +
                        "  gl_Position = uPosMtx * position;\n" +
                        "  textureCoordinate   = inputTextureCoordinate.xy;\n" +
                        "}\n";
//        final String fragmentShader =
//                //
//                "precision mediump float;\n" +
//                        "uniform sampler2D uSampler;\n" +
//                        "varying vec2  textureCoordinate;\n" +
//                        "void main() {\n" +
//                        "  gl_FragColor = texture2D(uSampler, textureCoordinate);\n" +
//                        "}\n";

        final String fragmentShader = "" +
                "precision highp float;\n"+
                "   varying highp vec2 textureCoordinate;\n" +
                "\n" +
                "    uniform sampler2D uSampler;\n" +
                "\n" +
                "    uniform highp vec2 singleStepOffset;\n" +
                "    uniform highp vec4 params;\n" +
                "    uniform highp float brightness;\n" +
                "\n" +
                "    const highp vec3 W = vec3(0.299, 0.587, 0.114);\n" +
                "    const highp mat3 saturateMatrix = mat3(\n" +
                "        1.1102, -0.0598, -0.061,\n" +
                "        -0.0774, 1.0826, -0.1186,\n" +
                "        -0.0228, -0.0228, 1.1772);\n" +
                "    highp vec2 blurCoordinates[24];\n" +
                "\n" +
                "    highp float hardLight(highp float color) {\n" +
                "    if (color <= 0.5)\n" +
                "        color = color * color * 2.0;\n" +
                "    else\n" +
                "        color = 1.0 - ((1.0 - color)*(1.0 - color) * 2.0);\n" +
                "    return color;\n" +
                "}\n" +
                "\n" +
                "    void main(){\n" +
                "    highp vec3 centralColor = texture2D(uSampler, textureCoordinate).rgb;\n" +
                "    blurCoordinates[0] = textureCoordinate.xy + singleStepOffset * vec2(0.0, -10.0);\n" +
                "    blurCoordinates[1] = textureCoordinate.xy + singleStepOffset * vec2(0.0, 10.0);\n" +
                "    blurCoordinates[2] = textureCoordinate.xy + singleStepOffset * vec2(-10.0, 0.0);\n" +
                "    blurCoordinates[3] = textureCoordinate.xy + singleStepOffset * vec2(10.0, 0.0);\n" +
                "    blurCoordinates[4] = textureCoordinate.xy + singleStepOffset * vec2(5.0, -8.0);\n" +
                "    blurCoordinates[5] = textureCoordinate.xy + singleStepOffset * vec2(5.0, 8.0);\n" +
                "    blurCoordinates[6] = textureCoordinate.xy + singleStepOffset * vec2(-5.0, 8.0);\n" +
                "    blurCoordinates[7] = textureCoordinate.xy + singleStepOffset * vec2(-5.0, -8.0);\n" +
                "    blurCoordinates[8] = textureCoordinate.xy + singleStepOffset * vec2(8.0, -5.0);\n" +
                "    blurCoordinates[9] = textureCoordinate.xy + singleStepOffset * vec2(8.0, 5.0);\n" +
                "    blurCoordinates[10] = textureCoordinate.xy + singleStepOffset * vec2(-8.0, 5.0);\n" +
                "    blurCoordinates[11] = textureCoordinate.xy + singleStepOffset * vec2(-8.0, -5.0);\n" +
                "    blurCoordinates[12] = textureCoordinate.xy + singleStepOffset * vec2(0.0, -6.0);\n" +
                "    blurCoordinates[13] = textureCoordinate.xy + singleStepOffset * vec2(0.0, 6.0);\n" +
                "    blurCoordinates[14] = textureCoordinate.xy + singleStepOffset * vec2(6.0, 0.0);\n" +
                "    blurCoordinates[15] = textureCoordinate.xy + singleStepOffset * vec2(-6.0, 0.0);\n" +
                "    blurCoordinates[16] = textureCoordinate.xy + singleStepOffset * vec2(-4.0, -4.0);\n" +
                "    blurCoordinates[17] = textureCoordinate.xy + singleStepOffset * vec2(-4.0, 4.0);\n" +
                "    blurCoordinates[18] = textureCoordinate.xy + singleStepOffset * vec2(4.0, -4.0);\n" +
                "    blurCoordinates[19] = textureCoordinate.xy + singleStepOffset * vec2(4.0, 4.0);\n" +
                "    blurCoordinates[20] = textureCoordinate.xy + singleStepOffset * vec2(-2.0, -2.0);\n" +
                "    blurCoordinates[21] = textureCoordinate.xy + singleStepOffset * vec2(-2.0, 2.0);\n" +
                "    blurCoordinates[22] = textureCoordinate.xy + singleStepOffset * vec2(2.0, -2.0);\n" +
                "    blurCoordinates[23] = textureCoordinate.xy + singleStepOffset * vec2(2.0, 2.0);\n" +
                "\n" +
                "    highp float sampleColor = centralColor.g * 22.0;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[0]).g;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[1]).g;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[2]).g;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[3]).g;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[4]).g;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[5]).g;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[6]).g;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[7]).g;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[8]).g;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[9]).g;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[10]).g;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[11]).g;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[12]).g * 2.0;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[13]).g * 2.0;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[14]).g * 2.0;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[15]).g * 2.0;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[16]).g * 2.0;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[17]).g * 2.0;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[18]).g * 2.0;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[19]).g * 2.0;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[20]).g * 3.0;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[21]).g * 3.0;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[22]).g * 3.0;\n" +
                "    sampleColor += texture2D(uSampler, blurCoordinates[23]).g * 3.0;\n" +
                "\n" +
                "    sampleColor = sampleColor / 62.0;\n" +
                "\n" +
                "    highp float highPass = centralColor.g - sampleColor + 0.5;\n" +
                "\n" +
                "    for (int i = 0; i < 5; i++) {\n" +
                "        highPass = hardLight(highPass);\n" +
                "    }\n" +
                "    highp float lumance = dot(centralColor, W);\n" +
                "\n" +
                "    highp float alpha = pow(lumance, params.r);\n" +
                "\n" +
                "    highp vec3 smoothColor = centralColor + (centralColor-vec3(highPass))*alpha*0.1;\n" +
                "\n" +
                "    smoothColor.r = clamp(pow(smoothColor.r, params.g), 0.0, 1.0);\n" +
                "    smoothColor.g = clamp(pow(smoothColor.g, params.g), 0.0, 1.0);\n" +
                "    smoothColor.b = clamp(pow(smoothColor.b, params.g), 0.0, 1.0);\n" +
                "\n" +
                "    highp vec3 lvse = vec3(1.0)-(vec3(1.0)-smoothColor)*(vec3(1.0)-centralColor);\n" +
                "    highp vec3 bianliang = max(smoothColor, centralColor);\n" +
                "    highp vec3 rouguang = 2.0*centralColor*smoothColor + centralColor*centralColor - 2.0*centralColor*centralColor*smoothColor;\n" +
                "\n" +
                "    gl_FragColor = vec4(mix(centralColor, lvse, alpha), 1.0);\n" +
                "    gl_FragColor.rgb = mix(gl_FragColor.rgb, bianliang, alpha);\n" +
                "    gl_FragColor.rgb = mix(gl_FragColor.rgb, rouguang, params.b);\n" +
                "\n" +
                "    highp vec3 satcolor = gl_FragColor.rgb * saturateMatrix;\n" +
                "    gl_FragColor.rgb = mix(gl_FragColor.rgb, satcolor, params.a);\n" +
                "    gl_FragColor.rgb = vec3(gl_FragColor.rgb + vec3(brightness));\n" +
                "}";
        mProgram = GlUtil.createProgram(vertexShader, fragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "position");
        maTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        muPosMtxHandle = GLES20.glGetUniformLocation(mProgram, "uPosMtx");
        muSamplerHandle = GLES20.glGetUniformLocation(mProgram, "uSampler");

        paramsLocation = GLES20.glGetUniformLocation(mProgram, "params");
        brightnessLocation = GLES20.glGetUniformLocation(mProgram, "brightness");
        singleStepOffsetLocation = GLES20.glGetUniformLocation(mProgram, "singleStepOffset");

//        toneLevel = -0.5f;
//        beautyLevel = 0.8f;
//        brightLevel = 0.3f;
        toneLevel = 3f;
        beautyLevel = 2.6f;
        brightLevel = 0.7f;

        setParams(beautyLevel, toneLevel);
        setBrightLevel(brightLevel);

        GlUtil.checkGlError("initGL_E");
    }

    private int paramsLocation;
    private int brightnessLocation;
    private int singleStepOffsetLocation;
    private float toneLevel;
    private float beautyLevel;
    private float brightLevel;
    private final LinkedList<Runnable> mRunOnDraw = new LinkedList<>();

    //磨皮
    public void setBeautyLevel(float beautyLevel) {
        this.beautyLevel = beautyLevel;
        setParams(beautyLevel, toneLevel);
    }

    //美白
    public void setBrightLevel(float brightLevel) {
        this.brightLevel = brightLevel;
        setFloat(brightnessLocation, 0.6f * (-0.5f + brightLevel));
//        GLES20.glUniform1f(brightnessLocation, 0.6f * (-0.5f + brightLevel));
    }

    //红润
    public void setToneLevel(float toneLevel) {
        this.toneLevel = toneLevel;
        setParams(beautyLevel, toneLevel);
    }

    public void setAllBeautyParams(float beauty, float bright, float tone) {
        setBeautyLevel(beauty);
        setBrightLevel(bright);
        setToneLevel(tone);
    }

    public void setParams(float beauty, float tone) {
        float[] vector = new float[4];
        vector[0] = 1.0f - 0.6f * beauty;
        vector[1] = 1.0f - 0.3f * beauty;
        vector[2] = 0.1f + 0.3f * tone;
        vector[3] = 0.1f + 0.3f * tone;
        setFloatVec4(paramsLocation, vector);
//        GLES20.glUniform4fv(paramsLocation, 1, FloatBuffer.wrap(vector));
    }

    protected void runOnDraw(final Runnable runnable) {
        synchronized (mRunOnDraw) {
            mRunOnDraw.addLast(runnable);
        }
    }

    protected void runPendingOnDrawTasks() {
        while (!mRunOnDraw.isEmpty()) {
            mRunOnDraw.removeFirst().run();
        }
    }

    protected void setFloatVec4(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform4fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloat(final int location, final float floatValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(location, floatValue);
            }
        });
    }
}

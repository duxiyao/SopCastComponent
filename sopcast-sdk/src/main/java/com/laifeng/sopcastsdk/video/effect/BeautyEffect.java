package com.laifeng.sopcastsdk.video.effect;

import android.content.Context;
import android.opengl.GLES20;

import com.laifeng.sopcastsdk.video.GLSLFileUtils;

public class BeautyEffect extends Effect{

    private static final String GRAY_EFFECT_VERTEX = "beauty/vertexshader.glsl";
    private static final String GRAY_EFFECT_FRAGMENT = "beauty/fragmentshader.glsl";
    private float toneLevel;
    private float beautyLevel;
    private float brightLevel;

    private int paramsLocation;
    private int brightnessLocation;
    private int singleStepOffsetLocation;

    public BeautyEffect(Context context) {
        super();
//        toneLevel = -0.5f;
//        beautyLevel = 0.8f;
//        brightLevel = 0.3f;
        String vertexShader = GLSLFileUtils.getFileContextFromAssets(context, GRAY_EFFECT_VERTEX);
        String fragmentShader = GLSLFileUtils.getFileContextFromAssets(context, GRAY_EFFECT_FRAGMENT);
        super.setShader(vertexShader, fragmentShader);
        toneLevel = 1f;
        beautyLevel = 1.9f;
        brightLevel = 0.3f;
    }

    public void onInit() {
//        toneLevel = -0.6f;
//        beautyLevel = 0.9f;
//        brightLevel = 0.2f;
        paramsLocation = GLES20.glGetUniformLocation(getProgram(), "params");
        brightnessLocation = GLES20.glGetUniformLocation(getProgram(), "brightness");
        singleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");

        setParams(beautyLevel, toneLevel);
        setBrightLevel(brightLevel);
    }

    @Override
    protected void loadOtherParams() {
        onInit();
    }

    //磨皮
    public void setBeautyLevel(float beautyLevel) {
        this.beautyLevel = beautyLevel;
        setParams(beautyLevel, toneLevel);
    }

    //美白
    public void setBrightLevel(float brightLevel) {
        this.brightLevel = brightLevel;
        setFloat(brightnessLocation, 0.6f * (-0.5f + brightLevel));
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
    }
}

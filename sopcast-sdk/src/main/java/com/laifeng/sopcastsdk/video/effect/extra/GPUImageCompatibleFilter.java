//package com.laifeng.sopcastsdk.video.effect.extra;
//
//import android.opengl.GLES20;
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.FloatBuffer;
//import java.nio.ShortBuffer;
//
//import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
//
//public class GPUImageCompatibleFilter<T extends GPUImageFilter> {
//
//    protected int SIZE_WIDTH;
//    protected int SIZE_HEIGHT;
//    protected int directionFlag=-1;
////    protected ShortBuffer drawIndecesBuffer;
//
//    private T innerGPUImageFilter;
//
//    private FloatBuffer innerShapeBuffer;
//    private FloatBuffer innerTextureBuffer;
//
//    public GPUImageCompatibleFilter(T filter) {
//        innerGPUImageFilter = filter;
//    }
//
//    public T getGPUImageFilter() {
//        return innerGPUImageFilter;
//    }
//
//    public void onInit(int VWidth, int VHeight) {
//
//        SIZE_WIDTH = VWidth;
//        SIZE_HEIGHT = VHeight;
////        drawIndecesBuffer = GLHelper.getDrawIndecesBuffer();
//
//        innerGPUImageFilter.init();
//        innerGPUImageFilter.onOutputSizeChanged(VWidth, VHeight);
//    }
//
//    public void onDraw(int cameraTexture) {
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, cameraTexture);
//        innerGPUImageFilter.onDraw(cameraTexture, innerShapeBuffer, innerTextureBuffer);
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
//    }
//
//    public void onDestroy() {
//        innerGPUImageFilter.destroy();
//    }
//
//    public void onDirectionUpdate(int _directionFlag) {
//        if (directionFlag != _directionFlag) {
//            innerShapeBuffer = getGPUImageCompatShapeVerticesBuffer();
//            innerTextureBuffer = getGPUImageCompatTextureVerticesBuffer(directionFlag);
//        }
//    }
//
//    public static final float TEXTURE_NO_ROTATION[] = {
//            1.0f, 1.0f,
//            0.0f, 1.0f,
//            1.0f, 0.0f,
//            0.0f, 0.0f,
//    };
//
//    public static final float TEXTURE_ROTATED_90[] = {
//            0.0f, 1.0f,
//            0.0f, 0.0f,
//            1.0f, 1.0f,
//            1.0f, 0.0f,
//    };
//    public static final float TEXTURE_ROTATED_180[] = {
//            0.0f, 0.0f,
//            1.0f, 0.0f,
//            0.0f, 1.0f,
//            1.0f, 1.0f,
//    };
//    public static final float TEXTURE_ROTATED_270[] = {
//            1.0f, 0.0f,
//            1.0f, 1.0f,
//            0.0f, 0.0f,
//            0.0f, 1.0f,
//    };
//    static final float CUBE[] = {
//            -1.0f, -1.0f,
//            1.0f, -1.0f,
//            -1.0f, 1.0f,
//            1.0f, 1.0f,
//    };
//
//    public static FloatBuffer getGPUImageCompatShapeVerticesBuffer() {
//        FloatBuffer result = ByteBuffer.allocateDirect(4 * CUBE.length).
//                order(ByteOrder.nativeOrder()).
//                asFloatBuffer();
//        result.put(CUBE);
//        result.position(0);
//        return result;
//    }
//
//    public static final int FLAG_DIRECTION_FLIP_HORIZONTAL = 0x01;
//    public static final int FLAG_DIRECTION_FLIP_VERTICAL = 0x02;
//    public static final int FLAG_DIRECTION_ROATATION_0 = 0x10;
//    public static final int FLAG_DIRECTION_ROATATION_90 = 0x20;
//    public static final int FLAG_DIRECTION_ROATATION_180 = 0x40;
//    public static final int FLAG_DIRECTION_ROATATION_270 = 0x80;
//
//    public static FloatBuffer getGPUImageCompatTextureVerticesBuffer(final int directionFlag) {
//        float[] buffer;
//        switch (directionFlag & 0xF0) {
//            case FLAG_DIRECTION_ROATATION_90:
//                buffer = TEXTURE_ROTATED_90.clone();
//                break;
//            case FLAG_DIRECTION_ROATATION_180:
//                buffer = TEXTURE_ROTATED_180.clone();
//                break;
//            case FLAG_DIRECTION_ROATATION_270:
//                buffer = TEXTURE_ROTATED_270.clone();
//                break;
//            default:
//                buffer = TEXTURE_NO_ROTATION.clone();
//        }
//        if ((directionFlag & FLAG_DIRECTION_FLIP_HORIZONTAL) != 0) {
//            buffer[0] = flip(buffer[0]);
//            buffer[2] = flip(buffer[2]);
//            buffer[4] = flip(buffer[4]);
//            buffer[6] = flip(buffer[6]);
//        }
//        if ((directionFlag & FLAG_DIRECTION_FLIP_VERTICAL) != 0) {
//            buffer[1] = flip(buffer[1]);
//            buffer[3] = flip(buffer[3]);
//            buffer[5] = flip(buffer[5]);
//            buffer[7] = flip(buffer[7]);
//        }
//        FloatBuffer result = ByteBuffer.allocateDirect(4 * buffer.length).
//                order(ByteOrder.nativeOrder()).
//                asFloatBuffer();
//        result.put(buffer);
//        result.position(0);
//        return result;
//    }
//
//    private static float flip(final float i) {
//        return i == 0.0f ? 1.0f : 0.0f;
//    }
//
//}

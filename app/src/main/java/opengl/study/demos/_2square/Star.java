package opengl.study.demos._2square;

import android.content.Context;
import android.opengl.GLES20;

import com.laifeng.sopcastdemo.R;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import opengl.study.glkit.BufferUtils;
import opengl.study.glkit.ShaderProgram;
import opengl.study.glkit.ShaderUtils;

public class Star {

    private FloatBuffer vertexBuffer;
    private ShaderProgram shader;
    private int vertexBufferId;
    private int vertexCount;
    private int vertexStride;

    //we should convert vertices to FloatBuffer!
    float [] vertices = new float[] {
            0.37f, -0.12f, 0.0f,
            0.95f,  0.30f, 0.0f,
            0.23f,  0.30f, 0.0f,

            0.23f,  0.30f, 0.0f,
            0.00f,  0.90f, 0.0f,
            -0.23f,  0.30f, 0.0f,

            -0.23f,  0.30f, 0.0f,
            -0.95f,  0.30f, 0.0f,
            -0.37f, -0.12f, 0.0f,

            -0.37f, -0.12f, 0.0f,
            -0.57f, -0.81f, 0.0f,
            0.00f, -0.40f, 0.0f,

            0.00f, -0.40f, 0.0f,
            0.57f, -0.81f, 0.0f,
            0.37f, -0.12f, 0.0f,
    };


    public Star(Context context) {
        setupShader(context);
        setupVertexBuffer();
    }

    private void setupShader(Context context) {
        // compile & link shader
        shader = new ShaderProgram(
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.simple_vertex_shader),
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.simple_fragment_shader)
        );
    }

    private void setupVertexBuffer() {

        //store vertices vertexBuffer
        vertexBuffer = BufferUtils.newFloatBuffer(vertices.length);
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        //copy vertices from cpu to the gpu
        IntBuffer buffer = IntBuffer.allocate(1);
        GLES20.glGenBuffers(1, buffer);
        vertexBufferId = buffer.get(0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.length * 4, vertexBuffer, GLES20.GL_STATIC_DRAW);

    }

    public void draw() {
        shader.begin();

        shader.enableVertexAttribute("a_Position");
        shader.setVertexAttribute("a_Position", 3, GLES20.GL_FLOAT, false, 3*4, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length);

        shader.disableVertexAttribute("a_Position");

        shader.end();
    }
}

package opengl.study.demos._2square;

import android.content.Context;
import android.opengl.GLES20;

import com.laifeng.sopcastdemo.R;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import opengl.study.glkit.BufferUtils;
import opengl.study.glkit.ShaderProgram;
import opengl.study.glkit.ShaderUtils;

public class Point {

    private FloatBuffer vertexBuffer;
    private ShaderProgram shader;
    private int vertexBufferId;
    private int vertexCount;
    private int vertexStride;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static final float coords[] = {
            -0.5f,  0.5f, 0.0f,   //
    };

    public Point(Context context) {
        setupShader(context);
        setupVertexBuffer();
    }

    private void setupShader(Context context) {
        // compile & link shader
        shader = new ShaderProgram(
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.point_vertex_shader),
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.point_frag_shader)
        );
    }

    private void setupVertexBuffer() {
        // initialize vertex float buffer for shape coordinates
        vertexBuffer = BufferUtils.newFloatBuffer(coords.length);

        // add the coordinates to the FloatBuffer
        vertexBuffer.put(coords);

        // set the buffer to read the first coordinate
        vertexBuffer.position(0);


        //copy vertices from cpu to the gpu
        IntBuffer buffer = IntBuffer.allocate(1);
        GLES20.glGenBuffers(1, buffer);
        vertexBufferId = buffer.get(0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, coords.length * 4, vertexBuffer, GLES20.GL_STATIC_DRAW);

        vertexCount = coords.length / COORDS_PER_VERTEX;
        vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    }

    public void draw() {

        shader.begin();

        shader.enableVertexAttribute("a_Position");
        shader.setVertexAttribute("a_Position", COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, vertexCount);

        shader.disableVertexAttribute("a_Position");

        shader.end();
    }
}

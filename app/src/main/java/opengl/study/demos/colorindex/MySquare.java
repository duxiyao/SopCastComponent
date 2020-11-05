package opengl.study.demos.colorindex;

import opengl.study.demos.model.Model;
import opengl.study.glkit.ShaderProgram;

public class MySquare extends Model {

    static final float squareCoords[] = {
            -0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,// top left
            -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,// bottom left
            0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,// bottom right
            0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,// top right
    };

    static final short indices[] = {
            0, 1, 2,
            0, 2, 3,
    };

    public MySquare(ShaderProgram shader) {
        super("square", shader, squareCoords, indices);
    }
}

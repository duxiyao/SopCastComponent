package opengl.study.demos.move;

import opengl.study.demos.model.TransformModel;
import opengl.study.glkit.ShaderProgram;

public class Square extends TransformModel {


    static final float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f,// top left
            -0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f, 1.0f,// bottom left
            0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f, 1.0f,// bottom right
            0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f,// top right
    };

    static final short indices[] = {
            0, 1, 2,
            0, 2, 3,
    };

    public Square(ShaderProgram shader) {
        super("square", shader, squareCoords, indices);
    }
}

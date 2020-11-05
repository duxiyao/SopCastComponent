package opengl.study.demos._4move;

import opengl.study.demos.model.TransformationModel;
import opengl.study.glkit.ShaderProgram;

public class TransformSquare extends TransformationModel {
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

    public TransformSquare(ShaderProgram shader) {
        super("square", shader, squareCoords, indices);
    }

}

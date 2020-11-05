package opengl.study.demos.texture;

import opengl.study.demos.model.TextureModel;
import opengl.study.glkit.ShaderProgram;

public class Square extends TextureModel {

    static final float squareCoords[] = {
            1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,// top left
            1.0f,  1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,// bottom left
            -1.0f,  1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,// bottom right
            -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,// top right
    };

    static final short indices[] = {
            0, 1, 2,
            2, 3, 0
    };

    public Square(ShaderProgram shader) {
        super("square", shader, squareCoords, indices);
    }


}

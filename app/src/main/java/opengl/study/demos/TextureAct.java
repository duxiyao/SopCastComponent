package opengl.study.demos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import opengl.study.demos.texture.DiceRender;
import opengl.study.demos.texture.MaskingRender;
import opengl.study.demos.texture.OGLRenderer;
import opengl.study.demos.view.OGLView;

public class TextureAct extends AppCompatActivity {

    private OGLView oglView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oglView = new OGLView(this);
//        oglView.setRenderer(new OGLRenderer(this));
//        oglView.setRenderer(new DiceRender(this));
        oglView.setRenderer(new MaskingRender(this));
        setContentView(oglView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        oglView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        oglView.onResume();
    }

}

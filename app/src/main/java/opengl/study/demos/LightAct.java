package opengl.study.demos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import opengl.study.demos.light.DiffuseRender;
import opengl.study.demos.light.OGLRenderer;
import opengl.study.demos.light.SpecularRender;
import opengl.study.demos.texture.MaskingRender;
import opengl.study.demos.view.OGLView;

public class LightAct extends AppCompatActivity {

    private OGLView oglView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oglView = new OGLView(this);
//        oglView.setRenderer(new OGLRenderer(this));
//        oglView.setRenderer(new DiffuseRender(this));
        oglView.setRenderer(new SpecularRender(this));
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

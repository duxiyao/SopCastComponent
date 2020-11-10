package opengl.study.demos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import opengl.study.demos._7light.DiffuseRender;
import opengl.study.demos._7light.SpecularRender;
import opengl.study.demos._8framebuffer.OGLRenderer;
import opengl.study.demos.view.OGLView;

public class FBAct extends AppCompatActivity {

    private OGLView oglView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oglView = new OGLView(this);
        oglView.setRenderer(new OGLRenderer(this));
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

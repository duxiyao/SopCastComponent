package opengl.study.demos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import opengl.study.demos._4move.OGLRenderer;
import opengl.study.demos.view.OGLView;

public class ModelTransformationAct extends AppCompatActivity {

    private OGLView oglView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oglView = new OGLView(this);
//        oglView.setEGLContextClientVersion(3);
        oglView.setRenderer(new OGLRenderer(this));
//        oglView.setRenderer(new MoveRender(this));
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
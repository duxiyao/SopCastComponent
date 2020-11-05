package opengl.study.demos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import opengl.study.demos.hello.OGLRenderer;
import opengl.study.demos.hello.RedAlertOGLRenderer;
import opengl.study.demos.view.OGLView;

public class HelloAct extends AppCompatActivity {

    private OGLView oglView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oglView = new OGLView(this);
//        oglView.setRenderer(new OGLRenderer());
        oglView.setRenderer(new RedAlertOGLRenderer());
        ViewGroup.LayoutParams layoutParams = oglView.getLayoutParams();
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

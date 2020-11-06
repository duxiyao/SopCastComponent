package opengl.study;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.laifeng.sopcastdemo.LandscapeActivity;
import com.laifeng.sopcastdemo.MainActivity;
import com.laifeng.sopcastdemo.PartActivity;
import com.laifeng.sopcastdemo.PortraitActivity;
import com.laifeng.sopcastdemo.R;
import com.laifeng.sopcastdemo.ScreenActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 先看些基础文章https://www.jianshu.com/p/89b5609d1c16等
 * https://blog.csdn.net/u013795543/article/details/90744547  基本变量
 * https://blog.csdn.net/flycatdeng/article/details/82588903  api
 * https://blog.csdn.net/q1398284020/article/details/80181803 内置变量 函数
 * https://blog.csdn.net/u014587123/article/details/80626652 参考
 *
 */
public class DemosActivity extends ListActivity {

    public static final String TAG = "Grafika";

    // map keys
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String CLASS_NAME = "class_name";

    /**
     * Each entry has three strings: the test title, the test description, and the name of
     * the activity class.
     */
    private static final String[][] TESTS = {
            {"1hello",
                    "hello",
                    "HelloAct"},
            {"2square",
                    "square",
                    "SquareAct"},
            {"3square",
                    "ColorIndexSquare",
                    "ColorIndexSquareAct"},
            {"4square",
                    "ModelTransformation",
                    "ModelTransformationAct"},
            {"5square",
                    "Projection",
                    "ProjectionAct"},
            {"6Texture",
                    "Texture",
                    "TextureAct"},
            {"7Light",
                    "Light",
                    "LightAct"},
    };

    /**
     * Compares two list items.
     */
    private static final Comparator<Map<String, Object>> TEST_LIST_COMPARATOR =
            new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> map1, Map<String, Object> map2) {
                    String title1 = (String) map1.get(TITLE);
                    String title2 = (String) map2.get(TITLE);
                    return title1.compareTo(title2);
                }
            };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl);

        // One-time singleton initialization; requires activity context to get file location.
        ContentManager.initialize(this);

        setListAdapter(new SimpleAdapter(this, createActivityList(),
                android.R.layout.two_line_list_item, new String[]{TITLE, DESCRIPTION},
                new int[]{android.R.id.text1, android.R.id.text2}));

        ContentManager cm = ContentManager.getInstance();
        if (!cm.isContentCreated(this)) {
            ContentManager.getInstance().createAll(this);
        }
    }

    /**
     * Creates the list of activities from the string arrays.
     */
    private List<Map<String, Object>> createActivityList() {
        List<Map<String, Object>> testList = new ArrayList<Map<String, Object>>();

        for (String[] test : TESTS) {
            Map<String, Object> tmp = new HashMap<String, Object>();
            tmp.put(TITLE, test[0]);
            tmp.put(DESCRIPTION, test[1]);
            Intent intent = new Intent();
            // Do the class name resolution here, so we crash up front rather than when the
            // activity list item is selected if the class name is wrong.
            try {
                Class cls = Class.forName("opengl.study.demos." + test[2]);
                intent.setClass(this, cls);
                tmp.put(CLASS_NAME, intent);
            } catch (ClassNotFoundException cnfe) {
                throw new RuntimeException("Unable to find " + test[2], cnfe);
            }
            testList.add(tmp);
        }

        Collections.sort(testList, TEST_LIST_COMPARATOR);

        return testList;
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) listView.getItemAtPosition(position);
        Intent intent = (Intent) map.get(CLASS_NAME);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * onClick handler for "about" menu item.
     */
    public void clickAbout(@SuppressWarnings("unused") MenuItem unused) {

    }

    /**
     * onClick handler for "regenerate content" menu item.
     */
    public void clickRegenerateContent(@SuppressWarnings("unused") MenuItem unused) {
        ContentManager.getInstance().createAll(this);
    }

}

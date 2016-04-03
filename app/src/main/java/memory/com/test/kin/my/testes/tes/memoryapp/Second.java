package memory.com.test.kin.my.testes.tes.memoryapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by maki on 2016/04/02.
 * MemoryApp.
 */
public class Second extends Activity {
    private Button button;
    private DebugFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();

//        // FragmentTransactionを作成
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new DebugFragment();
//        // add()の第一引数は、Fragmentを挿入する部分のレイアウトファイルのid、第二引数は挿入するFragmen
//        fragmentTransaction.add(R.id.debugFragment, fragment);
//        fragmentTransaction.commit();
    }

    private void setLayout() {
        // ステータスバー非表示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // タイトルバー非表示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_second_activitys);

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivitys.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        LayoutUtil.cleanupView(findViewById(R.id.second_cotent));
        super.onDestroy();
    }
}

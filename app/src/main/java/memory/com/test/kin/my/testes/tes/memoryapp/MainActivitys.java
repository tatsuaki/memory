package memory.com.test.kin.my.testes.tes.memoryapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import memory.com.test.kin.my.testes.tes.memoryapp.Debug.DebugFragment;
import memory.com.test.kin.my.testes.tes.memoryapp.Debug.LayoutUtil;

public class MainActivitys extends AppCompatActivity {
    private static final String TAG = "MainActivitys";
    private DebugFragment debugFragment;
    private boolean showDebugFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ステータスバー非表示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // タイトルバー非表示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_activitys);

        Button button = (Button) findViewById(R.id.button);
        if (null != button) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Second.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        Button debug_main = (Button) findViewById(R.id.debug_main);
        if (null != debug_main) {
            debug_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "onClick showDebugFragment = " + showDebugFragment);
                    if (showDebugFragment) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.hide(debugFragment);
                        fragmentTransaction.commitAllowingStateLoss();
                        showDebugFragment = false;
                    } else {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if (null == debugFragment) {
                            Log.v(TAG, "new debugFragment");
                            debugFragment = new DebugFragment();
                            fragmentTransaction.add(R.id.container, debugFragment, "debugFragment");
                            fragmentTransaction.addToBackStack(null);
                        } else {
                            fragmentTransaction.show(debugFragment);
                        }
                        fragmentTransaction.commitAllowingStateLoss();
                        showDebugFragment = true;
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        LayoutUtil.cleanupView(findViewById(R.id.topBase));
        super.onDestroy();
    }
}
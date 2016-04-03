package memory.com.test.kin.my.testes.tes.memoryapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivitys extends AppCompatActivity {
    private DebugFragment debugFragment;
    private boolean showDebugFragment;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ステータスバー非表示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // タイトルバー非表示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_activitys);

        mFragmentTransaction = getFragmentManager().beginTransaction();
        debugFragment = new DebugFragment();
        mFragmentTransaction.add(R.id.debugFragment, debugFragment, "debugFragment");
        mFragmentTransaction.commit();

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
                    if (showDebugFragment) {
                        if (null == mFragmentTransaction) {
                            mFragmentTransaction = getFragmentManager().beginTransaction();
                        }
//                        if (null == debugFragment) {
//                            debugFragment = new DebugFragment();
//                        }
                        mFragmentTransaction.hide(debugFragment);
                        mFragmentTransaction.commit();
                        showDebugFragment = false;
                    } else {
                        if (null == mFragmentTransaction) {
                            mFragmentTransaction = getFragmentManager().beginTransaction();
                        }
//                        if (null == debugFragment) {
//                            debugFragment = new DebugFragment();
//                        }
                        mFragmentTransaction.show(debugFragment);
                        mFragmentTransaction.commit();
                        showDebugFragment = true;
                    }
                }
            });
        }
//        String packageName = BuildConfig.APPLICATION_ID;
//        Toast.makeText(this.getApplicationContext(), packageName, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        LayoutUtil.cleanupView(findViewById(R.id.topBase));
        super.onDestroy();
    }
}
package memory.com.test.kin.my.testes.tes.memoryapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import memory.com.test.kin.my.testes.tes.memoryapp.Debug.DebugFragment;
import memory.com.test.kin.my.testes.tes.memoryapp.Debug.LayoutUtil;

public class Second extends Activity {
    private static final String TAG = "Second";
    private DebugFragment debugFragment;
    private boolean showDebugFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();
    }

    private void setLayout() {
        Log.v(TAG, "setLayout");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_second_activitys);

        Button button = (Button) findViewById(R.id.to_top);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivitys.class);
                startActivity(intent);
                finish();
            }
        });
        Button scond_ = (Button) findViewById(R.id.scond_);
        scond_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chekss();
                Log.v(TAG, "onClick = " + showDebugFragment);
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
                        fragmentTransaction.add(R.id.container_second, debugFragment, "debugFragment");
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

    public void chekss() {
        Log.d("build","MANUFACTURER:" + Build.MANUFACTURER);
        Log.d("build","MODEL:" + Build.MODEL);
        Log.d("build","VERSION.RELEASE:" + Build.VERSION.RELEASE);
        Log.d("build","VERSION.SDK_INT:" + Build.VERSION.SDK_INT);
    }

    @Override
    protected void onDestroy() {
        LayoutUtil.cleanupView(findViewById(R.id.second_cotent));
        super.onDestroy();
    }
}

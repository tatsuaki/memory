package memory.com.test.kin.my.testes.tes.memoryapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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
        boolean DEVELOPER_MODE = true;
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
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
                    LayoutUtil.cleanupView(findViewById(R.id.debug_content));
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
        Button debug_main2 = (Button) findViewById(R.id.debug_main2);
        if (null != debug_main2) {
            debug_main2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), GooleAuthActivity.class);
                    startActivity(intent);
                    //  getAccounts();
                }
            });
        }
    }

    //    private void downloadFromInternet() {
//
//        String url = "https://sites.google.com/site/yukianzm/tmp/image1.png";
//
//        try {
//            Bitmap bmp = null;
//
//            final DefaultHttpClient httpClient = new DefaultHttpClient();
//
//            HttpGet hg = new HttpGet(url);
//            HttpResponse httpResponse = httpClient.execute(hg);
//            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                bmp = BitmapFactory.decodeStream(httpResponse.getEntity().getContent());
//                hg.abort();
//            }
//        }
//        catch (ClientProtocolException e) {}
//        catch (IOException e) {}
//    }
    @Override
    protected void onDestroy() {
        LayoutUtil.cleanupView(findViewById(R.id.topBase));
        super.onDestroy();
    }
}
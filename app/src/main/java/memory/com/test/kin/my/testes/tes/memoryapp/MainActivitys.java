package memory.com.test.kin.my.testes.tes.memoryapp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.params.CoreProtocolPNames;

import java.util.ArrayList;

import memory.com.test.kin.my.testes.tes.memoryapp.Debug.DebugFragment;
import memory.com.test.kin.my.testes.tes.memoryapp.Debug.LayoutUtil;
import memory.com.test.kin.my.testes.tes.memoryapp.sync.SyncActivity;

public class MainActivitys extends Activity implements ActionBar.TabListener {

    private static final String TAG = "MainActivitys";
    private DebugFragment debugFragment;
    private boolean showDebugFragment;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean DEVELOPER_MODE = true;
        //noinspection ConstantConditions
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

        Thread.setDefaultUncaughtExceptionHandler(new CsUncaughtExceptionHandler(null));
        // ステータスバー非表示
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        // タイトルバー非表示
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main_activitys);
        @SuppressWarnings("deprecation") String su = CoreProtocolPNames.USER_AGENT;
        Log.v(TAG, "CoreProtocolPNames.USER_AGENT = " + su);
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
        Button errorButton = (Button) findViewById(R.id.errorButton);
        if (null != errorButton) {
            errorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    errors();
                }
            });
        }
        Button syncButton = (Button) findViewById(R.id.syncButton);
        if (null != syncButton) {
            syncButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SyncActivity.class);
                    startActivity(intent);
                }
            });
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //  client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void errors() {
        String[] ss = new String[2];
        ss[5].toString();
    }

    @Override
    protected void onDestroy() {
        LayoutUtil.cleanupView(findViewById(R.id.topBase));
        super.onDestroy();
    }

    @SuppressLint("AlwaysShowAction")
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
//            // メニューの要素を追加して取得
//         // MenuItem actionItem = menu.add("Action Button Help Icon");
//            // アイコンを設定
//            actionItem.setIcon(android.R.drawable.ic_menu_help);
//            action bar を取得する
//            final ActionBar mActionBar = getActionBar();
//
//            // ActionBarにタブを表示する
//            // このままでは表示されない
////        mActionBar.setCustomView();
////        mActionBar.addTab(mActionBar.newTab().setText("Tab 1").setTabListener(this));
////        mActionBar.addTab(mActionBar.newTab().setText("Tab 2").setTabListener(this));
////        mActionBar.addTab(mActionBar.newTab().setText("Tab 3").setTabListener(this));
//            // SHOW_AS_ACTION_ALWAYS:常に表示
//            actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//
//            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//            client.connect();
//            Action viewAction = Action.newAction(
//                    Action.TYPE_VIEW, // TODO: choose an action type.
//                    "MainActivitys Page", // TODO: Define a title for the content shown.
//                    // TODO: If you have web page content that matches this app activity's content,
//                    // make sure this auto-generated web page URL is correct.
//                    // Otherwise, set the URL to null.
//                    Uri.parse("http://host/path"),
//                    // TODO: Make sure this auto-generated app URL is correct.
//                    Uri.parse("android-app://memory.com.test.kin.my.testes.tes.memoryapp/http/host/path")
//            );
//            AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

//            // ATTENTION: This was auto-generated to implement the App Indexing API.
//            // See https://g.co/AppIndexing/AndroidStudio for more information.
//            Action viewAction = Action.newAction(
//                    Action.TYPE_VIEW, // TODO: choose an action type.
//                    "MainActivitys Page", // TODO: Define a title for the content shown.
//                    // TODO: If you have web page content that matches this app activity's content,
//                    // make sure this auto-generated web page URL is correct.
//                    // Otherwise, set the URL to null.
//                    Uri.parse("http://host/path"),
//                    // TODO: Make sure this auto-generated app URL is correct.
//                    Uri.parse("android-app://memory.com.test.kin.my.testes.tes.memoryapp/http/host/path")
//            );
//            AppIndex.AppIndexApi.end(client, viewAction);
//            client.disconnect();
    }
}
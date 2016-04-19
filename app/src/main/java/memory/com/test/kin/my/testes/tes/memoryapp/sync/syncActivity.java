package memory.com.test.kin.my.testes.tes.memoryapp.sync;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.vending.billing.IInAppBillingService;

import memory.com.test.kin.my.testes.tes.memoryapp.Billing.BillingConfig;
import memory.com.test.kin.my.testes.tes.memoryapp.Billing.util.IabHelper;
import memory.com.test.kin.my.testes.tes.memoryapp.Billing.util.IabResult;
import memory.com.test.kin.my.testes.tes.memoryapp.Billing.util.Inventory;
import memory.com.test.kin.my.testes.tes.memoryapp.Billing.util.Purchase;
import memory.com.test.kin.my.testes.tes.memoryapp.Debug.LayoutUtil;
import memory.com.test.kin.my.testes.tes.memoryapp.MainActivitys;
import memory.com.test.kin.my.testes.tes.memoryapp.R;

// メイン処理。
public class SyncActivity extends Activity {
    private static final String TAG = SyncActivity.class.getSimpleName();
    public IInAppBillingService billingService;
    public ServiceConnection mServiceConn;

    Activity activity;
    // The helper object
    IabHelper mHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sync);

        activity = this;
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, BillingConfig.KEY);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
            //  mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
//        mServiceConn = new ServiceConnection() {
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                billingService = null;
//            }
//
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                billingService = IInAppBillingService.Stub.asInterface(service);
//            }
//        };

    //  bindService(new Intent("com.android.vending.billing.InAppBillingService.BIND"),
    //            mServiceConn, Context.BIND_AUTO_CREATE);
//        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
//        intent.setPackage("com.android.vending");
//        bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);

        // 非同期タスクたちを順番に実行させる。
        new SgpBillingTaskRunner(this, mHelper, new SgpBillingProcessTask[]{
                // 下記に，逐次実行したい非同期タスクを列挙する。
                // １番目の非同期タスク
                new SgpPfListGet() {
                },
                // ２番目の非同期タスク
                new SgpGoogleListGet() {
                    public boolean execTask() {
                        mHelper.queryInventoryAsync(mGotInventoryListener);
//                        log("前の非同期タスクの実行結果は" + getDataFromRunner("sum2"));
//                        log("前の前の非同期タスクの実行結果は" + getDataFromRunner("sum1"));
//                        log("webに返却、またはListViewに表示");
                        return true;
                    }
                },
                // ３番目の非同期タスク
                new SgpToWebShow() {
                    public boolean execTask() {
                        log("前の非同期タスクの実行結果は" + getDataFromRunner("sum2"));
                        log("前の前の非同期タスクの実行結果は" + getDataFromRunner("sum1"));
                        log("webに返却、またはListViewに表示");
                        return true;
                    }
                }
        }).taskRun();
        System.out.println("main()が終了。");

        Button button = (Button) findViewById(R.id.to_top);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivitys.class);
                startActivity(intent);
                LayoutUtil.cleanupView(findViewById(R.id.debug_content));
                finish();
            }
        });
    }

    @Override
    public void onDestroy()  {
        super.onDestroy();
        // 課金サービスを終了
        if(billingService != null){
            unbindService(mServiceConn);
        }
    }

    void complain(String message) {
        Log.e(TAG, "**** Error: " + message);
        alert("Error: " + message);
    }
    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    public IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            // Do we have the premium upgrade?
            Purchase item1 = inventory.getPurchase(BillingConfig.ITEM1);

//            if (item1 != null && verifyDeveloperPayload(item1)) {
//                Log.d(TAG, "We have gas. Consuming it.");
//                mHelper.consumeAsync(inventory.getPurchase(BillingConfig.ITEM1), mConsumeFinishedListener);
//                return;
//            }

//            updateUi();
//            setWaitScreen(false);
            if ( null != item1 ) {
                Log.d(TAG, "item1 = " + item1.toString());
            }
            String payload = "";
            mHelper.launchPurchaseFlow(activity, BillingConfig.ITEM1, 3, mPurchaseFinishedListener, payload);
        }
    };

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }
            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(BillingConfig.ITEM1)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
            }
            else {
                complain("Error while consuming: " + result);
            }
            Log.d(TAG, "End consumption flow.");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }
}
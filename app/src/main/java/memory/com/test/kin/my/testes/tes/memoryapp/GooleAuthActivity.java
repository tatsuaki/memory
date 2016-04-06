package memory.com.test.kin.my.testes.tes.memoryapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by maki on 2016/04/06.
 * MemoryApp.
 */
public class GooleAuthActivity extends Activity {
    private AccountManager accountManager;
    private String accountName;
    private String authToken;
    private String authTokenType;
    private static final String AUTH_TOKEN_TYPE_PROFILE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    //  private static final String AUTH_TOKEN_TYPE_ADSENSE = "oauth2:https://www.googleapis.com/auth/adsense.readonly";
    private static final String ACCOUNT_TYPE = "com.google";
    private static final int REQUEST_CODE_AUTH = 0;
    private ProgressDialog dialog;
    private static final String API_KEY = "USE_YOUR_API_KEY";
    private static final String KEY_AUTH_ERROR = "\"code\":401";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager_oauth2);
        accountManager = AccountManager.get(this);
        dialog = new ProgressDialog(this);
    }


    private void startRequest(String authTokenType) {
        Log.v("startRequest", "リクエスト開始 - リクエスト先:" + authTokenType);
        this.authTokenType = authTokenType;
        if (accountName == null) {
            Log.v("startRequest", "アカウントが選択されていない");
            chooseAccount();
        } else {
            getAuthToken();
        }
    }

    private void chooseAccount() {
        Log.v("chooseAccount", "AuthToken取得開始（アカウント選択）");
        accountManager.getAuthTokenByFeatures(ACCOUNT_TYPE, authTokenType, null, GooleAuthActivity.this, null, null,
                new AccountManagerCallback<Bundle>() {
                    public void run(AccountManagerFuture<Bundle> future) {
                        onGetAuthToken(future);
                    }
                },
                null);
    }

    private void getAuthToken() {
        Account account = null;
        Account[] accounts = accountManager.getAccounts();
        for (int i = 0; i < accounts.length; i++) {
            account = accounts[i];
            if (account.name.equals(accountName)) {
                break;
            }
        }
        if (account == null) {
            Log.v("getAuthToken", "アカウントが削除されている");
            chooseAccount();
            return;
        }
        Log.v("getAuthToken", "AuthToken取得開始");
        // getAuthToken(Account, String, boolean, AccountManagerCallback<Bundle>, Handler) method is deprecated.
        // getAuthToken(Account, String, Bundle, Activity, AccountManagerCallback<Bundle>, Handler)
        // or
        // getAuthToken(Account, String, Bundle, boolean, AccountManagerCallback<Bundle>, Handler)
        accountManager.getAuthToken(account, authTokenType, null, this,
                new AccountManagerCallback<Bundle>() {
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle bundle = future.getResult();
                            if (bundle.containsKey(AccountManager.KEY_INTENT)) {
                                //まだAPIアクセス許可が出ていない場合にgetAuthToken()すると
                                //BundleにKEY_INTENTが含まれる。この場合AuthTokenはNULLとなる。
                                Log.v("getAuthToken", "アクセス許可画面へ");
                                Intent intent = bundle.getParcelable(AccountManager.KEY_INTENT);
                                //「FLAG_ACTIVITY_NEW_TASK」の前の「~」はビット反転演算子
                                //これをしないとアクセス許可画面でのボタンクリックを待たずにonActivityResult()が呼ばれてしまう
                                intent.setFlags(intent.getFlags() & ~Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivityForResult(intent, REQUEST_CODE_AUTH);
                            } else if (bundle.containsKey(AccountManager.KEY_AUTHTOKEN)) {
                                onGetAuthToken(future);
                            }
                        } catch (Exception e) {
                            Log.v("getAuthToken", "AuthToken取得失敗", e);
                        }
                    }
                },
                null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("onActivityResult", "requestCode=" + requestCode + " resultCode=" + resultCode);
        switch (requestCode) {
            case REQUEST_CODE_AUTH:
                if (resultCode == RESULT_OK) {
                    getAuthToken();
                } else {
                    Log.v("onActivityResult", "アクセス許可画面で拒否された");
                }
                break;
        }
    }

    private void onGetAuthToken(AccountManagerFuture<Bundle> future) {
        try {
            Bundle bundle = future.getResult();
            accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
            authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
            if (authToken == null) {
                throw new Exception("authTokenがNULL accountName=" + accountName);
            }
            Log.v("onGetAuthToken", "AuthToken取得完了 accountName=" + accountName + " authToken=" + authToken + " authTokenType=" + authTokenType);
            if (authTokenType.equals(AUTH_TOKEN_TYPE_PROFILE)) {
                getUserInfo(); //ユーザー情報取得開始
            }
        } catch (OperationCanceledException e) {
            Log.v("onGetAuthToken", "AuthToken取得キャンセル");
        } catch (Exception e) {
            Log.v("onGetAuthToken", "AuthToken取得失敗", e);
        }
    }


    public void onClickBtnUserInfo(View view) {
        Log.v("onClickBtnUserInfo", "ユーザー情報取得ボタンクリック");
        startRequest(AUTH_TOKEN_TYPE_PROFILE);
    }

    private void getUserInfo() {
        Log.v("getUserInfo", "ユーザー情報取得開始");
        String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + authToken + "&key=" + API_KEY;
        AsyncTaskGetJson task = new AsyncTaskGetJson();
        task.setListener(new OnResultEventListener() {
            @Override
            public void onResult(JSONObject json) {
                TextView tv = (TextView) findViewById(R.id.textView);
                String msg = "";
                if (json == null) {
                    msg = "ユーザー情報取得失敗";
                    dialog.dismiss();
                } else if (json.toString().contains(KEY_AUTH_ERROR)) {
                    msg = "ユーザー情報取得失敗（認証エラー）";
                    accountManager.invalidateAuthToken(ACCOUNT_TYPE, authToken);
                    Log.v("getUserInfo", msg + " AuthTokenを破棄して再取得");
                    startRequest(AUTH_TOKEN_TYPE_PROFILE);
                } else {
                    msg = "ユーザー情報取得成功\njson=" + json.toString();
                    Log.v("getUserInfo", msg);
                    dialog.dismiss();
                }
                tv.setText(msg);
            }
        });
        task.execute(url);
        dialog.setMessage("ユーザー情報取得中");
        dialog.show();
    }

    public void onClickBtnAdSense(View view) {
        Log.v("onClickBtnAdSense", "AdSenseレポート取得ボタンクリック");
    //  startRequest(AUTH_TOKEN_TYPE_ADSENSE);
    }

    public class AsyncTaskGetJson extends AsyncTask<String, Void, JSONObject> {
        protected DefaultHttpClient client;
        protected OnResultEventListener listener;

        @Override
        protected void onPreExecute() {
            //Log.v("onPreExecute", "JSON取得開始");
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            JSONObject json = null;
            String url = urls[0];
            client = new DefaultHttpClient();
            try {
                HttpGet httpGet = new HttpGet(url);
                HttpResponse res = client.execute(httpGet);
                HttpEntity entity = res.getEntity();
                String result = EntityUtils.toString(entity);
                //Log.v("doInBackground", "result=" + result);
                json = new JSONObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //Log.v("doInBackground", "リクエスト切断");
                client.getConnectionManager().shutdown();
            }
            return json;
        }


        @Override
        protected void onPostExecute(JSONObject json) {
            if (json == null) {
                //Log.v("onPostExecute", "JSON取得失敗 JSONがNULL");
            } else {
                //Log.v("onPostExecute", "JSON取得成功");
            }
            if (listener != null) {
                listener.onResult(json);
            }
        }

        @Override
        protected void onCancelled() {
            //Log.v("onCancelled", "JSON取得キャンセル");
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
            super.onCancelled();
        }

        public void setListener(OnResultEventListener listener) {
            this.listener = listener;
        }
    } // END class TaskGetJson

    public interface OnResultEventListener {
        public void onResult(JSONObject json);
    } // END interface EventListener
}

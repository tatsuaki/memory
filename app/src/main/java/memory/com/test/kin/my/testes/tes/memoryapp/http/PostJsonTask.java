package memory.com.test.kin.my.testes.tes.memoryapp.http;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by maki on 2016/04/07.
 * MemoryApp.
 */
@SuppressWarnings("JavaDoc")
class PostJsonTask extends AsyncTask<String, Void, JSONObject> {
    private static final String TAG = PostJsonTask.class.getSimpleName();
    @SuppressWarnings("FieldCanBeLocal")
    private final String REQUEST = "POST";
    private final String mUrlString;

    @SuppressWarnings("unused")
    private PostJsonTask(String url) {
        mUrlString = url;
    }

    @Override
    protected void onPreExecute() {
        //Log.v("onPreExecute", "JSON取得開始");
    }

    /**
     * URL, UA, postData
     * @param params
     * @return
     */
    @SuppressWarnings("JavaDoc")
    @Override
    protected JSONObject doInBackground(String... params) {
        HttpURLConnection conn = ConnectionFactory.createConnection(mUrlString);
        conn = ConnectionFactory.setConnection(conn, REQUEST);

        return doPost(conn, params[0]);
    }

    @Override
    protected void onPostExecute(JSONObject json) {

    }

    /**
     * API <　19	libcore.net.http.HttpURLConnectionImpl
     * API　>=19	com.android.okhttp.internal.http.HttpURLConnectionImpl
     */
    private JSONObject doPost(HttpURLConnection conn, String param) {
//        String url = "http://xxx.xxx.xxx.xxx";
//        requestJSON = "JSON文字列";
//        HttpURLConnection conn = createConnection(url);
//        conn = setConnection(conn, "POST");

        JSONObject json = null;
        // タイムアウト設定
//		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 3000);
        conn.setFixedLengthStreamingMode(param.getBytes().length);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//      // 独自ヘッダ付与
//      ArrayList<BasicHeader> arrHeader = new ArrayList<BasicHeader>();
//      arrHeader.add(new BasicHeader(mConfig.getHeaderName(), SgpUtility.getUserAgent(mContext)));

        // 確認用
        Map<String, List<String>> settings = conn.getRequestProperties();
        Log.i("OSA030", "doPost start.:" + settings);

        try {
            //POST用のOutputStreamを取得
            OutputStream os = conn.getOutputStream();
            // リクエストデータ整形
            String postStr = "foo1=bar1&foo2=bar2";//POSTするデータ
            PrintStream ps = new PrintStream(os);
            ps.print(postStr);//データをPOSTする
            ps.close();

            int status;
            status = conn.getResponseCode();
            Log.d(TAG, "status: " + status);
            if (status == 200) {
                String data = ConnectionFactory.InputStreamToString(conn.getInputStream());
                data = data.replaceAll("\n", "");
                if (data.trim().length() == 0) {
                    json = new JSONObject();
                } else {
                    json = new JSONObject(data);
                }
            } else {
                json = new JSONObject();
            }
            json.put("status", status);
            Log.d(TAG, "json: " + json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException ignored) {
        } finally {
            conn.disconnect();
        }
        return json;
    }
}

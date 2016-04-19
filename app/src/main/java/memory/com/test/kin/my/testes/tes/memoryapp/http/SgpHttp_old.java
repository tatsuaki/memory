package memory.com.test.kin.my.testes.tes.memoryapp.http;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
/**
 * Created by maki on 2016/04/06.
 * MemoryApp.
 */
@SuppressWarnings({"ALL", "deprecation"})
class SgpHttp_old {
    /**
     * ログ出力用タグ
     */
    private static final String TAG = "SpgHttp";
    private final String UA_DUMMY = "sample_UA";
//    private SgpConfig mConfig = null;
//    private SgpAppBean mBean = null;
    private Context mContext = null;
    private Context mConfig;
    /**
     * リスナー
     */
    private OnPostFinishedListener mPostListener = null;
    /**
     * POST送信完了後処理リスナー
     */
    public interface OnPostFinishedListener {
        /**
         * POST送信完了後の処理
         * @param jsonObject JSONオブジェクト
         */
        void onPostFinished(JSONObject jsonObject);
    }
    /**
     * リスナー
     */
    private OnDownloadFinishedListener mDownloadFinished = null;
    /**
     * ダウンロード完了後処理リスナー
     */
    public interface OnDownloadFinishedListener {
        /**
         * ダウンロード完了後の処理
         * @param filePath ファイルパス
         */
        void onDownloadFinished(String filePath);
    }
    /**
     * リスナー
     */
    private OnGetFinishedListener mGetListener = null;
    /**
     * GET送信完了後処理リスナー
     */
    public interface OnGetFinishedListener {
        /**
         * GET送信完了後の処理
         * @param status ステータス
         * @param data データ
         */
        void onGetFinished(int status, String data);
    }
    /**
     * リスナー
     */
    private OnGetImageFinishedListener mGetImageListener = null;
    /**
     * GET送信完了後処理リスナー
     */
    public interface OnGetImageFinishedListener {
        void onFinished(Bitmap bitmap);
    }

    /**
     * コンストラクタ
     * @param context Context
     */
    public SgpHttp_old(Context context) {
        mContext = context;
//        mBean = SgpAppBean.getBean(mContext);
//        mConfig = mBean.getConfig();
    }
    /**
     * POST送信
     * @param listener POST送信後処理リスナー
     * @param url URL
     * @param params パラメータ
     */
    public void post(final OnPostFinishedListener listener, String url, ArrayList<NameValuePair> params) {
        mPostListener = listener;
        HttpPost request = new HttpPost(url);
        Log.i(TAG, String.format("url: %s", url));
        if (params != null) {
            try {
                request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                Log.i(TAG, String.format("data: %s", params.toString()));
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        HttpPostTask task = new HttpPostTask();
        task.execute(request);
    }
    /**
     * POST通信タスク
     */
    private class HttpPostTask extends AsyncTask<HttpUriRequest, Void, JSONObject> {
        /* (非 Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         * バックグラウンド処理
         */
        @SuppressLint("DefaultLocale")
        protected JSONObject doInBackground(HttpUriRequest... request) {
//			AndroidHttpClient httpClient = AndroidHttpClient.newInstance(mContext.getPackageName());
            AndroidHttpClient httpClient = AndroidHttpClient.newInstance(System.getProperty("http.agent"));
            HttpResponse response;
            JSONObject jo;
            try {
                // UserAgent変更
                httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, UA_DUMMY);
                // 独自ヘッダ付与
                ArrayList<BasicHeader> arrHeader = new ArrayList<>();
                arrHeader.add(new BasicHeader("mConfig.getHeaderName()", UA_DUMMY));
                httpClient.getParams().setParameter("http.default-headers", arrHeader);
                // タイムアウト設定
//				HttpConnectionParams.setSoTimeout(httpClient.getParams(), 3000);
                response = httpClient.execute(request[0]);
                int status = 0;
                try {
                    status = response.getStatusLine().getStatusCode();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                Log.d(TAG, "status: " + status);
                try {
                    String data = EntityUtils.toString(response.getEntity(), "UTF-8");
                    if (data == null) {
                        data = "";
                    }
                    data = data.replaceAll("\n", "");
                    if (data.trim().length() == 0) {
                        jo = new JSONObject();
                    } else {
                        jo = new JSONObject(data);
                    }
                    jo.put("status", status);
                    return jo;
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                if (httpClient != null) {
                    httpClient.close();
                }
            }
            return null;
        }
        /* (非 Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         * バックグラウンド処理完了時の処理
         */
        protected void onPostExecute(JSONObject jsonObject) {
            if (mPostListener != null) {
                mPostListener.onPostFinished(jsonObject);
            }
        }
    }
    /**
     * POST送信(JSON)
     * @param listener POST送信後処理リスナー
     * @param url URL
     * @param json JSONオブジェクト
     */
    public void postJson(final OnPostFinishedListener listener, String url, JSONObject json) {
        mPostListener = listener;
        HttpPostJsonTask task = new HttpPostJsonTask();
        task.execute(url, json.toString());
    }
    /**
     * POST送信(JSON)
     * @param listener POST送信後処理リスナー
     * @param url URL
     * @param json JSON文字列
     */
    public void postJson(final OnPostFinishedListener listener, String url, String json) {
        mPostListener = listener;
        HttpPostJsonTask task = new HttpPostJsonTask();
        task.execute(url, json);
    }
    /**
     * POST通信タスク(JSON)
     */
    private class HttpPostJsonTask extends AsyncTask<String, Void, JSONObject> {
        /* (非 Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         * バックグラウンド処理
         */
        @SuppressLint("DefaultLocale")
        protected JSONObject doInBackground(String... param) {
            JSONObject jo;
            StringBuffer sb;
            try {
                URL oUrl = new URL(param[0]);
                Log.d(TAG, String.format("url: %s", oUrl.toString()));
                String data = param[1];
                Log.d(TAG, String.format("data: %s", data));
                HttpURLConnection con = (HttpURLConnection)oUrl.openConnection();
                // タイムアウト設定
//				con.setReadTimeout(3000);
//				con.setConnectTimeout(3000);
                // UserAgent変更
/*
				String ua = SgpUtility.getUserAgent(mContext);
				try {
					con.setRequestProperty("User-Agent", ua);
				} catch (Exception e) {
					e.printStackTrace();
				}
*/
                // 独自ヘッダ付与
                con.setRequestProperty("mConfig.getHeaderName()", UA_DUMMY);
                con.setRequestMethod("POST");
                if (data != null) {
                    con.setDoOutput(true);
                    PrintStream ps = new PrintStream(con.getOutputStream());
//					ps.write(data.getBytes("UTF-8"));
                    ps.print(data);
                    ps.close();
                }
                int status = 0;
                try {
                    status = con.getResponseCode();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                Log.d(TAG, "status: " + status);
                String ss = String.format("%s", status);
                if (ss.charAt(0) == '2') {
                    Log.d(TAG, String.format("status: %s", status));
                    InputStream in = con.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    sb = new StringBuffer();
                    String line;
                    // InputStreamからのデータを文字列として取得する
                    while((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    data = sb.toString();
                    try {
                        data = data.replaceAll("\n", "");
                        if (data.trim().length() == 0) {
                            jo = new JSONObject();
                        } else {
                            jo = new JSONObject(data);
                        }
                        jo.put("status", status);
                        Log.d(TAG, "json: " + jo.toString());
                        return jo;
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                } else {
                    jo = new JSONObject();
                    jo.put("status", status);
                    return jo;
                }
            } catch (IOException | JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }
        /* (非 Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         * バックグラウンド処理完了時の処理
         */
        protected void onPostExecute(JSONObject jsonObject) {
            if (mPostListener != null) {
                mPostListener.onPostFinished(jsonObject);
            }
        }
    }

    /**
     * ダウンロード
     * @param listener ダウンロード完了後処理リスナー
     * @param url ダウンロード対象URL
     * @param name 保存先
     */
    public void download(final OnDownloadFinishedListener listener, String url, String name) {
        mDownloadFinished = listener;
        HttpDownloadTask task = new HttpDownloadTask();
        task.execute(url, name);
    }
    /**
     * POST通信タスク(JSON)
     */
    private class HttpDownloadTask extends AsyncTask<String, Void, String> {
        @SuppressLint("DefaultLocale")
        protected String doInBackground(String... param) {
            String rights;
            URL url;
            String name;
            InputStream istream;
            OutputStream ostream;
            File file;
            String path;
            Bitmap srcBmp = null;
            Bitmap tmpBmp = null;
            Bitmap bmp = null;
            try {
                url = new URL(param[0]);
                name = param[1];
                istream = url.openStream();
                srcBmp = BitmapFactory.decodeStream(istream);
                if (srcBmp == null) {
                    return null;
                }
                //noinspection ConstantIfStatement
                if (true) {
            //  if (mConfig.getEmbedInvitationCodeOfSaveImage()) {
                    // 招待コードの埋め込み
                //  tmpBmp = SgpUtility.drawTextOnBitmap(srcBmp, mBean.getInvitationCode(), Gravity.TOP, 32);
                    tmpBmp = Bitmap.createBitmap(null);
                    try {
                        srcBmp.recycle();
                    } catch (Exception e) {
                    }
                    srcBmp = null;
                } else {
                    tmpBmp = srcBmp;
                }
                //noinspection ConstantIfStatement
                if (true) {
            //  if (mConfig.getEmbedCopyrightOfSaveImage()) {
                    // 著作権の埋め込み
                    rights = "mConfig.getCopyright()";
                    if (rights != null && rights.trim().length() > 0) {
                    //  bmp = SgpUtility.drawTextOnBitmap(tmpBmp, rights, Gravity.BOTTOM);
                        bmp = Bitmap.createBitmap(null);
                        try {
                            tmpBmp.recycle();
                        } catch (Exception e) {
                        }
                        tmpBmp = null;
                    } else {
                        bmp = tmpBmp;
                    }
                } else {
                    bmp = tmpBmp;
                }
                //
                file = new File(name);
                path = file.getParent();
                file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                ostream = new FileOutputStream(name, false);
                try{
                    // pngで保存、第2引数はpngでは無視される（jpegの圧縮率）
                    bmp.compress(CompressFormat.PNG, 0, ostream);
                    ostream.flush();
                    return name;
                }finally{
                    istream.close();
                    ostream.close();
                }
            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                if (bmp != null) {
                    bmp.recycle();
                }
                if (tmpBmp != null) {
                    tmpBmp.recycle();
                }
                if (srcBmp != null) {
                    srcBmp.recycle();
                }
            }
            return null;
        }
        /* (非 Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         * バックグラウンド処理完了時の処理
         */
        protected void onPostExecute(String filePath) {
            if (mDownloadFinished != null) {
                mDownloadFinished.onDownloadFinished(filePath);
            }
        }
    }
    /**
     * GET送信
     * @param listener GET送信後処理リスナー
     * @param url URL
     */
    public void get(final OnGetFinishedListener listener, String url) {
        mGetListener = listener;
        HttpPost request = new HttpPost(url);
        Log.d(TAG, String.format("url: %s", url));
        HttpGetTask task = new HttpGetTask();
        task.execute(request);
    }
    /**
     * GET通信タスク
     */
    private class HttpGetTask extends AsyncTask<HttpUriRequest, Void, HttpResponse> {
        /* (非 Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         * バックグラウンド処理
         */
        @SuppressLint("DefaultLocale")
        protected HttpResponse doInBackground(HttpUriRequest... request) {
//			AndroidHttpClient httpClient = AndroidHttpClient.newInstance(mContext.getPackageName());
            AndroidHttpClient httpClient = AndroidHttpClient.newInstance(System.getProperty("http.agent"));
            try {
                // UserAgent変更
                httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, UA_DUMMY);
                // 独自ヘッダ付与
                ArrayList<BasicHeader> arrHeader = new ArrayList<>();
               arrHeader.add(new BasicHeader("mConfig.getHeaderName()", UA_DUMMY));
                httpClient.getParams().setParameter("http.default-headers", arrHeader);
                // タイムアウト設定
//				HttpConnectionParams.setSoTimeout(httpClient.getParams(), 3000);
                return httpClient.execute(request[0]);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                if (httpClient != null) {
                    httpClient.close();
                }
            }
            return null;
        }
        /* (非 Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         * バックグラウンド処理完了時の処理
         */
        protected void onPostExecute(HttpResponse response) {
            int status = 0;
            String data = null;
            try {
                if (response != null) {
                    status = response.getStatusLine().getStatusCode();
                    Log.d(TAG, "status: " + status);
                    data = EntityUtils.toString(response.getEntity(), "UTF-8");
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            if (mGetListener != null) {
                mGetListener.onGetFinished(status, data);
            }
        }
    }

    /**
     * NotificationCompat.BigPictureStyle用画像取得
     * @param listener GET送信後処理リスナー
     * @param url URL
     */
    public void getImage(final OnGetImageFinishedListener listener, String url) {
        mGetImageListener = listener;
        HttpGetImageTask task = new HttpGetImageTask();
        task.execute(url);
    }
    class HttpGetImageTask extends AsyncTask<String, Void, Bitmap> {
        /* (非 Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         * バックグラウンド処理
         */
        @SuppressLint("DefaultLocale")
        protected Bitmap doInBackground(String... url) {
            // NotificationCompat.BigPictureStyle の画像表示エリアの大きさ（dp）
            final float WIDTH_DP = 450.0f;
            final float HEIGHT_DP = 192.0f;
            HttpUriRequest httpRequest;
            HttpClient httpclient;
            HttpResponse httpResponse;
            HttpEntity httpEntity = null;
            BufferedHttpEntity bufHttpEntity;
            Bitmap canvasBmp;
            Bitmap srcBmp;
            // 画像読み込みオプションを作成
            BitmapFactory.Options options = new BitmapFactory.Options();
            // メモリが少なくなったらパージできるようにオプションを設定
            options.inPurgeable = true;
            try {
                httpRequest = new HttpGet(url[0]);
                httpclient = new DefaultHttpClient();
                httpResponse = httpclient.execute(httpRequest);
                httpEntity = httpResponse.getEntity();
                bufHttpEntity = new BufferedHttpEntity(httpEntity);
                if (mGetImageListener == null) {
                    // 実際に画像を読み込み
                    canvasBmp = BitmapFactory.decodeStream(bufHttpEntity.getContent(), null, options);
                    if (canvasBmp == null) {
                        return null;
                    }
                    if (mGetImageListener == null) {
                        setPrefImageData(canvasBmp);
                        return null;
                    }
                } else {
                    // ピクセル密度を取得する
                    DisplayMetrics metrics = new DisplayMetrics();
                    WindowManager wm = (WindowManager)mContext.getSystemService(Activity.WINDOW_SERVICE);
                    wm.getDefaultDisplay().getMetrics(metrics);
                    float scaleDensity = metrics.scaledDensity;
                    // ピクセル密度を加味してターゲットの幅と高さのピクセル数を計算する
                    int targetWidthPx = (int) (WIDTH_DP * scaleDensity);
                    int targetHeightPx = (int) (HEIGHT_DP * scaleDensity);
                    // 実画面幅に合わせる
                //  int dw = SgpUtility.getDispWidth(mContext);
                    int dw = 10;
                    if (targetWidthPx > dw) {
                        targetWidthPx = dw;
                    }
                    // 画像サイズだけを読み込むようにオプションを設定
                    options.inJustDecodeBounds = true;

                    // 画像サイズの読み込み
                    BitmapFactory.decodeStream(bufHttpEntity.getContent(), null, options);

                    // 画像の幅と高さのピクセル数を取得する
                    int srcWidth = options.outWidth;
                    int srcHeight = options.outHeight;
                    if (srcWidth <= 0 || srcHeight <= 0) {
                        return null;
                    }

                    // Matrixを取得する パディングは0で小さな画像は拡大しない
                    Matrix mat = SgpImage.getMatrix(srcWidth, srcHeight, targetWidthPx, targetHeightPx, 0, false);

                    // Paintを作成する
                    Paint bmpPaint = new Paint();
                    bmpPaint.setFilterBitmap(true);

                    // ターゲット幅と高さで背景が透明のキャンバスを作成
                    canvasBmp = Bitmap.createBitmap(targetWidthPx, targetHeightPx, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(canvasBmp);

                    // 画像サイズだけでなく画像そのものを読み込むようにオプションを設定しなおす
                    options.inJustDecodeBounds = false;

                    // 実際に画像を読み込み
                    srcBmp = BitmapFactory.decodeStream(bufHttpEntity.getContent(), null, options);
                    if (srcBmp == null) {
                        return null;
                    }

                    // 背景が透明のキャンバスに画像を合成する
                    canvas.drawBitmap(srcBmp, mat, bmpPaint);

                    // メモリ解放
                    srcBmp.recycle();
                }
                if (mGetImageListener == null) {
                    setPrefImageData(canvasBmp);
                    return null;
                }
                return canvasBmp;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (httpEntity != null && httpEntity.getContent() != null) {
                        httpEntity.getContent().close();
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }
        /* (非 Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         * バックグラウンド処理完了時の処理
         */
        protected void onPostExecute(Bitmap bitmap) {
            if (mGetImageListener != null) {
                mGetImageListener.onFinished(bitmap);
            } else {
                setPrefImageData(bitmap);
            }
        }
        private void setPrefImageData(Bitmap bitmap) {
            ByteArrayOutputStream bos = null;
            try {
                final SharedPreferences pref = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
                String imageUrl = pref.getString("EXTRA_IMAGE_URL", null);
                if (imageUrl == null) {
                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    return;
                }
                if (bitmap != null) {
                    String ct = null;
                    try {
                        if (imageUrl.endsWith(".png")) {
                            ct = "png";
                        } else if (imageUrl.endsWith(".jpg")) {
                            ct = "jpeg";
                        }
                        bos = new ByteArrayOutputStream();
                        if (ct.compareTo("png") == 0) {
                            bitmap.compress(CompressFormat.PNG, 0, bos);
                        } else if (ct.compareTo("jpeg") == 0) {
                            bitmap.compress(CompressFormat.JPEG, 100, bos);
                        } else {
                            //noinspection ConstantConditions
                            if (bitmap != null) {
                                bitmap.recycle();
                                bitmap = null;
                            }
                            return;
                        }
                    } catch (Exception e) {
                    }
                }
                if (bos == null) {
                    if (pref.getString("base64", null) != null) {
                        pref.edit().remove("base64").commit();
                    }
                } else {
                    byte[] _bArray = bos.toByteArray();
                    String data = Base64.encodeToString(_bArray, android.util.Base64.URL_SAFE | android.util.Base64.NO_WRAP);
                    pref.edit().putString("base64", data).commit();
                }
            } catch (Exception e) {
            } finally {
                if (bitmap != null) {
                    bitmap.recycle();
                }
                bitmap = null;
            }
        }
    }
}
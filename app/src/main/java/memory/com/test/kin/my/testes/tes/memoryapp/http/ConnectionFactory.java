package memory.com.test.kin.my.testes.tes.memoryapp.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by maki on 2016/04/06.
 * MemoryApp.
 */
@SuppressWarnings("unused")
class ConnectionFactory {
    private static final String TAG = ConnectionFactory.class.getSimpleName();
    private final static String EOL = "\r\n";
    private final static String CHARSET = "UTF-8";
    // URL, UA, postData

    public static HttpURLConnection createConnection(String urlString) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            if (urlString.startsWith("https")) {
                conn = (HttpsURLConnection) url.openConnection();
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
        } catch (MalformedURLException ignored) {
        } catch (IOException e) {
            conn = null;
        }
        return conn;
    }

    public static HttpURLConnection setConnection(HttpURLConnection conn, String api) {
        try {
            // UserAgent変更
            // ヘッダー設定(複数設定可能)
            conn.setRequestProperty("X-SOL-UA", "UserAgent");
            conn.setRequestMethod(api);
            conn.setDoInput(true);
            conn.setDoOutput(true);
        } catch (ProtocolException e) {
            conn.disconnect();
            conn = null;
        }
        return conn;
    }

    public void checkHeder(HttpURLConnection conn) {
        final Map<String, List<String>> headers = conn.getHeaderFields();
        for (String key : headers.keySet()) {
            final List valueList = headers.get(key);
            final StringBuilder values = new StringBuilder();
            for (Object val : valueList) {
                values.append(val).append(" ");
            }
            Log.i(getClass().getSimpleName(), key + " : " + values.toString());
        }
    }

    public void checkModified(HttpURLConnection conn) {
        // 最終更新日時の文字列を取得する
        String lastModified = conn.getHeaderField("Last-Modified");

        // それをDate型に変換します
        //noinspection UnusedAssignment
        Date lastModifiedDate = null;
        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE',' dd' 'MMM' 'yyyy HH':'mm':'ss zzz", Locale.US);
            dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            dateFormatter.setCalendar(new GregorianCalendar());
            //noinspection UnusedAssignment
            lastModifiedDate = dateFormatter.parse(lastModified);
        } catch (ParseException ignored) {
        }
        // あとはlastModifiedDateを比較して更新するかどうかを決める
    }

    @SuppressWarnings("unused")
    public static Date fromString(String format, String string) throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        dateFormatter.setCalendar(new GregorianCalendar());
        return dateFormatter.parse(string);
    }

    // InputStream -> String
    static String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}

//    /**
//     * API <　19	libcore.net.http.HttpURLConnectionImpl
//     * API　>=19	com.android.okhttp.internal.http.HttpURLConnectionImpl
//     */
//    private JSONObject doPost(HttpURLConnection conn, String param) {
//        // 送信するコンテンツを成形する
//        StringBuilder paramBuilder = new StringBuilder();
//        int iContentsLength = 0;
//
//        paramBuilder.append("value1");
//        paramBuilder.append(EOL);
//        paramBuilder.append(EOL);
//        paramBuilder.append("value2");
//        paramBuilder.append(EOL);
//        paramBuilder.append("value3");
//        paramBuilder.append(EOL);
//
//        // コンテンツの長さを取得
//        try {
//            // StringBuilderを文字列に変化してからバイト長を取得しないと
//            // 実際送ったサイズと異なる場合があり、コンテンツを正しく送信できなくなる
//            iContentsLength = paramBuilder.toString().getBytes(CHARSET).length;
//        } catch (UnsupportedEncodingException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//
//        // 持続接続を設定
//        conn.setRequestProperty("Connection", "Keep-Alive");
//        // ユーザエージェントの設定（必須ではない）
//        conn.setRequestProperty("User-Agent", String.format("Mozilla/5.0 (Linux; U; Android %s;)", Build.VERSION.RELEASE));
//
//        // POSTデータの形式を設定
//        conn.setRequestProperty("Content-Type", String.format("text/plain; boundary=%s", "BOURDARY"));
//        // POSTデータの長さを設定
//        conn.setRequestProperty("Content-Length", String.valueOf(iContentsLength));
//
//        JSONObject json = null;
//        // タイムアウト設定
////		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 3000);
//        conn.setFixedLengthStreamingMode(param.getBytes().length);
//        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//
//        // 確認用
//        Map<String, List<String>> settings = conn.getRequestProperties();
//        Log.i("OSA030", "doPost start.:" + settings);
//        String result = "";
//        try {
//            DataOutputStream outputStream = null;
//            //POST用のOutputStreamを取得
//            outputStream = new DataOutputStream(conn.getOutputStream());
//            //データをPOST
////            Byte[] bytes = string.getBytes();
////            for (int i=0;i<bytes.length;i++){
////                writeByte(bytes[i]);
////            }
////            outputStream.writeBytes(paramBuilder.toString());
////            outputStream.close();
//
//            // http://www.magata.net/memo/index.php?Java%A4%C7HTTP%A5%EA%A5%AF%A5%A8%A5%B9%A5%C8
//            // リクエストデータ整形
//            String postStr = "foo1=bar1&foo2=bar2";//POSTするデータ
//            PrintStream ps = new PrintStream(outputStream, true, "UTF-8");
//            ps.print(postStr);//データをPOSTする
//            ps.close();
//
//            // 取得ヘッダー
//            checkHeder(conn);
//
//            int responseCode = 0;
//            // レスポンスを受信する
//            responseCode = conn.getResponseCode();
//            Log.d(TAG, "status: " + responseCode);
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                InputStream stream = conn.getInputStream();
//                StringBuilder resultBuilder = new StringBuilder();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//                // レスポンスの読み込み
//                String inputLine = "";
//                while ((inputLine = reader.readLine()) != null) {
//                    resultBuilder.append(inputLine);
//                }
//                String data = resultBuilder.toString();
//                data = data.replaceAll("\n", "");
//                if (data.trim().length() == 0) {
//                    json = new JSONObject();
//                } else {
//                    json = new JSONObject(data);
//                }
//                stream.close();
//            } else {
//                json = new JSONObject();
//            }
//            json.put("status", responseCode);
//            Log.d(TAG, "json: " + json.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException ignored) {
//        } finally {
//            conn.disconnect();
//        }
//        return json;
//    }
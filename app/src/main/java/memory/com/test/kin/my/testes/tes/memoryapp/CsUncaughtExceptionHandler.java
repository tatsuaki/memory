package memory.com.test.kin.my.testes.tes.memoryapp;

/**
 * Created by maki on 2016/04/08.
 * MemoryApp.
 */

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class CsUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CsUncaughtEx";

    // 現在設定されている UncaughtExceptionHandler を退避
    final Thread.UncaughtExceptionHandler savedUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    private volatile boolean mCrashing = false;
    private Activity mActivity;

    public CsUncaughtExceptionHandler(Activity activity) {
        mActivity = activity;
    }

    /**
     * キャッチされない例外によって指定されたスレッドが終了したときに呼び出されます
     *
     * @param thread
     * @param exception
     */
    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        Log.i(TAG, "uncaughtException");
        Log.i(TAG, "thread = " + thread.toString());
        Log.i(TAG, "exception = " + exception.toString());

        try {
            if (!mCrashing) {
                mCrashing = true;
            }
            // catchされなかった例外処理 エラーログを保存したりする。
            // 再起動処理
            Intent in = new Intent(mActivity, MainActivitys.class);
            mActivity.startActivity(in);
            mActivity.finish();
        } finally {
            // 退避しておいた UncaughtExceptionHandler を実行
        //  savedUncaughtExceptionHandler.uncaughtException(thread, exception);//例外のダイアログ表示
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
package memory.com.test.kin.my.testes.tes.memoryapp.sync;

import android.util.Log;

/**
 * Created by maki on 2016/04/10.
 * MemoryApp.
 */
//// 課金プロセスタスクの基底クラス。
//// 変更不可能。
abstract class SgpBillingProcessBaseTask {
    /**
     * 別スレッドで行ないたいメインの処理を記述
     */
    protected abstract void processStart();
    /**
     * 非同期タスクを開始する
     */
    public void startAsyncTask() {
        // 新スレッド上で実行。startで開始したら，そのまま放置。
        Thread t = new Thread() {
            public void run() {
                Log.w("BillingProcess", "processStart SgpBillingProcessBaseTask");
                processStart();
            }
        };
        t.start();
    }
}
package memory.com.test.kin.my.testes.tes.memoryapp.sync;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by maki on 2016/04/10.
 * MemoryApp.
 */
// 逐次化可能な非同期タスクの基底クラス
abstract class SgpBillingProcessTask extends SgpBillingProcessBaseTask {
    // 呼び出し元ランナー
    protected SgpBillingTaskRunner mBillingTaskRunner = null;
    // タスク実行結果
    private boolean taskProcessResult = true;
    // プロダクトIDリスト
    private HashMap<String, Object> pfList = new HashMap<String, Object>();
    // Googleアイテムリスト
    private HashMap<String, Object> googleList = new HashMap<String, Object>();
    // タスク内の保持データ
    private HashMap<String, Object> hash = new HashMap<String, Object>();

    // 別スレッドで行ないたいメインの処理。継続の是非を返す。
    public abstract boolean execTask();

    // メイン処理の実行をラップさせるメソッドとする
    @Override
    protected void processStart() {
        // メイン処理を実行
        taskProcessResult = execTask();
        Log.w("BillingProcess", "processStart taskProcessResult = " + taskProcessResult);
        // 呼び出し元に通知
        mBillingTaskRunner.onCurrentTaskFinished();
    }

    // ランナーにより，このタスクを開始
    public void kickByRunner(final SgpBillingTaskRunner parent) {
        // 呼び出し元のランナーをセット
        this.mBillingTaskRunner = parent;
        // 別スレッド上でタスクを開始
        startAsyncTask();
        Log.w("BillingProcess", "kickByRunner");
    }

    // 次のタスクへ継続可能かどうかを判定
    public boolean tasksContinuable() {
        return taskProcessResult;
    }

    // ------------- この非同期タスク内部で保持するデータ ------------
    // 任意のデータを１つ格納
    protected void sharedObject(String key, Object value) {
        hash.put(key, value);
        Log.v("BillingProcess", "sharedObject ke = " + key + " value = " + value);
    }

    // 全データを返す
    public HashMap<String, Object> getSharedObjects() {
        return hash;
    }

    // デバッグ用
    protected void log(String s) {
        System.out.println("[DEBUG] " + this.getClass().getName() + " : " + s);
    }

    // ------------- 別の非同期タスクと共有するデータ ------------
    // 任意のデータをランナー側から１つ取得
    protected Object getDataFromRunner(String key) {
        return mBillingTaskRunner.getDataByKey(key);
    }
}
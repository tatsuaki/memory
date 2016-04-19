package memory.com.test.kin.my.testes.tes.memoryapp.sync;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import memory.com.test.kin.my.testes.tes.memoryapp.Billing.util.IabHelper;

/**
 * Created by maki on 2016/04/10.
 * MemoryApp.
 */
class SgpBillingTaskRunner {
    // 実行したい非同期タスク達
    private SgpBillingProcessTask[] mTasks;
    // 現在取り扱い中のタスクのインデックス
    private int mProcessIndex = 0;
    public Activity mActivity;
    // 全タスクから返されるデータ
    private HashMap<String, Object> mSharedProcessData = new HashMap<String, Object>();
    public IabHelper mHelper;
    /**
     *  初期化
     * @param tasks
     */
    public SgpBillingTaskRunner(Activity activity, IabHelper helper, SgpBillingProcessTask[] tasks) {
        mActivity = activity;
        mHelper = helper;
        this.mTasks = tasks;
    }

    // 全タスクを実行開始
    public void taskRun() {
        Log.i("BillingProcess","taskRun");
        if (mTasks.length > 0) {
            Log.i("BillingProcess","first");
            executeCurrentTask();
        }
        Log.v("BillingProcess","taskRun finish");
    }

    // 現在のタスクを実行
    private void executeCurrentTask() {
        Log.i("BillingProcess","executeCurrentTask mProcessIndex = " + mProcessIndex);
        getCurrentTask().kickByRunner(this);
    }

    // 現在取り組み中のタスクを返す
    private SgpBillingProcessTask getCurrentTask() {
        return mTasks[mProcessIndex];
    }

    // 現在のタスクの実行完了時に呼ばれる
    public void onCurrentTaskFinished() {
        // 現行タスクの終了処理
        mergeDataFromTask(getCurrentTask());

        if (mustMoveToNextTask()) {
            Log.e("BillingProcess","to nextTask");
            // 次のタスクへ移動
            executeNextTask();
        } else {
            Log.e("BillingProcess","not to nextTask");
        }
    }

    // 現在処理中のタスクが最後のタスクかどうか返す
    private boolean isProccessingLastTask() {
        return (mTasks.length == (mProcessIndex + 1));
    }

    /**
     * タスクを継続可否を返却
     * @return タスク継続可否
     */
    private boolean mustMoveToNextTask() {
        // 最後のタスクでなく，現行タスクの結果がOKであれば。
        return (
                (!isProccessingLastTask()) &&
                        (getCurrentTask().tasksContinuable())
        );
    }

    // １タスクの保持データを回収
    private void mergeDataFromTask(SgpBillingProcessTask task) {
        // HashMapを取り出して，そのループの準備をする
        HashMap<String, Object> data_from_current_task = task.getSharedObjects();
        Set<String> keySet = data_from_current_task.keySet();
        Iterator<String> keyIterator = keySet.iterator();

        // 全キーについて，ランナー側に値を取り込む
        while (keyIterator.hasNext()) {
            // １ペアを読み込み
            String key = (String) keyIterator.next();
            Object value = data_from_current_task.get(key);

            // １ペアを上書き
            mSharedProcessData.put(key, value);
            Log.e("BillingProcess","ランナー側で " + key + " の値を上書き");
        }
    }

    // 次のタスクを実行
    private void executeNextTask() {
        mProcessIndex++;
        executeCurrentTask();
    }

    // タスクから回収した値をキーごとに返す
    public Object getDataByKey(String key) {
        return mSharedProcessData.get(key);
    }

    // デバッグ用
    private void log(String s) {
        System.out.println("BillingProcess AsyncTasksRunner : " + s);
    }

}
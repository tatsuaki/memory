package memory.com.test.kin.my.testes.tes.memoryapp.sync;

/**
 * Created by maki on 2016/04/10.
 * MemoryApp.
 */
// カスタム非同期タスクの一種。
abstract class SgpPfListGet extends SgpBillingProcessTask {
    public boolean execTask() {
        log("main外のスレッドで，1から100までの合計値を計算します。");
        log("PFからリスト取得");
        // 計算実行
        int sum = 0;
        for (int i = 1; i < 101; i++) {
            sum += i;
        }
        log("計算終了。この値を別のタスクから参照可能になるよう保持。" + sum);
        log("PFリスト整形");
        sharedObject("sum1", sum);
        return true;
    }
}
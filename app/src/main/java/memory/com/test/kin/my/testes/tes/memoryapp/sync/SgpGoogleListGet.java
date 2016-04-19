package memory.com.test.kin.my.testes.tes.memoryapp.sync;

import memory.com.test.kin.my.testes.tes.memoryapp.Billing.util.IabHelper;

/**
 * Created by maki on 2016/04/10.
 * MemoryApp.
 */
// カスタム非同期タスクの一種。
abstract class SgpGoogleListGet extends SgpBillingProcessTask {
}

//                    public boolean execTask() {
//                        // 以前の非同期タスクから値を受け取ることができる
//                        log("前の非同期タスクの実行結果は" + getDataFromRunner("sum1"));
//                        log("main外のスレッドで，101から200までの合計値を計算します。");
//                        log("Googleに問い合わせ");
//                        // 計算実行
//                        int sum = 0;
//                        for (int i = 101; i < 201; i++) {
//                            sum += i;
//                        }
//                        log("計算終了。この値を別のタスクから参照可能になるよう保持。" + sum);
//                        sharedObject("sum2", sum);
//                        log("Googleリスト整形");
//                        return true;
//                    }
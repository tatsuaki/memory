package memory.com.test.kin.my.testes.tes.memoryapp.Debug;

import android.app.Activity;
import android.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LayoutUtil {
    private static final String TAG = "LayoutUtil";
    /**
     *
     * @param view views
     */
    public static void cleanupView(View view) {
    //  Log.v(TAG, "cleanupView start");
        if (view instanceof ImageButton) {
            Log.v(TAG, "clean ImageButton");
            ImageButton ib = (ImageButton) view;
            ib.setImageDrawable(null);
        } else if (view instanceof ImageView) {
            Log.v(TAG, "clean ImageView");
            ImageView iv = (ImageView) view;
            iv.setImageDrawable(null);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            Log.v(TAG, "clean TextView " + tv.getText());
        } else if (view instanceof LinearLayout) {
            Log.v(TAG, "clean LinearLayout");
        } else if (view instanceof RelativeLayout) {
            Log.v(TAG, "clean RelativeLayout");
        } else if (view instanceof FrameLayout) {
            Log.v(TAG, "clean FrameLayout");
        }
        if (null!= view) {
            view.setBackground(null);
            view.setOnClickListener(null);
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                int size = vg.getChildCount();
                Log.v(TAG, "size = " + size);
                for (int i = 0; i < size; i++) {
                    cleanupView(vg.getChildAt(i));
                }
            }
        } else {
            Log.v(TAG, "cleanupView finish");
        }
    }

    /**
     * バックスタックを全て削除
     */
    @SuppressWarnings("unused")
    public static void deleteBackStackFragment(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        manager.popBackStack();

        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack(manager.getBackStackEntryAt(0).getName(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        //トランザクションを直ちに実行
        manager.executePendingTransactions();
    }
}

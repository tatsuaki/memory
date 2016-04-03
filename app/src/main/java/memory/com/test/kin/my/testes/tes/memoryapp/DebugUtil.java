package memory.com.test.kin.my.testes.tes.memoryapp;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.math.BigDecimal;

/**
 * Created by maki on 2016/04/02.
 * MemoryApp.
 */
public class DebugUtil {

    public static String makeValut(float value) {
        BigDecimal bd = new BigDecimal(value);
        BigDecimal bd2 = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd2.toString();
    }
}

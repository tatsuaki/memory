package memory.com.test.kin.my.testes.tes.memoryapp.Debug;

import java.math.BigDecimal;

/**
 * Created by maki on 2016/04/02.
 * MemoryApp.
 */
class DebugUtil {
    private static final String L_DPI = "0.75";
    private static final String M_DPI = "1.0";
    private static final String H_DPI = "1.5";
    private static final String XH_DPI = "2.0";
    private static final String XXH_DPI = "3.0";
    private static final String XXXH_DPI = "4.0";

    /**
     * 丸目処理
     * @param value 変換対象
     * @return 返還後
     */
    public static String makeValut(float value) {
        BigDecimal bd = new BigDecimal(value);
        BigDecimal bd2 = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd2.toString();
    }

    /**
     * @param density 解像度
     * @return 対象解像度
     */
    public static String chengeDensity(String density) {
        String dpiName;
        switch (density) {
            case L_DPI:
                dpiName = "ldpi";
                break;
            case M_DPI:
                dpiName = "mdpi";
                break;
            case H_DPI:
                dpiName = "hdpi";
                break;
            case XH_DPI:
                dpiName = "xhdpi";
                break;
            case XXH_DPI:
                dpiName = "xxhdpi";
                break;
            case XXXH_DPI:
                dpiName = "xxxhdpi";
                break;
            default:
                dpiName = "none";
                break;
        }
        return dpiName;
    }
}

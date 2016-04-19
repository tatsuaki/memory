package memory.com.test.kin.my.testes.tes.memoryapp.http;

import android.graphics.Matrix;

/**
 *
 */
class SgpImage {
    /**
     * @param sw ソース幅
     * @param sh ソース高さ
     * @param pw この幅にフィットさせる
     * @param ph この高さにフィットさせる
     * @param padding フィット時に考慮するパディング
     * @param enableMagnify 画像が小さい場合に拡大するか否か
     * @return フィットさせるためのスケール値
     */
    static Matrix getMatrix(int sw, int sh, int pw, int ph, @SuppressWarnings("SameParameterValue") int padding, @SuppressWarnings("SameParameterValue") boolean enableMagnify) {
        float scale = getMaxScaleToParent(sw, sh, pw, ph, padding);
        //noinspection StatementWithEmptyBody
        if (!enableMagnify && scale > 1.0f) {
//			scale = 1;
        }
        Matrix mat = new Matrix();
        mat.postScale(scale, scale);
        mat.postTranslate((pw - (int) (sw * scale)) / 2, (ph - (int) (sh * scale)) / 2);
        return mat;
    }

    private static float getMaxScaleToParent(int sw, int sh, int pw, int ph, int padding) {
        float hScale = (float) (pw - padding) / (float) sw;
        float vScale = (float) (ph - padding) / (float) sh;
        return Math.min(hScale, vScale);
    }
}

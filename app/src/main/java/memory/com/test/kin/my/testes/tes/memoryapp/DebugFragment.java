package memory.com.test.kin.my.testes.tes.memoryapp;

import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

public class DebugFragment extends Fragment {
    private static final String TAG = "DebugFragment";
    private TextView widthTextView;
    private TextView heightTextView;
    private TextView densityTextView;
    private TextView scaledDensityTextView;
    private TextView refreshRateTextView;
    private TextView freeTextView;
    private TextView memoryTextView;
    private TextView maxtextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.debug_fragment, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();

        showDisplayData();
        TextView onText = (TextView)getActivity().findViewById(R.id.debug_checked);
        onText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheks();
            }
        });
    }

    private void showDisplayData() {
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        //  Build.VERSION.SDK_INT;
        String widthPixels = String.valueOf(displayMetrics.widthPixels);
        String heightPixels = String.valueOf(displayMetrics.heightPixels);
        String density = String.valueOf(displayMetrics.density);
        String scaledDensity = String.valueOf(displayMetrics.scaledDensity);
        String refreshRate = String.valueOf(display.getRefreshRate());

        Log.v(TAG, "widthPixels   " + widthPixels);
        Log.v(TAG, "heightPixels  " + heightPixels);
        Log.v(TAG, "density       " + density);
        Log.v(TAG, "scaledDensity " + scaledDensity);
        Log.v(TAG, "refreshRate   " + refreshRate);

        if (null == widthTextView) {
            widthTextView = (TextView) getActivity().findViewById(R.id.debug_widthPixels);
        }
        if (null == heightTextView) {
            heightTextView = (TextView) getActivity().findViewById(R.id.debug_heightPixels);
        }
        if (null == densityTextView) {
            densityTextView = (TextView) getActivity().findViewById(R.id.debug_density);
        }
        if (null == scaledDensityTextView) {
            scaledDensityTextView = (TextView) getActivity().findViewById(R.id.debug_scaledDensity);
        }
        if (null == refreshRateTextView) {
            refreshRateTextView = (TextView) getActivity().findViewById(R.id.debug_refreshRate);
        }

        widthTextView.setText("widthPixels   " + widthPixels);
        heightTextView.setText("heightPixels  " + heightPixels);
        densityTextView.setText("density       " + density);
        scaledDensityTextView.setText("scaledDensity " + scaledDensity);
        refreshRateTextView.setText("refreshRate   " + refreshRate);
    }

    public void cheks() {
        Runtime runtime = Runtime.getRuntime();
        float totals =(float)(runtime.totalMemory()/1024) / 1000;
        float frees = (float)(runtime.freeMemory()/1024) / 1000;
        float max = (float)(runtime.maxMemory()/1024) / 1000;
        Log.v(TAG, "total[MB] = " + DebugUtil.makeValut(totals));
        Log.v(TAG, "free[MB] = " + DebugUtil.makeValut(frees));
        Log.v(TAG, "use[MB]  = " + DebugUtil.makeValut(totals - frees));
        Log.v(TAG, "max[MB]  = " + DebugUtil.makeValut(max));

        if (null == freeTextView) {
            freeTextView = (TextView) getActivity().findViewById(R.id.debug_free);
        }
        if (null == memoryTextView) {
            memoryTextView = (TextView) getActivity().findViewById(R.id.debug_use);
        }
        if (null == maxtextView) {
            maxtextView = (TextView) getActivity().findViewById(R.id.debug_max);
        }
        freeTextView.setText("free[MB] = " + DebugUtil.makeValut(frees));
        memoryTextView.setText("use[MB]  = " + DebugUtil.makeValut(totals - frees));
        maxtextView.setText("max[MB]  = " + DebugUtil.makeValut(max));
    }

    public void chekss() {
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        //  Build.VERSION.SDK_INT;
        Log.v(TAG, "widthPixels   " + String.valueOf(displayMetrics.widthPixels));
        Log.v(TAG, "heightPixels  " + String.valueOf(displayMetrics.heightPixels));
        Log.v(TAG, "density       " + String.valueOf(displayMetrics.density));
        Log.v(TAG, "scaledDensity " + String.valueOf(displayMetrics.scaledDensity));
        Log.v(TAG, "refreshRate   " + String.valueOf(display.getRefreshRate()));
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
}

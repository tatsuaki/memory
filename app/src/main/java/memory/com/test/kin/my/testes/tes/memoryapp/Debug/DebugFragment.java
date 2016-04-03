package memory.com.test.kin.my.testes.tes.memoryapp.Debug;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import memory.com.test.kin.my.testes.tes.memoryapp.R;

public class DebugFragment extends Fragment {
    private static final String TAG = "DebugFragment";
    private TextView manufactureTextView;
    private TextView releaseTextView;
    private TextView pixelsTextView;
    private TextView densityTextView;
    private TextView linuxTextView;
    private TextView nativeTextView;
    private TextView freeTextView;
    private TextView memoryTextView;
    private TextView maxtextView;
    private TextView totaltextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.debug_fragment, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();

        showModelData();
        showDisplayData();
        TextView onText = (TextView)getActivity().findViewById(R.id.debug_checked);
        onText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyze();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showModelData() {
        if (null == manufactureTextView) {
            manufactureTextView = (TextView) getActivity().findViewById(R.id.debug_manufacture);
        }
        if (null == releaseTextView) {
            releaseTextView = (TextView) getActivity().findViewById(R.id.debug_release);
        }
        manufactureTextView.setText(Build.MANUFACTURER  + "  " + Build.MODEL);
        releaseTextView.setText("Android " + Build.VERSION.RELEASE + "[" + Build.VERSION.SDK_INT + "]");
    }

    @SuppressLint("SetTextI18n")
    private void showDisplayData() {
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        String density = String.valueOf(displayMetrics.density);
        String scaledDensity = String.valueOf(displayMetrics.scaledDensity);
        String dpi = DebugUtil.chengeDensity(scaledDensity);

        if (null == pixelsTextView) {
            pixelsTextView = (TextView) getActivity().findViewById(R.id.debug_pixels);
        }
        if (null == densityTextView) {
            densityTextView = (TextView) getActivity().findViewById(R.id.debug_density);
        }
        pixelsTextView.setText("w:" + displayMetrics.widthPixels + " h:" + displayMetrics.heightPixels);
        densityTextView.setText("density" + density + " " + dpi);
    }

    private void analyze() {
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = ((ActivityManager)getActivity().getSystemService(Activity.ACTIVITY_SERVICE));
        activityManager.getMemoryInfo(info);
        float linuxHeap = (float)(info.availMem / 1024) / 1000;
        float nativeHeap = (float)(Debug.getNativeHeapAllocatedSize() / 1024 ) / 1000;

        Runtime runtime = Runtime.getRuntime();
        float total =(float)(runtime.totalMemory() / 1024) / 1000;
        float frees = (float)(runtime.freeMemory() / 1024) / 1000;
        float max = (float)(runtime.maxMemory() / 1024) / 1000;
        float javaHeap = total - frees;

        if (null == linuxTextView) {
            linuxTextView = (TextView) getActivity().findViewById(R.id.debug_linux);
        }
        if (null == nativeTextView) {
            nativeTextView = (TextView) getActivity().findViewById(R.id.debug_native);
        }
        if (null == totaltextView) {
            totaltextView = (TextView) getActivity().findViewById(R.id.debug_total);
        }
        if (null == freeTextView) {
            freeTextView = (TextView) getActivity().findViewById(R.id.debug_free);
        }
        if (null == memoryTextView) {
            memoryTextView = (TextView) getActivity().findViewById(R.id.debug_use);
        }
        if (null == maxtextView) {
            maxtextView = (TextView) getActivity().findViewById(R.id.debug_max);
        }
        Log.d(TAG, "linux " + DebugUtil.makeValut(linuxHeap));
        Log.d(TAG, "native " + DebugUtil.makeValut(nativeHeap));
        Log.d(TAG, "frees " + DebugUtil.makeValut(frees));
        Log.d(TAG, "java " + DebugUtil.makeValut(javaHeap));
        Log.d(TAG, "max " + DebugUtil.makeValut(max));
        Log.d(TAG, "total " + DebugUtil.makeValut(javaHeap + nativeHeap));
        linuxTextView.setText(DebugUtil.makeValut(linuxHeap));
        nativeTextView.setText(DebugUtil.makeValut(nativeHeap));
        freeTextView.setText(DebugUtil.makeValut(frees));
        memoryTextView.setText(DebugUtil.makeValut(javaHeap));
        maxtextView.setText(DebugUtil.makeValut(max));
        totaltextView.setText(DebugUtil.makeValut(javaHeap + nativeHeap));
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        LayoutUtil.cleanupView(getActivity().findViewById(R.id.debug_content));
        super.onDestroy();
    }
}

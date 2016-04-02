package memory.com.test.kin.my.testes.tes.memoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivitys extends AppCompatActivity {
    private ImageView imageView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activitys);

        button = (Button) findViewById(R.id.button);
        String packageName = BuildConfig.APPLICATION_ID;
    }
}

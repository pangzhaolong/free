package debug;

import android.app.Activity;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类描述
 *
 * @author Swei
 * @date 2021/4/9 10:38
 * @since v1.0
 */
public class Test2Activity12 extends Activity {

    public static final String METHOD = "method";
    private String exceptionPoint;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        textView.setText("测试");
        textView.setTextSize(18);

        setContentView(textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Process.killProcess(Process.myPid());
            }
        });

        init();
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        exceptionPoint = getIntent().getStringExtra(METHOD);
    }

    private void init() {

        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                textView.setText(time.format(new Date()));

                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }

}

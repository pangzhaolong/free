package debug;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.donews.keepalive.LaunchStart;
import com.keepalive.daemon.core.notification.NotifyResidentService;

/**
 * 类描述
 *
 * @author Swei
 * @date 2021/4/7 17:31
 * @since v1.0
 */
public class TestActivity extends Activity {
    public static final String TAG = "crash";
    private static final LaunchStart launchStart = new LaunchStart();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Button btn1 = new Button(this);
        btn1.setText("更新通知");
        btn1.setTextSize(15);


        Button btn2 = new Button(this);
        btn2.setText("开启获取隐藏API模式");
        btn2.setTextSize(15);

        Button btn3 = new Button(this);
        btn3.setText("安装crash");
        btn3.setTextSize(15);

        Button btn33 = new Button(this);
        btn33.setText("卸载crash");
        btn33.setTextSize(15);

        Button btn4 = new Button(this);
        btn4.setText("主线程崩溃");
        btn4.setTextSize(15);
        Button btn44 = new Button(this);
        btn44.setText("子线程崩溃");
        btn44.setTextSize(15);

        Button btn5 = new Button(this);
        btn5.setText("生命周期崩溃");
        btn5.setTextSize(15);

        linearLayout.addView(btn1,new LinearLayout.LayoutParams(800,200));
        linearLayout.addView(btn2,new LinearLayout.LayoutParams(800,200));
        linearLayout.addView(btn3,new LinearLayout.LayoutParams(800,200));
        linearLayout.addView(btn33,new LinearLayout.LayoutParams(800,200));
        linearLayout.addView(btn4,new LinearLayout.LayoutParams(800,200));
        linearLayout.addView(btn44,new LinearLayout.LayoutParams(800,200));
        linearLayout.addView(btn5,new LinearLayout.LayoutParams(800,200));

        setContentView(linearLayout);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(TestActivity.this, NotifyResidentService.class);
                    intent.putExtra("noti_title", "Test");
                    intent.putExtra("noti_text", "Hello,world!");
                    intent.putExtra("noti_activity", TestActivity.class.getName());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent);
                    }else{
                        startService(intent);
                    }
                } catch (Throwable th) {
                    Log.e(TAG, "failed to start foreground service: " + th.getMessage());
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //start
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btn44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(TestActivity.this, Test2Activity.class);
                        intent.putExtra(Test2Activity.METHOD, "onCreate");
                        launchStart.doStart(TestActivity.this,intent);
//                        startActivity(intent);
                    }
                },3000);

            }
        });


    }


}

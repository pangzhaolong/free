package debug;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.donews.crashhandler.core.CrashCoreHandler;
import com.donews.crashhandler.reflect.ReflectionLimit;

import java.lang.reflect.Field;

/**
 * 类描述
 *
 * @author Swei
 * @date 2021/4/7 17:31
 * @since v1.0
 */
public class TestActivity extends AppCompatActivity {
    public static final String TAG = "crash";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Button btn1 = new Button(this);
        btn1.setText("反射隐藏API");
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

        //warning api test
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    test();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    test1();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        //start
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CrashCoreHandler.install();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
        btn33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CrashCoreHandler.uninstall();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=null;

                if (a.equals("aaa")) {
                    Toast.makeText(getApplicationContext(), "aaa", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(),"bbb",Toast.LENGTH_SHORT).show();
            }
        });

        btn44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String a=null;
                        if (a.equals("aaa")) {
                            Log.d(TAG,"crash1");
                        }
                        Log.d(TAG,"crash2");
                    }
                }).start();
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, Test2Activity.class);
                intent.putExtra(Test2Activity.METHOD, "onCreate");
                startActivity(intent);
            }
        });


    }

    private void test() throws Throwable {
        Class<?> activityThread = Class.forName("android.app.ActivityThread");
        Class<?> hclass = Class.forName("android.app.ActivityThread$H");
        Field[] declaredFields = hclass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Log.i(TAG, "declareField: " + declaredField);
        }
    }

    private void test1() throws Throwable {
        ReflectionLimit.clearLimit();
    }

}

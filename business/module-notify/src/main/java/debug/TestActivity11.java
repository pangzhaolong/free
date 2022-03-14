package debug;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * 类描述
 *
 * @author Swei
 * @date 2021/4/7 17:31
 * @since v1.0
 */
public class TestActivity11 extends Activity {
    public static final String TAG = "crash";
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

            }
        });


    }


}

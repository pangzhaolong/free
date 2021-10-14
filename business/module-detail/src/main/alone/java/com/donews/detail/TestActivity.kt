package com.donews.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.alibaba.android.arouter.launcher.ARouter
import com.donews.common.router.RouterActivityPath
import com.donews.detail.ui.GoodsDetailActivity

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity_test)
        val button:Button = findViewById(R.id.detail_button)
        button.setOnClickListener {
            ARouter.getInstance().build(RouterActivityPath.GoodsDetail.GOODS_DETAIL)
                .withString("params_id", "35925577")
                .navigation()
        }
    }

//    fun clickGoodsDetail(view: View) {
//        ARouter.getInstance().build(RouterActivityPath.GoodsDetail.GOODS_DETAIL)
//            .withString("params_id", "35925577")
//            .navigation()
//
////        GoodsDetailActivity.start(this, "35925577", null)
//    }
}
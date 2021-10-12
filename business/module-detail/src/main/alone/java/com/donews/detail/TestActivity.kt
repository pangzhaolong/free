package com.donews.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.donews.detail.ui.GoodsDetailActivity

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity_test)
    }

    fun clickGoodsDetail(view: View) {
        GoodsDetailActivity.start(this, "25081212714", null)
    }
}
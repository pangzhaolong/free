package com.donews.detail.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.donews.detail.R

/**
 * 商品详情页
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/11 17:25
 */
class GoodsDetailActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, GoodsDetailActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity_goods_detail)
    }
}
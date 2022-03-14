package com.donews.unboxing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.donews.common.router.RouterFragmentPath

class TestUnboxingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.unboxing_activity_test_unboxing)
        val fragment = RouterFragmentPath.Unboxing.getUnboxingFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container, fragment)
            .commit()
    }
}
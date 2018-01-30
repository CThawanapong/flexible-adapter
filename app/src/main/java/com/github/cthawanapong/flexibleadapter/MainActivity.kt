package com.github.cthawanapong.flexibleadapter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.github.cthawanapong.flexibleadapter.adapter.ShowcaseAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        recyclerView.apply {
            adapter = ShowcaseAdapter(this@MainActivity).apply {
                resetAdapter()
            }
            layoutManager = GridLayoutManager(this@MainActivity, 1, LinearLayoutManager.VERTICAL, false)
        }
    }
}

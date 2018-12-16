package com.nimble.surveys.ui.main

import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import com.nimble.surveys.R
import com.nimble.surveys.base.BaseActivity
import com.nimble.surveys.base.DummyViewModel
import com.nimble.surveys.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding, DummyViewModel>(DummyViewModel::class) {
    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun initViews() {
        setSupportActionBar(binding.appBar.toolbar)

        val params = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )

        // To put app title on center of actionBar
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setCustomView(layoutInflater.inflate(R.layout.app_title, null), params)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val mainFragment = supportFragmentManager.findFragmentById(R.id.mainFragment) as MainFragment?

        return when {
            item.itemId == android.R.id.home -> {
                mainFragment?.viewModel?.onRefresh()
                true
            }
            item.itemId == R.id.action_menu -> {
                mainFragment?.viewModel?.onClickMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
}

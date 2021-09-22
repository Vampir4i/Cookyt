package com.example.cookyt.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookyt.App
import com.example.cookyt.R
import com.example.cookyt.adapter.RecipesListAdapter
import com.example.cookyt.databinding.ActivityRecipesBinding
import com.example.cookyt.view_model.MainActivityViewModel

class RecipesActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecipesBinding
    lateinit var vm: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = resources.getColor(android.R.color.holo_green_light)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipes)
        vm = ViewModelProvider(this)[MainActivityViewModel::class.java]

        val categoryId = intent.getStringExtra("category_id") ?: "0"
        val category = intent.getStringExtra("category") ?: ""

        binding.category = category
        binding.rcRecipes.layoutManager = LinearLayoutManager(this)
        val adapter = RecipesListAdapter(listOf(), this, category, this)
        binding.rcRecipes.adapter = adapter

        vm.recipes.observe(this, {
            adapter.updateValues(it)
            binding.listRefresh.isRefreshing = false
        })

        binding.btnBack.setOnClickListener { finish() }
        binding.btnSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        binding.listRefresh.setOnRefreshListener { checkConnection(categoryId) }
        findViewById<TextView>(R.id.tv_try_again).setOnClickListener { checkConnection(categoryId) }

        checkConnection(categoryId)
    }

    fun checkConnection(categoryId: String) {
        if(App.isOnline()){
            binding.layoutBadInternet.visibility = View.GONE
            binding.rcRecipes.visibility = View.VISIBLE
            vm.getRecipes(categoryId)
        } else {
            binding.rcRecipes.visibility = View.GONE
            binding.layoutBadInternet.visibility = View.VISIBLE
            binding.listRefresh.isRefreshing = false
        }
    }
}
package com.example.cookyt.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipes)
        vm = ViewModelProvider(this)[MainActivityViewModel::class.java]

        val categoryId = intent.getStringExtra("category_id") ?: "0"
        val category = intent.getStringExtra("category") ?: ""

        binding.rcRecipes.layoutManager = LinearLayoutManager(this)
        val adapter = RecipesListAdapter(listOf(), this, category, this)
        binding.rcRecipes.adapter = adapter

        vm.recipes.observe(this, {
            App.makeLog("${it.size}")
            adapter.updateValues(it)
        })

        vm.getRecipes(categoryId)
    }
}
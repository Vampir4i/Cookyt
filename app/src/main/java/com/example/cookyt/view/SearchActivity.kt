package com.example.cookyt.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookyt.R
import com.example.cookyt.adapter.RecipesListAdapter
import com.example.cookyt.databinding.ActivitySearchBinding
import com.example.cookyt.view_model.MainActivityViewModel

class SearchActivity : AppCompatActivity() {
    lateinit var vm: MainActivityViewModel
    lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        vm = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.rcRecipes.layoutManager = LinearLayoutManager(this)
        val adapter = RecipesListAdapter(listOf(), this, "", this)
        binding.rcRecipes.adapter = adapter

        vm.searchRecipes.observe(this, {
            adapter.updateValues(it)
        })

        binding.etSearch.addTextChangedListener {
            vm.searchRecipe(it?.toString() ?: "")
        }
    }
}
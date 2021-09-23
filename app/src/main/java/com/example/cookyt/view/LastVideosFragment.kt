package com.example.cookyt.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookyt.R
import com.example.cookyt.adapter.RecipesListAdapter
import com.example.cookyt.databinding.FragmentLastVideosBinding
import com.example.cookyt.model.Recipe
import com.example.cookyt.room.HistoryController
import com.example.cookyt.room.RecipeController

class LastVideosFragment : Fragment() {
    lateinit var binding: FragmentLastVideosBinding
    lateinit var adapter: RecipesListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_last_videos, container, false)

        adapter = RecipesListAdapter(listOf(), this.requireContext(), "", activity as MainActivity)
        adapter.updateFavoriteSet { getHistoryList() }
        val layoutManager = LinearLayoutManager(this.requireContext())
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.rvRecipes.layoutManager = layoutManager
        binding.rvRecipes.adapter = adapter

        binding.listRefresh.setOnRefreshListener { getHistoryList() }

        return binding.root
    }

    fun getHistoryList() {
        Thread {
            val listRoom = HistoryController.getHistories()
            val list = mutableListOf<Recipe>()
            listRoom.forEach { list.add(it.getRecipe()) }
            (activity as MainActivity).runOnUiThread {
                adapter.updateValues(list)
                binding.listRefresh.isRefreshing = false
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        getHistoryList()
    }
}
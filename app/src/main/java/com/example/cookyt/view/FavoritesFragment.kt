package com.example.cookyt.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookyt.R
import com.example.cookyt.adapter.RecipesListAdapter
import com.example.cookyt.databinding.FragmentFavoritesBinding
import com.example.cookyt.model.Recipe
import com.example.cookyt.room.RecipeController
import com.example.cookyt.view_model.MainActivityViewModel

class FavoritesFragment : Fragment() {
    lateinit var binding: FragmentFavoritesBinding
    lateinit var vm: MainActivityViewModel
    lateinit var adapter: RecipesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        vm = (activity as MainActivity).vm

        adapter = RecipesListAdapter(listOf(), this.requireContext(), "", activity as MainActivity)
        adapter.updateFavoriteSet { getFavorites() }
        binding.rvFavorite.layoutManager = LinearLayoutManager(this.requireContext())
        binding.rvFavorite.adapter = adapter

        return binding.root
    }

    fun getFavorites() {
        Thread {
            val listRoom = RecipeController.getFavorites()
            val list = mutableListOf<Recipe>()
            listRoom.forEach { list.add(it.getRecipe()) }
            (activity as MainActivity).runOnUiThread {
                adapter.updateValues(list)
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        getFavorites()
    }
}
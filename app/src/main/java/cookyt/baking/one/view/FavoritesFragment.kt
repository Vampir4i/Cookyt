package cookyt.baking.one.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cookyt.baking.one.R
import cookyt.baking.one.adapter.RecipesListAdapter
import cookyt.baking.one.databinding.FragmentFavoritesBinding
import cookyt.baking.one.model.Recipe
import cookyt.baking.one.room.RecipeController
import cookyt.baking.one.view_model.MainActivityViewModel

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
        val layoutManager = LinearLayoutManager(this.requireContext())
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.rvFavorite.layoutManager = layoutManager
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
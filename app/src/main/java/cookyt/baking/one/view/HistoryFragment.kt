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
import cookyt.baking.one.databinding.FragmentHistoryBinding
import cookyt.baking.one.model.Recipe
import cookyt.baking.one.room.HistoryController

class HistoryFragment : Fragment() {
    lateinit var binding: FragmentHistoryBinding
    lateinit var adapter: RecipesListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)

        adapter = RecipesListAdapter(listOf(), this.requireContext(), "", activity as MainActivity)
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
package cookyt.baking.one.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cookyt.baking.one.R
import cookyt.baking.one.adapter.RecipesListAdapter
import cookyt.baking.one.databinding.FragmentLastRecipesBinding
import cookyt.baking.one.view_model.MainActivityViewModel


class LastRecipesFragment : Fragment() {
    lateinit var binding: FragmentLastRecipesBinding
    lateinit var vm: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_last_recipes, container, false)
        vm = (activity as MainActivity).vm

        val adapter = RecipesListAdapter(listOf(), this.requireContext(), "", activity as MainActivity)
        binding.rvRecipes.adapter = adapter
        binding.rvRecipes.layoutManager = LinearLayoutManager(this.requireContext())
        adapter.loadNextPage { checkConnection("3", it.toString()) }

        vm.lastRecipes.observe(this.requireActivity(), {
            cookyt.baking.one.App.makeLog("SIZE ${it.size}")
            adapter.updateValues(it)
            binding.listRefresh.isRefreshing = false
        })

        binding.listRefresh.setOnRefreshListener {
            adapter.CURRENT_PAGE = 1
            vm.lastRecipes.value = mutableListOf()
            checkConnection()
        }
        binding.root.findViewById<TextView>(R.id.tv_try_again).setOnClickListener { checkConnection() }

        checkConnection()
        return binding.root
    }

    fun checkConnection(categoryId: String = "3", page: String = "1") {
        if(cookyt.baking.one.App.isOnline()){
            binding.layoutBadInternet.visibility = View.GONE
            binding.rvRecipes.visibility = View.VISIBLE
            vm.getLastRecipes(categoryId, page)
        } else {
            binding.rvRecipes.visibility = View.GONE
            binding.layoutBadInternet.visibility = View.VISIBLE
            binding.listRefresh.isRefreshing = false
        }
    }

}
package cookyt.salads.one.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cookyt.salads.one.App
import cookyt.salads.one.R
import cookyt.salads.one.adapter.CategoryListAdapter
import cookyt.salads.one.databinding.FragmentCategoryBinding
import cookyt.salads.one.view_model.MainActivityViewModel

class CategoryFragment : Fragment() {
    lateinit var binding: FragmentCategoryBinding
    lateinit var vm: MainActivityViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        vm = (activity as MainActivity).vm

        val adapter = CategoryListAdapter(listOf(), this.requireContext())
        binding.rvCategories.layoutManager = LinearLayoutManager(this.requireContext())
        binding.rvCategories.adapter = adapter

        vm.categories.observe(this.requireActivity()) {
            adapter.updateValues(it)
            binding.listRefresh.isRefreshing = false

        }

        binding.listRefresh.setOnRefreshListener { checkConnection(getString(R.string.CATEGORY_ID)) }
        binding.root.findViewById<TextView>(R.id.tv_try_again).setOnClickListener { checkConnection(getString(R.string.CATEGORY_ID)) }

        checkConnection(getString(R.string.CATEGORY_ID))

        return binding.root
    }

    fun checkConnection(categoryId: String) {
        if(App.isOnline()){
            binding.layoutBadInternet.visibility = View.GONE
            binding.rvCategories.visibility = View.VISIBLE
            vm.getCategories(categoryId)
        } else {
            binding.rvCategories.visibility = View.GONE
            binding.layoutBadInternet.visibility = View.VISIBLE
            binding.listRefresh.isRefreshing = false
        }
    }
}
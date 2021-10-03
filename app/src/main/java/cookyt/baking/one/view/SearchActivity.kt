package cookyt.baking.one.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cookyt.baking.one.R
import cookyt.baking.one.adapter.RecipesListAdapter
import cookyt.baking.one.databinding.ActivitySearchBinding
import cookyt.baking.one.view_model.MainActivityViewModel

class SearchActivity : AppCompatActivity() {
    lateinit var vm: MainActivityViewModel
    lateinit var binding: ActivitySearchBinding

    var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = resources.getColor(R.color.dark_orange)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        vm = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.rcRecipes.layoutManager = LinearLayoutManager(this)
        val adapter = RecipesListAdapter(listOf(), this, "", this)
        binding.rcRecipes.adapter = adapter

        vm.searchRecipes.observe(this, {
            adapter.updateValues(it)
            binding.listRefresh.isRefreshing = false
        })

        binding.etSearch.addTextChangedListener {
            searchText = it?.toString() ?: ""
            checkConnection(searchText)
        }

        binding.btnBack.setOnClickListener { finish() }
        binding.listRefresh.setOnRefreshListener { checkConnection(searchText) }
        findViewById<TextView>(R.id.tv_try_again).setOnClickListener { checkConnection(searchText) }
    }

    fun checkConnection(text: String) {
        if(cookyt.baking.one.App.isOnline()){
            binding.layoutBadInternet.visibility = View.GONE
            binding.rcRecipes.visibility = View.VISIBLE
            vm.searchRecipe(text)
        } else {
            binding.rcRecipes.visibility = View.GONE
            binding.layoutBadInternet.visibility = View.VISIBLE
            binding.listRefresh.isRefreshing = false
        }
    }
}
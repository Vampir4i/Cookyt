package com.example.cookyt.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookyt.ClickListener
import com.example.cookyt.R
import com.example.cookyt.RecyclerTouchListener
import com.example.cookyt.adapter.NavigationRVAdapter
import com.example.cookyt.databinding.ActivityMainBinding
import com.example.cookyt.model.NavigationItemModel
import com.example.cookyt.view_model.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NavigationRVAdapter
    private lateinit var binding: ActivityMainBinding
    lateinit var vm: MainActivityViewModel

    private var items = listOf(
        NavigationItemModel(R.drawable.baseline_video_library_24, "Последние видео"),
        NavigationItemModel(R.drawable.baseline_view_list_24, "Категории"),
        NavigationItemModel(R.drawable.baseline_favorite_24, "Избранное")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        vm = ViewModelProvider(this)[MainActivityViewModel::class.java]

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = resources.getColor(android.R.color.holo_green_light)
        }

        vm.action.observe(this, { actionHandler(it) })

        supportFragmentManager.beginTransaction()
            .add(R.id.fl_main, MainContentFragment())
//            .addToBackStack(null)
            .commit()

        binding.rvDrawerItems.layoutManager = LinearLayoutManager(this)
        binding.rvDrawerItems.setHasFixedSize(true)

        binding.ivGamb.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        updateAdapter(0)

        binding.rvDrawerItems.addOnItemTouchListener(RecyclerTouchListener(this, object: ClickListener {
            override fun onClick(view: View, position: Int) {
                when(position) {
                    0 -> {
                        vm.action.value = vm.T_LAST_VIDEOS
                        Log.d("myLog", "ITEM $position")
                    }
                    1 -> {
                        Log.d("myLog", "ITEM $position")
                        vm.action.value = vm.T_CATEGORY
                    }
                    2 -> {
                        Log.d("myLog", "ITEM $position")
                        vm.action.value = vm.T_FAVORITES
                    }
                }
                updateAdapter(position)
                Handler().postDelayed({
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }, 200)
            }

        }))
    }

    private fun updateAdapter(itemPos: Int) {
        adapter = NavigationRVAdapter(items, itemPos)
        binding.rvDrawerItems.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun actionHandler(action: Int) {
        when(action) {
            vm.T_LAST_VIDEOS -> {
                supportFragmentManager.popBackStack()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_main, MainContentFragment().apply {
                        whichPage = 0
                    })
                    .addToBackStack(null)
                    .commit()
            }
            vm.T_CATEGORY -> {
                supportFragmentManager.popBackStack()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_main, MainContentFragment().apply {
                        whichPage = 1
                    })
                    .addToBackStack(null)
                    .commit()
            }
            vm.T_FAVORITES -> {
                supportFragmentManager.popBackStack()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_main, FavoritesFragment())
                    .addToBackStack(null)
                    .commit()
            }

        }
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        finish()
//    }
}
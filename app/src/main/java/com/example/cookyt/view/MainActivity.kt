package com.example.cookyt.view

import android.content.Intent
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
import com.example.cookyt.App
import com.example.cookyt.ClickListener
import com.example.cookyt.R
import com.example.cookyt.RecyclerTouchListener
import com.example.cookyt.adapter.NavigationRVAdapter
import com.example.cookyt.databinding.ActivityMainBinding
import com.example.cookyt.model.NavigationItemModel
import com.example.cookyt.view_model.MainActivityViewModel
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NavigationRVAdapter
    private lateinit var adapter1: NavigationRVAdapter
    private lateinit var binding: ActivityMainBinding
    lateinit var vm: MainActivityViewModel

    private var items = listOf(
        NavigationItemModel(R.drawable.baseline_video_library_24, "Новые видео"),
        NavigationItemModel(R.drawable.baseline_view_list_24, "Категории"),
        NavigationItemModel(R.drawable.baseline_favorite_24, "Избранное"),
        NavigationItemModel(R.drawable.ic_baseline_history_24, "Недавние")
    )
    private var items1 = listOf(
        NavigationItemModel(R.drawable.ic_baseline_rate_review_24, "Оцени нас"),
        NavigationItemModel(R.drawable.ic_baseline_more_24, "Больше"),
        NavigationItemModel(R.drawable.baseline_share_24, "Поделиться"),
        NavigationItemModel(R.drawable.ic_baseline_info_24, "О нас")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        vm = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding.subTitle = ""

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = resources.getColor(R.color.sand_color)
        }

        vm.action.observe(this, { actionHandler(it) })
        binding.clTitle.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.fl_main, MainContentFragment())
//            .addToBackStack(null)
            .commit()

        binding.rvDrawerItems.layoutManager = LinearLayoutManager(this)
        binding.rvDrawerItems.setHasFixedSize(true)
        binding.rvOtherItems.layoutManager = LinearLayoutManager(this)
        binding.rvOtherItems.setHasFixedSize(true)

        App.loadPhoto(R.drawable.top_drawable, binding.drawerImage)
        binding.ivGamb.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        updateAdapter(0)

        binding.rvDrawerItems.addOnItemTouchListener(RecyclerTouchListener(this, object: ClickListener {
            override fun onClick(view: View, position: Int) {
                when(position) {
                    0 -> {
                        vm.action.value = vm.T_LAST_VIDEOS
                        binding.subTitle = ""
                    }
                    1 -> {
                        vm.action.value = vm.T_CATEGORY
                        binding.subTitle = ""
                    }
                    2 -> {
                        vm.action.value = vm.T_FAVORITES
                        binding.subTitle = "Избранное"
                    }
                    3 -> {
                        vm.action.value = vm.T_HISTORY
                        binding.subTitle = "История"
                    }
                }
                updateAdapter(position)
                Handler().postDelayed({
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }, 200)
            }

        }))

        binding.rvOtherItems.addOnItemTouchListener(RecyclerTouchListener(this, object: ClickListener {
            override fun onClick(view: View, position: Int) {
                when(position) {
                    0 -> {
//                        vm.action.value = vm.T_LAST_VIDEOS
                    }
                    1 -> {
//                        vm.action.value = vm.T_CATEGORY
                    }
                    2 -> {
//                        vm.action.value = vm.T_FAVORITES
                    }
                    3 -> {

                    }
                }
                updateAdapter(position + 4)
                Handler().postDelayed({
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }, 200)
            }

        }))
    }

    private fun updateAdapter(itemPos: Int) {
        if(itemPos <= 3) {
            adapter = NavigationRVAdapter(items, itemPos)
            binding.rvDrawerItems.adapter = adapter
            adapter.notifyDataSetChanged()
            adapter1 = NavigationRVAdapter(items1, -1)
            binding.rvOtherItems.adapter = adapter1
            adapter1.notifyDataSetChanged()
        } else {
            adapter = NavigationRVAdapter(items, -1)
            binding.rvDrawerItems.adapter = adapter
            adapter.notifyDataSetChanged()
            adapter1 = NavigationRVAdapter(items1, itemPos - 4)
            binding.rvOtherItems.adapter = adapter1
            adapter1.notifyDataSetChanged()
        }
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
            vm.T_HISTORY -> {
                supportFragmentManager.popBackStack()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_main, HistoryFragment())
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
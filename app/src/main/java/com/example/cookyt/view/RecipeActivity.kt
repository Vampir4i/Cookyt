package com.example.cookyt.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.cookyt.App
import com.example.cookyt.R
import com.example.cookyt.databinding.ActivityRecipeBinding
import com.example.cookyt.model.RecipeRoom
import com.example.cookyt.room.RecipeController
import com.example.cookyt.view_model.MainActivityViewModel

class RecipeActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecipeBinding
    lateinit var vm: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe)
        vm = ViewModelProvider(this)[MainActivityViewModel::class.java]
        val recipeId = intent.getStringExtra("recipe_id") ?: ""

        vm.recipe.observe(this, {
            Log.d("myLog", "RecipeID ${it.id} ${it.title}")
            binding.title = it.title
            binding.body = HtmlCompat.fromHtml(it.description ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
            binding.category = it.sCategory
            App.loadPhoto(it.picture, binding.videoImg)

            Thread() {
                val isFav = RecipeController.checkIsFavorite(it.getRecipeRoom())
                runOnUiThread { binding.isFavorite = isFav }
            }.start()
        })

        binding.btnFavorite.setOnClickListener {
            Log.d("myLog", "CLICK")

            Thread() {
                val isFav = RecipeController.changeFavorite(vm.recipe.value?.getRecipeRoom()
                    ?: RecipeRoom("", "", ""))
                runOnUiThread { binding.isFavorite = isFav }
            }.start()
        }

        binding.btnBack.setOnClickListener { finish() }

        binding.btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "${vm.recipe.value?.title}\n\n" +
                            "Я хотел бы поделиться этим с вами. Здесь вы можете загрузить это приложение из PlayStore\n\n" +
                            "{ссылка}"
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        vm.getRecipe(recipeId)
    }

}
package cookyt.baking.one.view

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import cookyt.baking.one.R
import java.io.InputStream
import java.lang.Exception

class PoliciesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policies)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = resources.getColor(R.color.dark_orange)
        }

        val text = findViewById<TextView>(R.id.text)
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener { finish() }

        try {
            val res: Resources = resources
            val in_s: InputStream = res.openRawResource(R.raw.terms)
            val b = ByteArray(in_s.available())
            in_s.read(b)
            text.text = String(b)
        } catch (e: Exception) {
            text.text = "Error: can't show terms."
        }
    }
}
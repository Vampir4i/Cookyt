package cookyt.baking.one

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.room.Room
import cookyt.baking.one .room.AppDatabase
import com.squareup.picasso.Picasso

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        cookyt.baking.one.App.Companion.context = this
        cookyt.baking.one.App.Companion.database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {
        val TAG = "myLog"
        lateinit var context: Context
        lateinit var database: AppDatabase

        fun makeToast(msg: String) {
            Toast.makeText(cookyt.baking.one.App.Companion.context, msg, Toast.LENGTH_SHORT).show()
        }
        fun makeLog(msg: String) {
            Log.d(cookyt.baking.one.App.Companion.TAG, msg)
        }
        fun loadPhoto(src: String?, iv: ImageView?) {
            if(src.isNullOrEmpty()) return
            Picasso.get().load(src)
                .fit().centerCrop().into(iv)
        }
        fun loadPhoto(src: Int?, iv: ImageView?) {
            if(src == null) return
            Picasso.get().load(src)
                .fit().centerCrop().into(iv)
        }
        fun isOnline(): Boolean {
            val connectivityManager =
                cookyt.baking.one.App.Companion.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val capabilities =
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    } else {
                        return true
                    }
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
            return false
        }
    }
}
package com.example.a12volley

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.MessageQueue
import android.util.JsonReader
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.a12volley.databinding.ActivityMainBinding
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isNetworkConnected()) {

            requestQueue = Volley.newRequestQueue(this)
            fetchImageLoad()

            fetchObjectLoad()
        } else {
            binding.tv.text = "DisConnected"
        }
    }



    private fun fetchObjectLoad() {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,"http://ip.jsontest.com/",null,object:Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject?) {
                val str = response?.getString("ip")
                binding.tv.text = str
            }
        },object:Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                binding.tv.text = error?.message
            }
        })
        requestQueue.add(jsonObjectRequest)
    }



//    ///////////////////////////////////////////////////////////////////////////////////////////////////////////



    private fun fetchImageLoad() {
        val imageRequest = ImageRequest("https://i.imgur.com/Nwk25LA.jpg",object:Response.Listener<Bitmap>{
            override fun onResponse(response: Bitmap?) {
                binding.imageView.setImageBitmap(response)
            }
        },0,0,ImageView.ScaleType.CENTER_CROP,Bitmap.Config.ARGB_8888,object:Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                binding.tv.text = error?.message
            }
        })
        requestQueue.add(imageRequest)
    }



//    /////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private fun isNetworkConnected():Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            return networkCapabilities!=null && networkCapabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo!=null && activeNetworkInfo.isConnected
        }
    }
}
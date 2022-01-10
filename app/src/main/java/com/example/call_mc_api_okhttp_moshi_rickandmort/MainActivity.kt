package com.example.call_mc_api_okhttp_moshi_rickandmort

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.call_mc_api_okhttp_moshi_rickandmort.databinding.ActivityMainBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okio.IOException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val requestFiles = registerForActivityResult(ActivityResultContracts.RequestPermission(), {
            if (it) {
                Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT).show()
                run()
            } else {
                Toast.makeText(applicationContext, "Permission no granted", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        binding.btnFilesPermission.setOnClickListener({
            requestFiles.launch(android.Manifest.permission.INTERNET)
        })


    }


    private val client = OkHttpClient()
    val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()


    fun run() {

        // val url = "https://rickandmortyapi.com/api/character"
        //val url = "https://rickandmortyapi.com/api/character/?name=rick&status=alive"
        val url = "https://rickandmortyapi.com/api/character/1,2"
        //val url = "https://rickandmortyapi.com/api/character/1"

        val request = Request.Builder()
            .url(url)
            .build()

        val adapterAll: JsonAdapter<CharactersAll> = moshi.adapter(CharactersAll::class.java)
        val type = Types.newParameterizedType(List::class.java, Character::class.java)
        val adapterNumber: JsonAdapter<List<Character>> = moshi.adapter(type)

        val adapterOther: JsonAdapter<Character> = moshi.adapter(Character::class.java)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")


                    val body = response.body!!.string()

                    if (body.substring(0, 7) == "{\"info\"") {


                        val data = adapterAll.fromJson(body)

                             if (data != null) {
                                 data.results.forEach {
                                     Log.i("CHARACTER", "Name : " + it.name + " ")
                                 }
                             }

                    } else if (body.substring(0, 7) == "[{\"id\":") {

                        val data = adapterNumber.fromJson(body)
                        if (data != null) {
                            data.forEach{
                                Log.i("CHARACTER", it.name)
                            }
                        }
                    } else {
                        val data = adapterOther.fromJson(body)
                        if (data != null) {
                                Log.i("CHARACTER", "Name : " + data.name + " ")
                        }
                    }


                }
            }
        })


    }

}


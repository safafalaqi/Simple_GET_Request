package com.example.simplegetrequest

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplegetrequest.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var myRv: RecyclerView
    private lateinit var rvAdapter: RVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createApiInterface()
    }

    fun createApiInterface()
    {

        //show progress Dialog
        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        var data:People?=null
        val call: Call<People?>? = apiInterface!!.getUsersInfo()

        call?.enqueue(object : Callback<People?> {
            override fun onResponse(
                call: Call<People?>?,
                response: Response<People?>
            ) {
                progressDialog.dismiss()
                data= response.body()

                val binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)

                data?.let { setRV(it) }
            }
            override fun onFailure(call: Call<People?>, t: Throwable?) {
                Toast.makeText(applicationContext,"Unable to load data!", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                call.cancel()
            }
        })

    }

    private fun setRV(data: People) {
        myRv = findViewById(R.id.rvPeople)
        rvAdapter =RVAdapter(data, this)
        myRv.adapter = rvAdapter
        myRv.layoutManager = LinearLayoutManager(applicationContext)
    }
}
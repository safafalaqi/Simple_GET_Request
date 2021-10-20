package com.example.simplegetrequest

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplegetrequest.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

    fun createApiInterface() {
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

    fun customAlert(v: View) {

            // first we create a variable to hold an AlertDialog builder
            val dialogBuilder =  AlertDialog.Builder(this)
            val editText=EditText(this)
            editText.setInputType(InputType.TYPE_CLASS_TEXT)
            dialogBuilder.setView(editText)

            dialogBuilder.setPositiveButton("Add", DialogInterface.OnClickListener {
                        dialog, id -> addUser(editText.text.toString())
                })
                // negative button text and action
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })
            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Enter a person name")
            //alert.setView(editText)
            // show alert dialog
            alert.show()
    }
    fun addUser(name:String){

        //check if user inputs are not empty
        if(name.isNotEmpty()) {
            val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
            val user = PersonListItem(name)
            val call: Call<PersonListItem> = apiInterface!!.addUsersInfo(user)

            call?.enqueue(object : Callback<PersonListItem?> {
                override fun onResponse(
                    call: Call<PersonListItem?>?,
                    response: Response<PersonListItem?>
                ) {

                    Toast.makeText(applicationContext, "Save Success!", Toast.LENGTH_SHORT).show()
                    createApiInterface()
                }
                override fun onFailure(call: Call<PersonListItem?>, t: Throwable) {
                    Toast.makeText(applicationContext, "Unable to add person.", Toast.LENGTH_SHORT)
                        .show()
                    call.cancel()
                }
            })
        }
        else {
            Toast.makeText(applicationContext, "Please do not leave it empty!", Toast.LENGTH_SHORT).show()
        }
    }

}
package com.brainstem.mvvmapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainstem.mvvmapp.adapter.CommentRecyclerView
import com.brainstem.mvvmapp.adapter.HomeRecyclerViewAdapter
import com.brainstem.mvvmapp.model.Comment
import com.brainstem.mvvmapp.model.Post
import com.brainstem.mvvmapp.repository.Repository
import com.brainstem.mvvmapp.viewmodel.MainViewModel
import com.brainstem.mvvmapp.viewmodel.MainViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.write_comment.*
import kotlinx.coroutines.cancel
import retrofit2.Response

class MainActivity: AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private val adapter by lazy { HomeRecyclerViewAdapter(this) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val searchView: SearchView by lazy { findViewById(R.id.searchView) }
    private val addPostBtn: FloatingActionButton by lazy { findViewById(R.id.floatingActionButton) }
    private var tempList: ArrayList<Post> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setting up recyclerview
        setUpHomeRecyclerViewAdapter()

        //calling data from the repo to display on the view
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.getPost()
        viewModel.myPostResponse.observe(this, { response ->
            if (response.isSuccessful) {
                //response.body()?.let { adapter.setData(it) }
                adapter.setData(response.body())
            } else {
                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
            }
        })

        //setting addPost to floatingButton
        addPostBtn.setOnClickListener{
            showUpdateDialog()
        }

        //implementing searches to filter the title of our post
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                    viewModel.getPost()
                    viewModel.myPostResponse.observe(this@MainActivity, { response ->
                        if (response.isSuccessful) {
                            //response.body()?.let { adapter.setData(it) }
                            adapter.setData(response.body())
                            setUpHomeRecyclerViewAdapter()
                        } else {
                            Toast.makeText(this@MainActivity, response.code(), Toast.LENGTH_SHORT).show()
                        }
                        setUpHomeRecyclerViewAdapter()
                    })
                Toast.makeText(this@MainActivity, "No title search pattern matched", Toast.LENGTH_SHORT).show()

               return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(query: String?): Boolean {
                tempList.clear()

                if (query!!.isNotEmpty()) {
                    adapter.adapterList.forEach {
                        if (it.title.contains(query)) {
                            tempList.add(it)
                        }
                    }
                    adapter.setData(tempList)
                    adapter.notifyDataSetChanged()
                } else {

                    viewModel.getPost()
                    viewModel.myPostResponse.observe(this@MainActivity, { response ->
                        if (response.isSuccessful) {
                            //response.body()?.let { adapter.setData(it) }
                            adapter.setData(response.body())
                            setUpHomeRecyclerViewAdapter()
                        } else {
                            Toast.makeText(this@MainActivity, response.code(), Toast.LENGTH_SHORT).show()
                        }
                        setUpHomeRecyclerViewAdapter()
                    })
                }

                return false
            }

        })
    }

    private fun setUpHomeRecyclerViewAdapter() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    @SuppressLint("SetTextI18n")
    private fun showUpdateDialog() {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(this)

        // set the custom layout
        val view = layoutInflater.inflate(R.layout.write_post, null)

        // Get Views references from your layout
        val editId: EditText = view.findViewById(R.id.edit_id)
        val editUserId: EditText = view.findViewById(R.id.edit_userId)
        val editTitle: EditText = view.findViewById(R.id.edit_title)
        val editBody: EditText = view.findViewById(R.id.edit_body)
        val cancelButton: Button = view.findViewById(R.id.cancel)
        val postButton: Button = view.findViewById(R.id.post)

        //dialog?.setTitle("Post Page")

        //on back button press
        cancelButton.setOnClickListener {
            val cancelIntent = Intent(this,MainActivity::class.java)
            startActivity(cancelIntent)
        }

        postButton.setOnClickListener{
            val id = editId.text.toString().trim()
            val userId = editUserId.text.toString().trim()
            val title = editTitle.text.toString().trim()
            val body = editBody.text.toString().trim()

            //check for empty input
            if(id.isEmpty() && userId.isEmpty() && title.isEmpty() && body.isEmpty()){
                Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else{
                if (id.isEmpty()){
                    Toast.makeText(this, "ID is required", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if (userId.isEmpty()){
                    Toast.makeText(this, "Uer Id is required", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if (title.isEmpty()){
                    Toast.makeText(this, "Title is required", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if (body.isEmpty()){
                    Toast.makeText(this, "Post body is required", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }
            //updating Contact
            postForm(id, userId, title, body)
            postButton.text = "Saved Successfully"
            //dialog?.dismiss()

            //Load Homepage
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }

        builder.setView(view)

        // create and show the alert dialog
        dialog = builder.create()
        dialog.show()
    }

    //function to add post
    private fun postForm(id: String, userId: String, title: String, body: String) {
        val postItem = Post(
                                id = id.toInt(),
                                userId = userId.toInt(),
                                title = title,
                                body = body
                            )
        adapter.adapterList.add(postItem)
        adapter.setData(adapter.adapterList)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.viewModelScope.cancel()
    }
}
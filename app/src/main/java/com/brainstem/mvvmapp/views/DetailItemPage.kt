package com.brainstem.mvvmapp.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainstem.mvvmapp.MainActivity
import com.brainstem.mvvmapp.R
import com.brainstem.mvvmapp.adapter.CommentRecyclerView
import com.brainstem.mvvmapp.model.Comment
import com.brainstem.mvvmapp.repository.Repository
import com.brainstem.mvvmapp.viewmodel.MainViewModel
import com.brainstem.mvvmapp.viewmodel.MainViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton


class DetailItemPage : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private  val adapter by lazy {   CommentRecyclerView() }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.detail_comment_reclerview) }

    //instantiating views
    private val backButton: ImageButton by lazy { findViewById(R.id.backButton) }
    private val titleView: TextView by lazy { findViewById(R.id.detail_textview_title) }
    private val bodyView: TextView by lazy { findViewById(R.id.detail_textview_userbody) }
    private  val addCommentBtn by lazy { findViewById<FloatingActionButton>(R.id.floatingActionButton2) }
    private val commentAdapter by lazy { CommentRecyclerView() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_item_page)

        backButton.setOnClickListener{
            onBackPressed()
        }

        //getting data from intentToPassDataToDetailItemPage

        val idOfIntent = intent.getStringExtra("id").toString()
        var userIdOfIntent = intent.getStringExtra("userId").toString()
        val titleOfIntent = intent.getStringExtra("title")
        val bodyOfIntent = intent.getStringExtra("body")

        titleView.text = titleOfIntent
        bodyView.text = bodyOfIntent


        setUpCommentRecyclerViewAdapter()

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        // make postId dynamic later on [todo]
        viewModel.getComment(idOfIntent.toInt(), "id", "desc")
        viewModel.myCommentResponse.observe(this, { response ->
            if (response.isSuccessful){
                response.body()?.let { adapter.passDataToMainView(it) }
            }else{
                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
            }
        })

        //setting add comment to floatingButton
        addCommentBtn.setOnClickListener{
            postCommentDialog()
        }
    }

    private fun setUpCommentRecyclerViewAdapter(){
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    //comment dialog display
    @SuppressLint("SetTextI18n")
    private fun postCommentDialog() {
        var dialog2: AlertDialog? = null
        val builder2 = AlertDialog.Builder(this)

        // set the custom layout
        val view = layoutInflater.inflate(R.layout.write_post, null)

        // Get Views references from your layout
        val commentId: EditText = view.findViewById(R.id.edit_commentid)
        val commentPostId: EditText = view.findViewById(R.id.edit_postid)
        val commentName: EditText = view.findViewById(R.id.edit_commentName)
        val commentEmail: EditText = view.findViewById(R.id.edit_commentEmail)
        val commentBody: EditText = view.findViewById(R.id.edit_commentBody)
        val cancelButton2: Button = view.findViewById(R.id.cancel2)
        val postButton2: Button = view.findViewById(R.id.post2)

        dialog2?.setTitle("Comment Box")

        //on back button press
        cancelButton2.setOnClickListener {
            val cancelIntent2 = Intent(this, MainActivity::class.java)
            startActivity(cancelIntent2)
        }

        postButton2.setOnClickListener{
            val cId = commentId.text.toString().trim()
            val cPostId = commentPostId.text.toString().trim()
            val cName = commentName.text.toString().trim()
            val cEmail = commentEmail.text.toString().trim()
            val cBody = commentBody.text.toString().trim()

            //check for empty input
            if(cId.isEmpty() && cPostId.isEmpty() && cName.isEmpty() && cEmail.isEmpty() && cBody.isEmpty()){
                Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else{
                if (cId.isEmpty()){
                    Toast.makeText(this, "Comment Id is required", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if (cPostId.isEmpty()){
                    Toast.makeText(this, "Post Id is required", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if (cName.isEmpty()){
                    Toast.makeText(this, "Your name is required", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if (cEmail.isEmpty()){
                    Toast.makeText(this, "Your email is required", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if (cBody.isEmpty()) {
                    Toast.makeText(this, "Comment body is required", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }
            //updating Comment
            postComment(cId, cPostId, cName, cEmail, cBody)
            postButton2.text = "Saved Successfully"
            //dialog?.dismiss()

            //Load Homepage
            val intent2 = Intent(this, MainActivity::class.java)
            startActivity(intent2)

        }
        // create and show the alert dialog
        builder2.setView(view)
        dialog2 = builder2.create()
        dialog2.show()
    }

    private fun postComment(id: String, postId: String, name: String, email: String, body: String) {
        val commentItem = Comment(
            id = id.toInt(),
            postId = postId.toInt(),
            name = name,
            email = email,
            body = body
        )
        commentAdapter.commentAdapterList.add(commentItem)
        commentAdapter.passDataToMainView(commentAdapter.commentAdapterList)
        adapter.notifyDataSetChanged()
    }
}
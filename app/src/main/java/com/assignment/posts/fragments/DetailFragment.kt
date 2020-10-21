package com.assignment.posts.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment.posts.R
import com.assignment.posts.activities.BaseActivity
import com.assignment.posts.room.Comments
import com.assignment.posts.room.Post
import com.assignment.posts.viewmodel.CommentsViewModel
import com.assignment.posts.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.comments_item.*


class DetailFragment : Fragment() {

    val commentsViewModel by activityViewModels<CommentsViewModel>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    var commentsData: List<Comments> = emptyList()
    var idOfPost: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_detail, container, false)

        idOfPost =commentsViewModel.id
        recyclerView = view.findViewById(R.id.recyclerview)
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = RecyclerAdapter()

        commentsViewModel.getAllCommentsAsLiveData().observe(
            viewLifecycleOwner,
            { posts ->
                if(!posts.isNullOrEmpty()) {
                    commentsData = posts.filter { idOfPost == idOfPost }
                    activity?.runOnUiThread {
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                } else {
                    commentsViewModel.callCommentsApi(commentsViewModel.id)
                }
            }
        )
        return view
    }

    override fun onResume() {
        super.onResume()
    }

    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.CommentsViewHolder>()  {
        inner class CommentsViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

            private var view: View = v
            private var poastId: TextView
            private var commentId: TextView
            private var name: TextView
            private var email: TextView
            private var body: TextView
            private var ivFav: ImageView
            private  var llSide: LinearLayout

            init {
                poastId = v.findViewById(R.id.postId)
                commentId = v.findViewById(R.id.commentId)
                name = v.findViewById(R.id.name)
                email = v.findViewById(R.id.email)
                body = v.findViewById(R.id.body)
                ivFav = v.findViewById(R.id.iv_fav)
                llSide = v.findViewById(R.id.ll_side)

                v.setOnClickListener(this)
            }

            fun bind(comments: Comments){
                poastId.text = "Post ID: " +comments.postId
                commentId.text = "Comment ID: " +comments.id
                name.text = "Name: " +comments.name
                email.text = "Email: " +comments.email
                body.text = "Body: " +comments.body


                commentsViewModel.poastId = commentsData.get(position).postId.toString()

                llSide.background.setTint(resources.getColor(R.color.blue))

                if(comments.isFavorite!!){
                    ivFav.setColorFilter(Color.RED)
                } else {
                    ivFav.setColorFilter(Color.GRAY)
                }

                ivFav.setOnClickListener {
                    ivFav.setColorFilter(Color.RED)
                    commentsViewModel.addOrRemoveFavourite(comments)
                    Toast.makeText(activity, "Added to Favourites", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onClick(v: View) {
                Log.d("RecyclerView", "CLICK!")
            }

        }

        override fun getItemCount(): Int {
            return commentsData.size
        }

        override fun onBindViewHolder(holder: RecyclerAdapter.CommentsViewHolder, position: Int) {
            holder.bind(commentsData.get(position))
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.CommentsViewHolder {
            val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.comments_item, parent, false)
            return CommentsViewHolder(inflatedView)
        }
    }

    override fun onAttach(context: Context) {
        (activity as BaseActivity)?.hideBottomNavigation()
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        (activity as BaseActivity)?.showBottomNavigation()
    }


}
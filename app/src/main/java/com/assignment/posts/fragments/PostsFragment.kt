package com.assignment.posts.fragments

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment.posts.R
import com.assignment.posts.room.Post
import com.assignment.posts.room.RoomClient
import com.assignment.posts.utils.Communicator
import com.assignment.posts.viewmodel.CommentsViewModel
import com.assignment.posts.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.fragment_favourites.*

class PostsFragment : Fragment() {

    val postViewModel by activityViewModels<PostViewModel>()
    val commentsViewModel by activityViewModels<CommentsViewModel>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    var postData: List<Post> = emptyList()
    private lateinit var communicator: Communicator
    var postsId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_posts, container, false)

        recyclerView = view.findViewById(R.id.recyclerview)
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = RecyclerAdapter()

        communicator = activity as Communicator

        postViewModel.getAllPostAsLiveData().observe(
            viewLifecycleOwner,
            { posts ->
                if (!posts.isNullOrEmpty()) {
                    postData = posts
                    activity?.runOnUiThread {
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                } else {
                    postViewModel.callPostsApi()
                }
            }
        )
        return view
    }




    override fun onResume() {
        super.onResume()
    }

    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.PostViewHolder>() {
        inner class PostViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

            private var view: View = v
            private var titleTextView: TextView
            private var uidTextView: TextView
            private var bodyTextView: TextView
            private var ivFav: ImageView
            private var llSide: LinearLayout

            init {
                titleTextView = v.findViewById(R.id.title)
                uidTextView = v.findViewById(R.id.uid)
                bodyTextView = v.findViewById(R.id.body)
                ivFav = v.findViewById(R.id.iv_fav)
                llSide = v.findViewById(R.id.ll_side)
                v.setOnClickListener(this)
            }

            fun bind(post: Post) {
                uidTextView.text = "User ID: " + post.userId
                titleTextView.text = "Post Title: " + post.title
                bodyTextView.text = "Post Body: " + post.body
                postsId = post.id.toString()

                llSide.background.setTint(resources.getColor(R.color.colorPrimary))

                if (post.isFavorite!!) {
                    ivFav.setColorFilter(Color.RED)
                } else {
                    ivFav.setColorFilter(Color.GRAY)
                }

                ivFav.setOnClickListener {
                    ivFav.setColorFilter(Color.RED)
                    postViewModel.addOrRemoveFavourite(post)
                    Toast.makeText(activity, "Added to Favourites", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onClick(v: View) {
                Log.d("RecyclerView", "CLICK!")
                findNavController().navigate(R.id.action_postsFragment_to_detailFragment)
                // communicator.passDataCom(postsId)
                commentsViewModel.id = postData.get(position).id.toString()
            }

        }

        override fun getItemCount(): Int {
            return postData.size
        }

        override fun onBindViewHolder(holder: RecyclerAdapter.PostViewHolder, position: Int) {
            holder.bind(postData.get(position))
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerAdapter.PostViewHolder {
            val inflatedView =
                LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
            return PostViewHolder(inflatedView)
        }
    }

}
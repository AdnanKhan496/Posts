package com.assignment.posts.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment.posts.R
import com.assignment.posts.room.Post
import com.assignment.posts.viewmodel.PostViewModel


class FavouritesFragment : Fragment() {

    val postViewModel by activityViewModels<PostViewModel>()

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView

    var postData: List<Post> = emptyList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_favourites, container, false)

        recyclerView = view.findViewById(R.id.recyclerview)

        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = RecyclerAdapter()

        postViewModel.getFavouriteAsLiveData().observe(
            viewLifecycleOwner,
            { posts ->
                if(!posts.isNullOrEmpty()) {
                    postData = posts
                    activity?.runOnUiThread {
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                }
            }
        )

        return view
    }

    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.PostViewHolder>()  {

        inner class PostViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

            private var view: View = v
            private var titleTextView: TextView
            private var uidTextView: TextView
            private var bodyTextView: TextView
            private var ivFav: ImageView

            init {
                titleTextView = v.findViewById(R.id.title)
                uidTextView = v.findViewById(R.id.uid)
                bodyTextView = v.findViewById(R.id.body)
                ivFav = v.findViewById(R.id.iv_fav)
                v.setOnClickListener(this)
            }

            fun bind(post: Post){
                uidTextView.text = "User ID: " +post.userId
                titleTextView.text = "Post Title: " +post.title
                bodyTextView.text = "Post Body: " +post.body

                ivFav.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red),  android.graphics.PorterDuff.Mode.SRC_IN)
                ivFav.setOnClickListener {
                    ivFav.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bg),  android.graphics.PorterDuff.Mode.SRC_IN)

                    postViewModel.addOrRemoveFavourite(post)
                    Toast.makeText(activity,"Removed from Favourites",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onClick(v: View) {
                Log.d("RecyclerView", "CLICK!")
            }

        }

        override fun getItemCount(): Int {
            return postData.size
        }

        override fun onBindViewHolder(holder: RecyclerAdapter.PostViewHolder, position: Int) {
            holder.bind(postData.get(position))
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.PostViewHolder {
            val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
            return PostViewHolder(inflatedView)
        }
    }
}
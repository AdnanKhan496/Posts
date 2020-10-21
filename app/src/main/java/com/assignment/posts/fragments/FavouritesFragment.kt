package com.assignment.posts.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment.posts.R
import com.assignment.posts.room.Post
import com.assignment.posts.viewmodel.CommentsViewModel
import com.assignment.posts.viewmodel.PostViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_favourites.*


class FavouritesFragment : Fragment() {

    val postViewModel by activityViewModels<PostViewModel>()
    val commentsViewModel by activityViewModels<CommentsViewModel>()

    lateinit var navController: NavController
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    lateinit var tv_fav_empty: TextView
    var postData: List<Post> = emptyList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_favourites, container, false)



        recyclerView = view.findViewById(R.id.recyclerview)
        tv_fav_empty = view.findViewById(R.id.tv_fav_is_empty)
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = RecyclerAdapter()

        postViewModel.getFavouriteAsLiveData().observe(
            viewLifecycleOwner,
            { posts ->
                if (!posts.isNullOrEmpty()) {
                    tv_fav_empty.visibility = View.GONE
                    postData = posts
                    activity?.runOnUiThread {
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                } else {
                    tv_fav_empty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE

                }
            }
        )

        return view
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

                ivFav.setColorFilter(Color.RED)
                llSide.background.setTint(Color.RED)

                ivFav.setOnClickListener {
                    ivFav.setColorFilter(Color.GRAY)
                    llSide.background.setTint(Color.GRAY)
                    postViewModel.addOrRemoveFavourite(post)
                    Toast.makeText(activity, "Removed from Favourites", Toast.LENGTH_SHORT).show()

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
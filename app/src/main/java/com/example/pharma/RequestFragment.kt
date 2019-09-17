package com.example.pharma

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pharma.AdapterItems.RequestItem
import com.example.pharma.Model.Request
import com.example.pharma.Utils.IMAGE_MODAL_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_request.*


class RequestFragment : Fragment() {
    companion object {
        val requestAdapter = GroupAdapter<ViewHolder>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_request, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateRequestList()

        // Setting up the listener for the refresher
        requestSwipeContainer.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                updateRequestList()
            }
        })

        request_list.adapter = requestAdapter

        requestAdapter.setOnItemClickListener {item, view ->
            val imageUrl = (item as RequestItem).request.imageUrl
            val imageIntent = Intent(activity, RequestModalActivity::class.java)
            imageIntent.putExtra(IMAGE_MODAL_URL, imageUrl)
            startActivity(imageIntent)
        }

        add_request.setOnClickListener {
            val requestIntent = Intent(activity, AddRequestModalActivity::class.java)
            startActivity(requestIntent)
        }
    }

    private fun updateRequestList() {
        val requestRef = FirebaseDatabase.getInstance().getReference("/requests")
        val uid = FirebaseAuth.getInstance().uid
        requestRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                requestAdapter.clear()
                p0.children.forEach {
                    val request = it.getValue(Request::class.java)
                    if (request != null && request.uid == uid) {
                        requestAdapter.add(RequestItem(request))
                    }
                }
                requestSwipeContainer.isRefreshing = false
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.d(ContentValues.TAG, "Cannot load pharmacie list")
            }
        })
    }
}

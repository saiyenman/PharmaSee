package com.example.pharma.AdapterItems

import android.graphics.Color
import com.example.pharma.Model.Request
import com.example.pharma.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.request_item.view.*

class RequestItem(val request: Request): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.request_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.request_item_description.text = request.descritpion
        viewHolder.itemView.request_item_status.text = request.status
        when (request.status) {
            "Reçu" -> viewHolder.itemView.request_item_status.setTextColor(Color.RED)
            "Traitement" -> viewHolder.itemView.request_item_status.setTextColor(Color.rgb(255,216,0))
            "Terminé" -> viewHolder.itemView.request_item_status.setTextColor(Color.GREEN)
        }
        val targetImageView = viewHolder.itemView.request_image
        Picasso.get().load(request.imageUrl).into(targetImageView)
    }
}
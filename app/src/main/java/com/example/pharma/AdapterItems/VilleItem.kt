package com.example.pharma.AdapterItems

import com.example.pharma.Model.Ville
import com.example.pharma.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.ville_choice_item.view.*

class VilleItem(val ville: Ville): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.ville_choice_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.ville_number.text = ville.id.toString()
        viewHolder.itemView.ville_name.text = ville.nom
    }
}
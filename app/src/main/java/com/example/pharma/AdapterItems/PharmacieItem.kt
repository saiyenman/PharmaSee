package com.example.pharma.AdapterItems

import com.example.pharma.Model.Pharmacie
import com.example.pharma.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.pharmacie_item.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PharmacieItem(val pharmacie: Pharmacie) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.pharmacie_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.adresse_item.text = pharmacie.adresse
        viewHolder.itemView.telephone_item.text = pharmacie.telephone
        val state_image = viewHolder.itemView.state_item

        if (checkOpen()) {
            state_image.setImageResource(R.drawable.button_pharmacie_open)
        } else {
            state_image.setImageResource(R.drawable.button_pharmacie_closed)
        }
    }

    fun checkOpen(): Boolean {
        val HorairesSplitted = pharmacie.horaires.split("-")
        val openTimeSplitted = HorairesSplitted.get(0).split(":")
        val closeTimeSplitted = HorairesSplitted.get(1).split(":")

        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formatted = currentTime.format(formatter).split(":")
        // Convert to minutes and compare values
        val start = openTimeSplitted.get(0).toInt() * 60 + openTimeSplitted.get(1).toInt()
        val end = closeTimeSplitted.get(0).toInt() * 60 + closeTimeSplitted.get(1).toInt()
        val current = formatted.get(0).toInt()* 60 + formatted.get(1).toInt()

        if (current >= start && current <= end) {
            return true
        }
        return false
    }
}
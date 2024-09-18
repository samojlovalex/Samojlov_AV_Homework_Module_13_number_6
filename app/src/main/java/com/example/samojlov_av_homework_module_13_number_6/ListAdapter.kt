package com.example.samojlov_av_homework_module_13_number_6

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListAdapter(context: Context, listItem: MutableList<Product>) :
    ArrayAdapter<Product>(context, R.layout.list_item, listItem) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val product = getItem(position)
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val name = view?.findViewById<TextView>(R.id.nameItemTV)
        val price = view?.findViewById<TextView>(R.id.priceItemTV)
        val weight = view?.findViewById<TextView>(R.id.weightItemTV)

        name?.text = product?.name
        price?.text = "${product?.price} руб."
        weight?.text = "${product?.weight} г"

        return view!!
    }
}
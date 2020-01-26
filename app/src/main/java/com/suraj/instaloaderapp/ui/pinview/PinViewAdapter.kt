package com.suraj.instaloaderapp.ui.pinview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.suraj.instaloader.InstaLoader
import com.suraj.instaloaderapp.R
import com.suraj.instaloaderapp.datastore.model.PinView
import com.suraj.instaloaderapp.utils.loadImage

class PinViewAdapter : RecyclerView.Adapter<PinViewAdapter.PinViewHolder>() {

    private var pinViewList: MutableList<PinView> = mutableListOf()

    fun addNews(pinViews: MutableList<PinView>) {
        if (pinViewList.isNotEmpty()) {
            pinViewList.clear()
        }
        this.pinViewList = pinViews
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinViewHolder {
        return PinViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_cell_pinview, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return if (pinViewList.size > 0) {
            pinViewList.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: PinViewHolder, position: Int) {
        holder.bind(pinViewList[position])
    }

    class PinViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private var parentConstrain = itemView.findViewById<ConstraintLayout>(R.id.parentContsraint)
        private var imgPinView = itemView.findViewById<ImageView>(R.id.imgPinView)

        private var parent = itemView

        fun bind(view: PinView) {
            Log.e("URLS",">>>>>>>>>>>>"+view.profileImage)
            InstaLoader.getInstance().source(view.profileImage!!)
                .into(imgPinView)
        //    loadImage(imgPinView,view.profileImage ?: "")

//            txvNewsSource.text = view.source
//            txvDate.text = convertDate(view.publishedAt)
//            txvTitle.text = view.title
//
//            loadImage(imvNews, view.urlToImage)
//
//            itemView.setOnClickListener {
//                parent.context.startActivity(DetailActivity.newIntent(parent.context, view.title))
//            }
        }

    }
}
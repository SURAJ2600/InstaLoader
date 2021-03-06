package com.suraj.instaloaderapp.ui.pinview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.suraj.instaloader.InstaLoader
import com.suraj.instaloaderapp.R
import com.suraj.instaloaderapp.datastore.model.PinView
import com.suraj.instaloaderapp.ui.fullview.FullImageViewActivity

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


        /*
        * Cancel the image loader request
        *
        * */
        if(position==0){

            InstaLoader.getInstance().cancelSingleRequest(pinViewList[position].profileImage ?: "")
        }
    }

    class PinViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private var parentConstrain = itemView.findViewById<ConstraintLayout>(R.id.parentContsraint)
        private var imgPinView = itemView.findViewById<ImageView>(R.id.imgPinView)
        private var txtName = itemView.findViewById<TextView>(R.id.txtName)

        private var parent = itemView

        fun bind(view: PinView) {
            var mHeight=0
            var mWidth=0

            /*
            *
            * Loading image with the use of @InstaLoader
            * */

            val vto = imgPinView.viewTreeObserver
            vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    imgPinView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    mHeight = imgPinView.height // This will return actual height.
                    mWidth = imgPinView.width // This will return actual width.
                }
            })

            InstaLoader.getInstance().source(view.profileImage!!)
                .resize(mWidth,mHeight)
                .into(imgPinView)
            txtName.text="${view.name}"


            itemView.setOnClickListener {
                parent.context.startActivity(FullImageViewActivity.newIntent(parent.context, view.profileImage ?: ""))
            }
            txtName.setOnClickListener { itemView.performClick() }
            parentConstrain.setOnClickListener { itemView.performClick() }
            imgPinView.setOnClickListener { itemView.performClick() }



        }


    }
}
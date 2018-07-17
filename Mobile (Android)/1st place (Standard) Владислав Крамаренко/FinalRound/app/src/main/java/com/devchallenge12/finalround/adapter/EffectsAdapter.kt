package com.devchallenge12.finalround.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devchallenge12.finalround.Constants
import com.devchallenge12.finalround.R
import com.devchallenge12.finalround.callbacks.OnEffectSelectedCallback
import com.devchallenge12.finalround.utils.Utils
import kotlinx.android.synthetic.main.effect_item.view.*

class EffectsAdapter(val images: ArrayList<Int>, val onEffectSelectedCallback: OnEffectSelectedCallback): RecyclerView.Adapter<EffectsAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = Holder(LayoutInflater.from(parent!!.context).inflate(R.layout.effect_item, parent, false))
    override fun getItemCount() = images.size
    override fun onBindViewHolder(holder: Holder?, position: Int) = holder!!.bind(images[position], onEffectSelectedCallback)
    class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(imageResId: Int, onEffectSelectedCallback: OnEffectSelectedCallback){
            itemView.imageView.setImageResource(imageResId)
            itemView.imageView.setOnClickListener {
                Utils.imagesRes = imageResId

                Utils.mode = when (imageResId){
                    R.drawable.mustache1 -> Constants.MUSTACHE_MODE
                    R.drawable.mustache2 -> Constants.MUSTACHE_MODE
                    R.drawable.mustache3 -> Constants.MUSTACHE_MODE
                    R.drawable.mustache4 -> Constants.MUSTACHE_MODE
                    R.drawable.mustache6 -> Constants.MUSTACHE_MODE

                    R.drawable.glasses1 -> Constants.GLASSES_MODE
                    R.drawable.glasses2 -> Constants.GLASSES_MODE
                    R.drawable.glasses3 -> Constants.GLASSES_MODE
                    R.drawable.glasses4 -> Constants.GLASSES_MODE
                    R.drawable.glasses5 -> Constants.GLASSES_MODE
                    R.drawable.glasses6 -> Constants.GLASSES_MODE
                    R.drawable.glasses7 -> Constants.GLASSES_MODE
                    R.drawable.glasses8 -> Constants.GLASSES_MODE
                    R.drawable.glasses9 -> Constants.GLASSES_MODE
                    R.drawable.glasses10 -> Constants.GLASSES_MODE
                    R.drawable.glasses11 -> Constants.GLASSES_MODE

                    R.drawable.lips1 -> Constants.LIPS_MODE
                    R.drawable.lips2 -> Constants.LIPS_MODE
                    R.drawable.lips3 -> Constants.LIPS_MODE

                    else -> Constants.MUSTACHE_MODE
                }
                onEffectSelectedCallback.onSelected()
            }
        }
    }
}
package com.example.dog.util

import android.content.Context
import android.transition.CircularPropagation
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dog.R

val PERMISSION_SEND_SMS=1234

fun getProgessDrawable(context:Context):CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth=10f
        centerRadius=50f
        start()
    }
}

fun ImageView.loadImage(url:String?,progessDrawable: CircularProgressDrawable){
    val options=RequestOptions()
        .placeholder(progessDrawable)
        .error(R.mipmap.ic_dog_icon)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}
@BindingAdapter("android:imageUrl")
//used to refern the layout
fun loadImage(view:ImageView,url:String){
    view.loadImage(url, getProgessDrawable(view.context))
}
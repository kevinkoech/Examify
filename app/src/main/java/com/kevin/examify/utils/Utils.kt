package com.kevin.examify.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kevin.examify.R
import java.util.*


enum class UserType { Admin, User }

enum class DataState{ LOADING, SUCCESS, ERROR }

enum class FileType { IMAGE, FILE }

/* set Cover Url */
fun ImageView.loadCover(coverUrl : String?){
    val option = RequestOptions().placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher)
    Glide.with(context)
        .setDefaultRequestOptions(option)
        .load(coverUrl)
        .into(this)
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.GONE
}


fun showToast(context: Context,message: String){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}

//internal fun generateId(): String {
//    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
//    fun getStr(l: Int): String = (1..l).map { allowedChars.random() }.joinToString("")
//    return getStr(8) // enter the length of id
//}

internal fun generateId() = UUID.randomUUID().toString()



fun getCurrentTime(): Date = Calendar.getInstance().time

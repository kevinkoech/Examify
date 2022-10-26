package com.kevin.examify.data

import android.os.Parcelable
import com.kevin.examify.utils.getCurrentTime
import kotlinx.parcelize.Parcelize
import java.util.*


// just use data class, the data is initialized so you do not need to add constructor.
@Parcelize
data class PdfData(
    var id: String = "",
    var folderId: String = "",
    var createDate: Date = getCurrentTime(),
    var title: String = "",
    var url:String = "",
    val idRead: Boolean = false
): Parcelable {
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "folderId" to folderId,
            "createDate" to createDate,
            "title" to title,
            "url" to url,
        )
    }
}



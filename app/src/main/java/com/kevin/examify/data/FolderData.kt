package com.kevin.examify.data

import android.os.Parcelable
import com.kevin.examify.utils.getCurrentTime
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

// I set here a data class it will be easy to handle
@Parcelize
data class FolderData(
    /* set data */
    var id: String = "",
    var createDate: Date = getCurrentTime(),
    var title: String = "",
    var pdfsCount: Int = 0,
    var isFavorite: Boolean = false,
    var pdfs: List<PdfData>  = ArrayList()

): Parcelable {
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "createDate" to createDate,
            "title" to title,
            "pdfsCount" to pdfsCount,
            "isFavorite" to isFavorite,
            "pdfs" to pdfs,
        )
    }
}






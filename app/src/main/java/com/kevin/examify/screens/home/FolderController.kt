package com.kevin.examify.screens.home

import com.airbnb.epoxy.TypedEpoxyController
import com.kevin.examify.data.FolderData
import com.kevin.examify.folder


class FolderController : TypedEpoxyController<List<FolderData>>() {

    companion object{
    }

    interface ClickListener {
        fun onClick(folder: FolderData)
    }

    lateinit var clickListener: ClickListener


    override fun buildModels(data: List<FolderData>?) {
        data?.forEachIndexed { _, folder ->

            folder {
                id(folder.id)
                folder(folder)
                onClick { v->
                    clickListener.onClick(folder)
                }
            }


        }
    }



}
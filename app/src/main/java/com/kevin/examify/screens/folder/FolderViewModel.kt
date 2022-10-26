package com.kevin.examify.screens.folder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.examify.data.FolderData
import com.kevin.examify.repository.PdfReaderRepository
import kotlinx.coroutines.launch

class FolderViewModel : ViewModel() {

    private val repository = PdfReaderRepository()


    private val _folder = MutableLiveData<FolderData>()
    val folder: LiveData<FolderData> = _folder



    fun getFolder(id: String){
        viewModelScope.launch {
            _folder.value = repository.getFolderById(id)
        }
    }



}
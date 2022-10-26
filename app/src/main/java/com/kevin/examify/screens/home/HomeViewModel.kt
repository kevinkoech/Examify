package com.kevin.examify.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.examify.data.FolderData
import com.kevin.examify.repository.PdfReaderRepository
import com.kevin.examify.utils.DataState
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = PdfReaderRepository()

    // life data
//    val folders = repository.observeFolders

    private val _inProgress = MutableLiveData<DataState?>()
    val inProgress: LiveData<DataState?> = _inProgress

    private val _folders = MutableLiveData<List<FolderData>>()
    val folders: LiveData<List<FolderData>> = _folders




    fun getFolders(){
        _inProgress.value = DataState.LOADING
        viewModelScope.launch {
            _folders.value = repository.getFolders()
            _inProgress.value = DataState.SUCCESS

        }

    }



}
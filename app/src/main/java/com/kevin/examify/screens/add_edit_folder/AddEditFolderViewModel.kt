package com.kevin.examify.screens.add_edit_folder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.examify.data.FolderData
import com.kevin.examify.repository.PdfReaderRepository
import com.kevin.examify.utils.DataState
import kotlinx.coroutines.launch

class AddEditFolderViewModel : ViewModel() {

    private val repository = PdfReaderRepository()
    val folders = repository.observeFolders

    private val _inProgress = MutableLiveData<DataState?>()
    val inProgress: LiveData<DataState?> = _inProgress

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    init {
        _inProgress.value = null
    }


    fun addFolder(folder: FolderData) {
        _inProgress.value = DataState.LOADING
        viewModelScope.launch {
            repository.addFolder(folder)
                .addOnSuccessListener {
                    _inProgress.value = DataState.SUCCESS
                }
                .addOnFailureListener { e ->
                    _inProgress.value = DataState.ERROR
                }
        }
    }


    fun updateFolder(folder: FolderData) {
        _inProgress.value = DataState.LOADING
        viewModelScope.launch {
           repository.updateFolder(folder)
               .addOnSuccessListener {
                   _inProgress.value = DataState.SUCCESS

               }
               .addOnFailureListener { e ->
                   _inProgress.value = DataState.ERROR

               }
        }
    }


    fun deleteFolder(folder: FolderData) {
        _inProgress.value = DataState.LOADING
        viewModelScope.launch {
            repository.deleteFolder(folder)
            .addOnSuccessListener {
                _inProgress.value = DataState.SUCCESS
            }
            .addOnFailureListener { e ->
                _inProgress.value = DataState.ERROR
            }
        }
    }



    fun resetData(){
        _inProgress.value = null
        _error.value = null
    }







}
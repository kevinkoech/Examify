package com.kevin.examify.screens.pdf_viewer

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.examify.data.PdfData
import com.kevin.examify.repository.PdfReaderRepository
import com.kevin.examify.utils.DataState
import kotlinx.coroutines.launch

class PdfViewerViewModel: ViewModel() {

    companion object {
        private const val TAG = "PdfViewerViewModel"
    }

    private val repository = PdfReaderRepository()

    private val _inProgress = MutableLiveData<DataState>()
    val inProgress: LiveData<DataState> = _inProgress


    // this bytes for pdf file
    private val _bytes = MutableLiveData<ByteArray?>()
    val bytes: LiveData<ByteArray?> = _bytes




    fun getPdfBytes(pdfUri: String) {
        _inProgress.value = DataState.LOADING
        viewModelScope.launch {
            repository.getPdf(pdfUri)
                .addOnSuccessListener { bytes ->
                    _inProgress.value = DataState.SUCCESS
                    _bytes.value = bytes
                }
                .addOnFailureListener {
                    _inProgress.value = DataState.ERROR
                }
        }
    }








}
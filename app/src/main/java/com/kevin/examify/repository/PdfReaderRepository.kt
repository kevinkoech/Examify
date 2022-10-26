package com.kevin.examify.repository

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.kevin.examify.data.FolderData
import com.kevin.examify.data.PdfData
import com.kevin.examify.remote.PdfReaderDataBase

class PdfReaderRepository {

    companion object{
        private const val TAG = "PdfReaderRepository"
    }

    private val remote = PdfReaderDataBase()

    val observeFolders = remote.folders


    suspend fun getFolders() = remote.getFolders()

    suspend fun addFolder(folderData: FolderData) =
         remote.addFolder(folderData)
            .addOnSuccessListener {
                Log.d(TAG, "onAddFolder: Folder has been uploaded to data base")
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "onAddFolder: Failed to upload due to ${e.message}")
            }


    suspend fun updateFolder(folder: FolderData) =
         remote.updateFolder(folder)
            .addOnSuccessListener {
                Log.d(TAG, "onUpdateFolder -> Folder has been updated from data base")
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "onUpdateFolder: Failed to update due to ${e.message}")
            }


    suspend fun deleteFolder(folderData: FolderData) =
         remote.addFolder(folderData)
            .addOnSuccessListener {
                Log.d(TAG, "onDeleteFolder -> Folder has been deleted from data base")
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "OnDeleteFolder: Failed to upload due to ${e.message}")
            }


    suspend fun getFolderById(id: String) = remote.getFolderById(id)

    suspend fun addPdf(pdfData: PdfData) = remote.addPdf(pdfData)

    suspend fun deletePdf(pdfData: PdfData) = remote.deletePdf(pdfData)

    suspend fun getPdf(pdfUri: String) = remote.getPdf(pdfUri)

    suspend fun uploadFile(uri: Uri, fileName: String, fileType: String) = remote.uploadFile(uri, fileName, fileType)



}
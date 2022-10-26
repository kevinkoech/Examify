package com.kevin.examify.remote

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kevin.examify.data.FolderData
import com.kevin.examify.data.PdfData
import com.kevin.examify.screens.pdf_viewer.PdfViewer
import com.kevin.examify.utils.Constants
import kotlinx.coroutines.tasks.await

class PdfReaderDataBase() {

    companion object{
        private const val TAG = "RemotePdfReaderDataBase"

        private const val FOLDERS_COLLECTION = "Folders"
        private const val PDFS_COUNT = "pdfsCount"
        private const val PDFS_FILED = "pdfs"
    }

    private val storage = Firebase.storage
    private val fireStore = FirebaseFirestore.getInstance()
    private val foldersPath = fireStore.collection(FOLDERS_COLLECTION)


    // you can get the live data from here once the class is init
    private val _observeFolders = MutableLiveData<List<FolderData>?>()
    val folders: LiveData<List<FolderData>?> = _observeFolders



    init {
        /** get the live data of folders data from firebase **/
        observeFolders()
    }


    private fun observeFolders() {
        foldersPath.addSnapshotListener { value, error ->
            if (error == null){
                if (value != null){
                    val folderData = value.toObjects(FolderData::class.java)
                    _observeFolders.value = folderData
                }
            }
        }
    }


    suspend fun getFolders():List<FolderData> = foldersPath.get().await().toObjects(FolderData::class.java)


    suspend fun addFolder(data: FolderData) = foldersPath.document(data.id).set(data)

    suspend fun updateFolder(data: FolderData) = foldersPath.document(data.id).update(data.toHashMap())

    suspend fun deleteFolder(data: FolderData) = foldersPath.document(data.id).delete()

    suspend fun getFolderById(id: String): FolderData? {
        val ref = foldersPath.document(id).get().await()
        return ref.toObject(FolderData::class.java)
    }


    suspend fun addPdf(data: PdfData) =
        foldersPath.document(data.folderId)
            .update(PDFS_FILED, FieldValue.arrayUnion(data)).addOnSuccessListener {
                // update pdf count
                foldersPath.document(data.folderId).get().addOnSuccessListener {
                    val pdfsCount = it.toObject(FolderData::class.java)?.pdfsCount!!
                    foldersPath.document(data.folderId).update(PDFS_COUNT,pdfsCount + 1)
                }
            }





    suspend fun deletePdf(pdfData: PdfData) {
        val ref = foldersPath.document(pdfData.folderId).get().await()
        if (ref != null){
            val folder = ref.toObject(FolderData::class.java)

            // delete pdf
            foldersPath.document(pdfData.folderId)
                .update(PDFS_FILED, FieldValue.arrayRemove(pdfData))

            // update total pdfs count in folder
            if (folder != null){
                folder.pdfsCount.plus(-1)
                updateFolder(folder)
            }

        }
    }


    // you can upload file or image (category could be image or file)
    suspend fun uploadFile(uri: Uri, fileName: String,fileType: String): Uri? {
        val filePath = storage.reference.child("$fileType/$fileName")
        val uploadTask = filePath.putFile(uri)
        val uriRef = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            filePath.downloadUrl
        }
        return uriRef.await()
    }



    suspend fun getPdf( pdfUrl : String) =
        storage.getReferenceFromUrl(pdfUrl).getBytes(Constants.MAX_BYTES_PDF)





//    suspend fun deleteFile(uri: Uri, fileName: String,category: String) {
//        val filePath = storage.reference.child("$category/$fileName")
//        val uploadTask = filePath.delete()
//        val uriRef = uploadTask.continueWithTask { task ->
//            if (!task.isSuccessful) {
//                task.exception?.let { throw it }
//            }
//            filePath.downloadUrl
//        }
//        return uriRef.await()
//    }




    /** we use wait to suspend the backend process until get the data **/
//    suspend fun loadFolders(): List<FolderData> {
//        val ref = foldersPath.get().await()
//        return ref.toObjects(FolderData::class.java)
//    }





}








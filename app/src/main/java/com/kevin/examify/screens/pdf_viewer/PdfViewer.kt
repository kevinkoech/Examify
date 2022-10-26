package com.kevin.examify.screens.pdf_viewer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.google.firebase.storage.FirebaseStorage
import com.kevin.examify.R
import com.kevin.examify.data.PdfData
import com.kevin.examify.databinding.PdfViewerBinding
import com.kevin.examify.utils.*
import kotlinx.coroutines.tasks.await
import java.io.FileOutputStream


class PdfViewer: Fragment() {

    companion object{
        private const val TAG = "PdfViewer"
    }

    private lateinit var viewModel: PdfViewerViewModel
    private lateinit var binding: PdfViewerBinding

    private lateinit var pdf: PdfData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding =   DataBindingUtil.inflate(inflater, R.layout.pdf_viewer, container, false)
        viewModel = ViewModelProvider(this)[PdfViewerViewModel::class.java]

        setViews()
        setObserves()

        // init data
        try { pdf = arguments?.get(Constants.PDF_KEY) as PdfData }
        catch (ex: Exception){}


        return binding.root

    }

    override fun onStart() {
        super.onStart()

        // load bytes of pdf
        viewModel.getPdfBytes(pdf.url)
    }


    private fun setObserves() {
        binding.apply {

            /** bytes live data **/
            viewModel.bytes.observe(viewLifecycleOwner) { bytes ->
                if (bytes != null){

                    pdfView.fromBytes(bytes)
                        .swipeHorizontal(false) // set false to scroll vertical , set true to scroll horizontal
                        .onPageChange { page, pageCount ->
                            //set current and total pages in toolbar subtitle
                            val currentPage = page + 1 //page starts from 0 so do +1 to start from 1
                            binding.pageNumber.text = "$currentPage/$pageCount" // e.g 3/232

                        }
                        .onError { t ->
                            Log.d(TAG, "loadPdfFromUrl : ${t.message}")
                            loaderLayout.root.hide()
                        }
                        .onPageError { _, t ->
                            loaderLayout.root.hide()
                            Log.d(TAG, "loadPdfFromUrl : ${t.message}")
                        }
                        .load()
                }
            }




            /** inProgress life data**/
            viewModel.inProgress.observe(viewLifecycleOwner) { inProgress ->
                if (inProgress != null){
                    when(inProgress){
                        DataState.LOADING->{
                            loaderLayout.root.show()
                        }
                        DataState.SUCCESS ->{
                            loaderLayout.root.hide()
                        }
                        DataState.ERROR->{
                            loaderLayout.root.show()
                        }
                    }
                }
            }



        }
    }

    private fun setViews() {

    }


    private val requestStoragePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted : Boolean ->

        //enable this.
      if (isGranted){ downloadPdf() }
     else{ showToast(requireContext(),"Permission Failed!") }

    }

    private fun downloadPdf() {
        TODO("Not yet implemented")
    }


    private fun saveToDownloadsFolder(bytes: ByteArray?) {
        // change the name file
        val nameWithExtension = "${System.currentTimeMillis()}.pdf"

        try {
            val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            downloadsFolder.mkdirs() // create folder if not exists
            val filePath = downloadsFolder.path +"/"+ nameWithExtension
            val out = FileOutputStream(filePath)
            out.write(bytes)
            out.close()
            // download file has been success
        }
        catch (e: Exception){
            Log.d(TAG , "saveToDownloadsFolder : Failed to Save due to ${e.message}")
            showToast(requireContext(),"Failed Download")
        }
    }




}
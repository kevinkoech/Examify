package com.kevin.examify.screens.add_pdf

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kevin.examify.R
import com.kevin.examify.data.PdfData
import com.kevin.examify.databinding.AddPdfBinding
import com.kevin.examify.utils.*
import com.kevin.examify.utils.generateId

class AddPdf : Fragment() {

    companion object {
        const val TAG = "AddPdf"
    }

    private lateinit var viewModel: AddPdfViewModel
    private lateinit var binding: AddPdfBinding

    //uri of picked pdf
    private var pdfUri: Uri? = null

    private var name = ""
    private var category = ""


    private var pdf: PdfData? = null
    private var folderId = ""
    private var isEdit: Boolean = false

    private var selectedFolderId = ""
    private var selectedFolderName = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding =  DataBindingUtil.inflate(inflater, R.layout.add_pdf, container, false)
        viewModel = ViewModelProvider(this)[AddPdfViewModel::class.java]

        folderId = arguments?.get(Constants.FOLDER_ID_KEY) as String
        isEdit = arguments?.get(Constants.IS_EDIT_KEY) as Boolean

        // init data
        try {
            pdf = arguments?.get(Constants.PDF_KEY) as PdfData
        }catch (ex: Exception){

        }




        setViews()
        setObserves()


        return binding.root
    }


    private fun setObserves() {
        binding.apply {

            /** inProgress life data**/
            viewModel.inProgress.observe(viewLifecycleOwner) { inProgress ->
                if (inProgress != null){
                    when(inProgress){
                        DataState.LOADING->{
                            loaderLayout.root.show()
                        }
                        DataState.SUCCESS ->{
                            loaderLayout.root.hide()
                            requireActivity().onBackPressed()
                            viewModel.resetState()
                            showToast(requireContext(),"Pdf Added")
                        }
                        DataState.ERROR->{
                            loaderLayout.root.show()
                            showToast(requireContext(),"Error happen!")
                        }
                    }
                }
            }

        }
    }


    private fun setViews() {
        binding.apply {

            /** button back **/
            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            /** button select category **/
            btnCategory.setOnClickListener {
                categoryPickIntent()
            }

            /** button init pdf data **/
            btnAddPdf.setOnClickListener {
                pdfPickIntent()
            }


            /** button submit pdf data **/
            submitBtn.setOnClickListener {
                subMit()
            }

            /** button remove pdf **/
            btnRemovePdf.setOnClickListener {
                pdfUri = null // to init pdf uri data
                pdfUploaded.hide()
                btnAddPdf.show()
            }


        }

    }



    private fun subMit() {
        binding.apply {
            //get data
            name = pdfName.text.toString().trim()
            category = btnCategory.text.toString().trim()

            //validate data
            if (name.isEmpty()){
                pdfName.error = "Pdf Name Required!"
                pdfName.requestFocus()
            }
            else if (category.isEmpty()){
                showToast(requireContext(),"Please Select Category!")
            }
            else if (pdfUri == null){
                showToast(requireContext(),"Pdf File Required!")
            }
            else{
                // check if pdf name is exist
                val folder = viewModel.folders.value?.find { it.id == folderId }!!
                val titles = folder.pdfs.map { it.title }
                if (name in titles){ // pdf name already exist!
                    pdfName.error = "Pdf name is already exit!"
                    pdfName.requestFocus()
                }else {
                    uploadPdf()
                }

            }
        }

    }


    private fun uploadPdf() {
        val pdfData = PdfData(id = generateId(), folderId, title = name)
        viewModel.uploadPdf(pdfData,pdfUri)
    }



    private fun categoryPickIntent() {
        val folder = viewModel.folders.value ?: emptyList()
        val titles = folder.map { it.title }.toTypedArray()

        // todo remove the duplicated titles.

        //get string array of novels from arraylist
        val novelsArray= arrayOfNulls<String>(folder.size)
        for (i in folder.indices) {
            novelsArray[i] = folder[i].title
        }

        //alert dialog
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Folder")
            .setItems(titles){ dialog ,which ->

                //handle item click
                //get clicked item
                selectedFolderName = folder[which].title
                selectedFolderId = folder[which].id

                // update data
                binding.btnCategory.text = selectedFolderName
                folderId = selectedFolderId


            }
            .show()
    }

    private fun pdfPickIntent() {
        val intent= Intent()
        intent.type="application/pdf"
        intent.action= Intent.ACTION_GET_CONTENT
        pdfActivityResultLauncher.launch(intent)
    }

    private val pdfActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            Log.d(TAG, "PDF Picked")
            pdfUri = result.data!!.data
            binding.btnAddPdf.hide()
            binding.pdfUploaded.show()
        } else {
           // cancel picked.
        }
    }


}
package com.kevin.examify.screens.folder

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.kevin.examify.R
import com.kevin.examify.data.PdfData
import com.kevin.examify.databinding.FolderBinding
import com.kevin.examify.utils.Constants
import com.kevin.examify.utils.hide
import com.kevin.examify.utils.show
import com.kevin.examify.utils.showToast

class Folder : Fragment() {

    private lateinit var viewModel: FolderViewModel
    private lateinit var binding: FolderBinding
    private val controller by lazy { PdfController() }

    private var folderId: String? = null
//    private val folderId = arguments?.get(Constants.FOLDER_ID_KEY) as Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding =   DataBindingUtil.inflate(inflater, R.layout.folder, container, false)
        viewModel = ViewModelProvider(this)[FolderViewModel::class.java]

        folderId = arguments?.get(Constants.FOLDER_ID_KEY) as String

        setViews()
        setObserves()



        return binding.root


    }

    override fun onStart() {
        super.onStart()

        // the folder data it will be updated every time when we open the screen
        if (folderId != null){
            viewModel.getFolder(folderId!!)
//            showToast(requireContext(), folderId!!)
        }

    }


    private fun setObserves() {

        /** folder live data **/
        viewModel.folder.observe(viewLifecycleOwner) { folder ->
            if (folder != null) {
                binding.folderName.text = folder.title
                if (folder.pdfs.isNotEmpty()){
                    binding.infoNoPdfFiles.hide()
                    controller.setData(folder.pdfs)
                }else{
                    binding.infoNoPdfFiles.show()
                }

            }
        }


    }



    private fun setViews() {
        binding.apply {


            /** button add pdf **/
            btnAddPdf.setOnClickListener {
//                showToast(requireContext(),"folderId: $folderId")
                val data = bundleOf(Constants.IS_EDIT_KEY to false, Constants.FOLDER_ID_KEY to folderId)
                findNavController().navigate(R.id.action_folder_to_addPdf,data)
            }

            /** button back **/
            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            setAdapter()

        }

    }


    private fun setAdapter() {
        controller.clickListener = object: PdfController.ClickListener {

            /** on pdf click **/
            override fun onClick(pdf: PdfData) {
                val data = bundleOf(Constants.PDF_KEY to pdf)
                findNavController().navigate(R.id.action_folder_to_pdfViewer , data)
            }

            // todo: edit pdf file this feature is not implemented yet.
        }
        binding.rvPdfs.adapter = controller.adapter
    }



}
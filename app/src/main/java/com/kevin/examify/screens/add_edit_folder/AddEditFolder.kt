package com.kevin.examify.screens.add_edit_folder

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kevin.examify.*
import com.kevin.examify.data.FolderData
import com.kevin.examify.databinding.AddEditFolderBinding
import com.kevin.examify.utils.*
import com.kevin.examify.utils.generateId


class AddEditFolder : Fragment() {

    companion object {
        const val TAG = "AddEditFolder"
    }

    private lateinit var viewModel: AddEditFolderViewModel
    private lateinit var binding: AddEditFolderBinding

    private var name = ""

    //uri of picked cover
    private var coverUri: Uri? = null

    private var isEdit = false
    private var folder: FolderData? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.add_edit_folder, container, false)
        viewModel = ViewModelProvider(this)[AddEditFolderViewModel::class.java]

        try {
            folder = arguments?.get(Constants.FOlDER_KEY) as FolderData
            isEdit = arguments?.get(Constants.IS_EDIT_KEY) as Boolean
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
                            showToast(requireContext(),"Added")
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



            // update state
            if (isEdit) {
                btnDelete.show()
                btnSubmit.text = "Update"
                folderName.setText(folder?.title)
            }

            /** button back **/
            btnBack.setOnClickListener{
                requireActivity().onBackPressed()
            }


            /** button attach cover **/
            btnSubmit.setOnClickListener {
                subMit()
            }

            /** button submit **/
            btnDelete.setOnClickListener{

            }

            /** button add Image **/
            btnAddImage.setOnClickListener {
                showCoverAttachMenu()
            }


        }

    }




    private fun subMit() {
        binding.apply {
            name = folderName.text.toString().trim()
            if (name.isEmpty()){
                folderName.error = "Folder Name Required!"
                folderName.requestFocus()
            }else{

                if (isEdit){
                    folder?.let { viewModel.updateFolder(it) }
                }else{
                    val folder = FolderData(generateId(), title = name)
                    val titles = viewModel.folders.value?.map { it.title }!!
                    if (name in titles){ // folder name is already exist!
                        folderName.error = "Folder name is already exist!"
                        folderName.requestFocus()
                    }else{
                        viewModel.addFolder(folder)
                    }
                }
            }
        }
    }


        private fun showCoverAttachMenu() {
            /* show popup menu with options Camera , Gallery to pick image */

            // setup popup menu
            val popupMenu = PopupMenu(requireContext(), binding.btnAddImage)
            popupMenu.menu.add(Menu.NONE, 0, 0, "Camera")
            popupMenu.menu.add(Menu.NONE, 1, 1, "Gallery")
            popupMenu.show()

            //handle popup menu item click

            popupMenu.setOnMenuItemClickListener { item ->
                // get id of clicked item
                val id = item.itemId
                if (id == 0) {
                    // camera clicked
                    pickCoverCamera()
                } else if (id == 1) {
                    // gallery clicked
                    pickCoverGallery()
                }
                true
            }
        }

        private fun pickCoverGallery() {
            // intent to pick image from gallery
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            galleryActivityResultLauncher.launch(intent)
        }

        private fun pickCoverCamera() {
            // intent to pick image from camera
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
            values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")
            coverUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, coverUri)
            cameraActivityResultLauncher.launch(intent)
        }


        // used to handle result of camera intent (new way in replacement of startActivityForResults)
        private val cameraActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            // get uri of image
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data

                // set to imageView
                binding.iconIv.setImageURI(coverUri)
            } else {
                //cancelled

            }
        }


    // used to handle result of gallery intent ( new way in replacement of startActivityForResults)
        private val galleryActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->

            // get uri of image
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                coverUri = data!!.data

                // set to imageView
                binding.iconIv.setImageURI(coverUri)
            } else {
                //cancelled

            }
        }


}
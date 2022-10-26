package com.kevin.examify.screens.home

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
import com.kevin.examify.data.FolderData
import com.kevin.examify.databinding.HomeBinding
import com.kevin.examify.utils.Constants
import com.kevin.examify.utils.showToast


class Home : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeBinding
    private val controller by lazy { FolderController() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding =  DataBindingUtil.inflate(inflater, R.layout.home, container, false)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]


        setViews()
        setObserves()



        return binding.root


    }


    override fun onStart() {
        super.onStart()

        // to refresh the recycler data
        viewModel.getFolders()


    }

    private fun setObserves() {

        /** folders live data **/
        viewModel.folders.observe(viewLifecycleOwner){ folders ->
            if (folders != null){
                controller.setData(folders)
            }
        }


    }

    private fun setViews() {
        setAdapter()
        binding.apply {

            /** button add folder **/
            btnAddFolder.setOnClickListener {
                val data = bundleOf(Constants.IS_EDIT_KEY to false)
                findNavController().navigate(R.id.action_home_to_addEditFolder,data)
            }



        }




    }


    private fun setAdapter() {
        controller.clickListener = object: FolderController.ClickListener {

            /** on folder click **/
            override fun onClick(folder: FolderData) {
                val folderId = bundleOf(Constants.IS_EDIT_KEY to false, Constants.FOLDER_ID_KEY to folder.id)
                findNavController().navigate(R.id.action_home_to_folder, folderId)
            }

        }
        binding.rvFolders.adapter = controller.adapter
    }






}
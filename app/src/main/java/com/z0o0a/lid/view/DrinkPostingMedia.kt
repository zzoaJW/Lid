package com.z0o0a.lid.view

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.z0o0a.lid.R
import com.z0o0a.lid.databinding.DrinkPostingMediaBinding
import com.z0o0a.lid.viewmodel.DrinkPostingVM


class DrinkPostingMedia : Fragment() {
    private lateinit var binding: DrinkPostingMediaBinding
    private val vm: DrinkPostingVM by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DrinkPostingMediaBinding.inflate(layoutInflater)
        binding.vm = vm
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // TODO (1) 권한 확인
        vm.getPermissionRequestResult().observe(viewLifecycleOwner) { result ->
            val cameraPermission = Manifest.permission.CAMERA
            val galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE

            if (result[cameraPermission] != true) {
                Toast.makeText(requireContext(), "카메라 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
            }
            if (result[galleryPermission] != true) {
                Toast.makeText(requireContext(), "갤러리 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        vm.requestCameraPermission()
        vm.requestGalleryPermission()


        binding.btnGetCamera.setOnClickListener {
            // TODO (2-1) 기본 카메라 촬영
            // TODO (2-2) 촬영 화질 높이기
            // TODO (2-3) 촬영한 사진 가져오기 [Repo -> (VM ->) View]
            // TODO (2-4) 촬영한 사진 가져오면 imageView 바꾸기 [View]
        }

        binding.btnGetImg.setOnClickListener {
            // TODO (3-1) 갤러리에서 이미지 선택 [Repo -> (VM ->) View]
            // TODO (3-2) 갤러리에서 이미지 가져오면 imageView 바꾸기 [View]
        }

        binding.btnBack2.setOnClickListener {
            findNavController().navigate(R.id.drinkPostingTitle)
        }

        binding.btnNext2.setOnClickListener {
            goPostingTextPerType()
        }
    }


    private fun goPostingTextPerType(){
//        var drinkType = vm.drink.value!!.drinkType
//
//        if (drinkType == "위스키"){
//            findNavController().navigate(R.id.drink_posting_detail_whiskey)
//        }else if (drinkType == "와인"){
//            findNavController().navigate(R.id.drink_posting_detail_wine)
//        }else if (drinkType == "맥주"){
//            findNavController().navigate(R.id.drink_posting_detail_beer)
//        }else{
            findNavController().navigate(R.id.drinkPostingDetail)
//        }
    }

}
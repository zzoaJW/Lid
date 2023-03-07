package com.z0o0a.lid

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.z0o0a.lid.databinding.DrinkPostingImgBinding
import com.z0o0a.lid.view.DrinkPostingText
import com.z0o0a.lid.viewmodel.DrinkPostingVM


class DrinkPostingImg : AppCompatActivity() {
    private lateinit var vm: DrinkPostingVM
    private lateinit var binding: DrinkPostingImgBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.drink_posting_img)
        vm = ViewModelProvider(this)[DrinkPostingVM::class.java]
        binding.vm = vm
        binding.lifecycleOwner = this

        // TODO (1) 권한 확인
        vm.getPermissionRequestResult().observe(this) { result ->
            val cameraPermission = Manifest.permission.CAMERA
            val galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE

            if (result[cameraPermission] != true) {
                Toast.makeText(applicationContext, "카메라 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
            }
            if (result[galleryPermission] != true) {
                Toast.makeText(applicationContext, "갤러리 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
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

        binding.btnNext2.setOnClickListener {
            goPostingTextPerType()
        }

        binding.btnBack2.setOnClickListener {
            finish()
        }
    }

    private fun goPostingTextPerType(){
        var drinkType = vm.drink.value!!.drinkType

        if (drinkType == "위스키"){
            intent = Intent(this, DrinkPostingTextWhiskey::class.java)
            startActivity(intent)
        }else if (drinkType == "와인"){
            intent = Intent(this, DrinkPostingTextWine::class.java)
            startActivity(intent)
        }else if (drinkType == "맥주"){
            intent = Intent(this, DrinkPostingTextBeer::class.java)
            startActivity(intent)
        }else{
            intent = Intent(this, DrinkPostingText::class.java)
            startActivity(intent)
        }
    }

}
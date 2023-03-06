package com.z0o0a.lid

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkPostingImgBinding
import com.z0o0a.lid.model.PostingDrinkSingleton
import com.z0o0a.lid.view.DrinkPostingText
import java.io.IOException


class DrinkPostingImg : AppCompatActivity() {
    private lateinit var binding: DrinkPostingImgBinding

    private var drinkImgUri : Uri? = null
    private var drinkImgBitmap : Bitmap? = null

    private val cameraPermissionList = arrayOf(Manifest.permission.CAMERA)

    private val permissionList = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        result.forEach {
            if(!it.value) {
                Toast.makeText(applicationContext, "권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkPostingImgBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkPermission.launch(cameraPermissionList)
        checkPermission.launch(permissionList)

        binding.btnGetImg.setOnClickListener {
            openGalleryForImage()
            onActivityResult(1000,1000, intent)
        }

        binding.btnGetCamera.setOnClickListener {
            val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            cameraActivityResult.launch(intent)
        }

        binding.btnNext2.setOnClickListener {
            if (drinkImgBitmap == null){ // 갤러리에서 사진 안가져온 경우
                drinkImgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bottle)

            }
            setPostingSingleton(drinkImgBitmap)

            // 다음 화면 ㄱㄱ
            setIntentFromType()
        }

        binding.btnBack2.setOnClickListener {
            finish()
        }
    }

    // 싱글톤에 img 넣기
    private fun setPostingSingleton(img:Bitmap?){
        val postingSingleton = PostingDrinkSingleton.getInstance(applicationContext)

        postingSingleton!!.drinkImg = img
    }

    // 갤러리 열기
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    // 갤러리 열고 받아온 이미지 처리
    // 사진 띄우기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000){
            drinkImgUri = data!!.data!!

            try {
                drinkImgBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            contentResolver,
                            drinkImgUri!!
                        )
                    )
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, drinkImgUri)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            binding.galleyPic.setImageBitmap(drinkImgBitmap) // handle chosen image
            binding.textView5.visibility = View.INVISIBLE
        }
    }

    private val cameraActivityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        if(it.resultCode == RESULT_OK && it.data != null){
            //값 담기
            val extras = it.data!!.extras

            //bitmap으로 타입 변경
            drinkImgBitmap = extras?.get("data") as Bitmap

            //화면에 보여주기
            binding.galleyPic.setImageBitmap(drinkImgBitmap) // handle chosen image
            binding.textView5.visibility = View.INVISIBLE
        }
    }






    private fun setIntentFromType(){
        val postingSingleton = PostingDrinkSingleton.getInstance(applicationContext)

        var whichType = postingSingleton!!.drinkType

        if (whichType == "위스키"){
            intent = Intent(this, DrinkPostingTextWhiskey::class.java)
            startActivity(intent)
        }else if (whichType == "와인"){
            intent = Intent(this, DrinkPostingTextWine::class.java)
            startActivity(intent)
        }else if (whichType == "맥주"){
            intent = Intent(this, DrinkPostingTextBeer::class.java)
            startActivity(intent)
        }else{
            intent = Intent(this, DrinkPostingText::class.java)
            startActivity(intent)
        }
    }

}
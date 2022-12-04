package com.z0o0a.lid

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkPostingImgBinding

class DrinkPostingImg : AppCompatActivity() {
    private lateinit var binding: DrinkPostingImgBinding

    var drinkImg = ""

    private val permissionList = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        result.forEach {
            if(!it.value) {
                Toast.makeText(applicationContext, "권한 동의 필요!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkPostingImgBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.textView5.visibility = View.VISIBLE

        checkPermission.launch(permissionList)
        binding.btnGetImg.setOnClickListener {
            openGalleryForImage()
            onActivityResult(1000,1000,intent)
        }

        binding.btnNext2.setOnClickListener {
            if(drinkImg == ""){
                // "android.resource://com.z0o0a.lid/drawable/bottle"로 저장해도되긴하는데... 나중에 에러 발생할까봐 빌더로 얌전히 할께요 ㅠ
                var defaultUri = Uri.Builder()
                    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                    .authority(resources.getResourcePackageName(R.drawable.bottle))
                    .appendPath(resources.getResourceTypeName(R.drawable.bottle))
                    .appendPath(resources.getResourceEntryName(R.drawable.bottle))
                    .build()

                setPostingSingleton(defaultUri.toString())
            }else{
                setPostingSingleton(drinkImg)
            }

            setIntentFromType()
        }

        binding.btnBack2.setOnClickListener {
            finish()
        }
    }

    private fun setPostingSingleton(img:String){
        val postingSingleton = PostingDrinkSingleton.getInstance(applicationContext)

        postingSingleton!!.drinkImg = img
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000){
            drinkImg = data?.data.toString()
            binding.galleyPic.setImageURI(data?.data) // handle chosen image
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
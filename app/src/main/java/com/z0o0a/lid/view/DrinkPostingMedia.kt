package com.z0o0a.lid.view

import android.Manifest
import android.R.attr
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.z0o0a.lid.R
import com.z0o0a.lid.databinding.DrinkPostingMediaBinding
import com.z0o0a.lid.viewmodel.DrinkPostingVM
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat


class DrinkPostingMedia : Fragment() {
    private lateinit var binding: DrinkPostingMediaBinding
    private val vm: DrinkPostingVM by activityViewModels()

    var mCurrentPhotoPath = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DrinkPostingMediaBinding.inflate(layoutInflater)
        binding.vm = vm
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.getPermissionRequestResult().observe(viewLifecycleOwner) { result ->
            val cameraPermission = Manifest.permission.CAMERA
            val galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE

//            if (result[cameraPermission] != true) {
//                Toast.makeText(requireContext(), "카메라 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
//            }
            if (result[galleryPermission] != true) {
                Toast.makeText(requireContext(), "갤러리 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        vm.requestCameraPermission()
        vm.requestGalleryPermission()


        binding.btnGetCamera.setOnClickListener {
            getCameraImg()
        }

        binding.btnGetImg.setOnClickListener {
            // TODO (3-1) 갤러리에서 이미지 선택 [Repo -> (VM ->) View]
            // TODO (3-2) 갤러리에서 이미지 가져오면 imageView 바꾸기 [View]
            getGalleyImg()
        }

        binding.btnBack2.setOnClickListener {
            findNavController().navigate(R.id.drinkPostingTitle)
        }

        binding.btnNext2.setOnClickListener {
            goPostingTextPerType()
        }
    }


    private fun goPostingTextPerType(){
        var drinkType = vm.drink.value!!.drinkType

        if (drinkType == "위스키"){
            findNavController().navigate(R.id.drinkPostingDetailWhiskey)
        }else if (drinkType == "와인"){
            findNavController().navigate(R.id.drinkPostingDetailWine)
        }else if (drinkType == "맥주"){
            findNavController().navigate(R.id.drinkPostingDetailBeer)
        }else{
            findNavController().navigate(R.id.drinkPostingDetail)
        }
    }

    private fun getCameraImg() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(requireActivity().packageManager) != null){
            var photoFile = requireActivity().cacheDir

            val now = System.currentTimeMillis()
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(now)
            val imgFileName = "Camera_" + timeStamp

            try{
                val tempImg = File.createTempFile(imgFileName, ".jpg", photoFile)
                mCurrentPhotoPath = tempImg.absolutePath
                photoFile = tempImg

            }catch (e : IOException){
                Toast.makeText(requireContext(), "입출력 예외", Toast.LENGTH_SHORT).show()
            }

            if(photoFile != null){
                val aut = requireActivity().packageName + ".fileprovider"
                val photoURI = FileProvider.getUriForFile(requireContext(), aut, photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, 200)
            }
        }
    }

    private fun getGalleyImg(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 300)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 200){
            try {
                val file = File(mCurrentPhotoPath)
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, Uri.fromFile(file))

                if(bitmap != null){
                    val ei = ExifInterface(mCurrentPhotoPath)
                    val orientation: Int = ei.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED
                    )

                    var rotatedBitmap: Bitmap? = null
                    rotatedBitmap = when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                        else -> bitmap
                    }

                    binding.galleyPic.setImageBitmap(rotatedBitmap)
                    vm.drink.value!!.drinkImg = rotatedBitmap
                }
            } catch (e: Exception) {
                val expt = "[get cameraPhoto Error] Exception : ${e.message}, Cause : ${e.cause}"
                Firebase.crashlytics.log(expt)
            }

        }
        else if(requestCode == 300 && data != null){
            val selectedImg = data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImg)

            if(selectedImg != null){
                binding.galleyPic.setImageBitmap(bitmap)
                vm.drink.value!!.drinkImg = bitmap
            }
        }
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}
package com.z0o0a.lid.viewmodel

import android.Manifest
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.z0o0a.lid.Drink
import com.z0o0a.lid.DrinkBeer
import com.z0o0a.lid.DrinkWhiskey
import com.z0o0a.lid.DrinkWine
import com.z0o0a.lid.repository.DrinkPostingRepo
import com.z0o0a.lid.repository.ReqPermissionCameraGallery

class DrinkPostingVM(application: Application) : AndroidViewModel(application) {
    val drink = MutableLiveData<Drink>()

    val drinkWhiskey = MutableLiveData<DrinkWhiskey>()
    val drinkWine = MutableLiveData<DrinkWine>()
    val drinkBeer = MutableLiveData<DrinkBeer>()

    private val postingRepo = DrinkPostingRepo(application)

    private val reqPermissionCameraGallery = ReqPermissionCameraGallery(application)
    private val permissionRequestResult = MutableLiveData<Map<String, Boolean>>()


    init {
        drink.value = Drink(0,null, "", "", "", 0L, "",
                             0f, "", "", "개봉일 선택", "", "")
        drinkWhiskey.value = DrinkWhiskey(0, false, "-", "#000000", arrayListOf(), arrayListOf(),
                                   3, 3, 3, arrayListOf())
        drinkWine.value = DrinkWine(0, false, "-", 3, 3, 3, false,
                                     3, "", "", arrayListOf(), 3, 3, 3, 3)
        drinkBeer.value = DrinkBeer(0, false, "-", "", 3, "", 3, 3,
                                      3, 3, 3, "", 3, 3, 3,
                                      3, 3, 3, 3)
    }

    fun requestCameraPermission() {
        reqPermissionCameraGallery.reqCameraPermission { granted ->
            val cameraPermission = Manifest.permission.CAMERA
            permissionRequestResult.value = mapOf(cameraPermission to granted)
        }
    }

    fun requestGalleryPermission() {
        reqPermissionCameraGallery.reqGalleryPermission { granted ->
            val galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE
            permissionRequestResult.value = mapOf(galleryPermission to granted)
        }
    }

    fun getPermissionRequestResult(): MutableLiveData<Map<String, Boolean>> {
        return permissionRequestResult
    }

    fun setDrinkType(drinkType : Int){
        // 위스키 0 / 와인 1 / 맥주 2 / 기타 3
        when (drinkType) {
            0 -> drink.value!!.drinkType = "위스키"
            1 -> drink.value!!.drinkType = "와인"
            2 -> drink.value!!.drinkType = "맥주"
            3 -> drink.value!!.drinkType = ""
        }
    }

    fun insertDrink(){
        postingRepo.insert(drink.value!!)
    }

    fun insertDrinkWhiskey(){
        drink.value!!.typeId = postingRepo.insertDrinkWhiskey(drinkWhiskey.value!!)
    }

    fun insertDrinkWine(){
        drink.value!!.typeId = postingRepo.insertDrinkWine(drinkWine.value!!)
    }

    fun insertDrinkBeer(){
        drink.value!!.typeId = postingRepo.insertDrinkBeer(drinkBeer.value!!)
    }
}
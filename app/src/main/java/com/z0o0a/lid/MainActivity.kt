package com.z0o0a.lid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.z0o0a.lid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initNavigationBar() //네이게이션 바의 각 메뉴 탭을 눌렀을 때 화면이 전환되도록 하는 함수.

    }

    fun initNavigationBar() {
        binding.bottomNavigationView.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.item_calendar -> {
                        changeFragment(MainFragmentCalendar())
                    }
                    R.id.item_drinks -> {
                        changeFragment(MainFragmentDrinks())
                    }
                    R.id.item_user -> {
                        changeFragment(MainFragmentUser())
                    }
                }
                true
            }
            selectedItemId = R.id.item_drinks
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.mainFragmentsContainer.id, fragment).commit()
    }

    override fun onBackPressed() {
        // 메인 화면에서는 뒤로가기 불가
        // super.onBackPressed()
    }

}
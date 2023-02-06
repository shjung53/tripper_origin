package com.tripper.tripper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tripper.tripper.databinding.ActivityMainBinding
import com.tripper.tripper.ui.main.map.MapActivity
import com.tripper.tripper.ui.main.myInfo.searchMyInfo.MyInfoFragment
import com.tripper.tripper.ui.main.home.HomeFragment
import com.tripper.tripper.ui.main.schedule.CalendarActivity
import androidx.fragment.app.Fragment
import com.tripper.tripper.ui.main.home.BlockFragment


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.mainBnv.setOnItemSelectedListener{
            when (it.itemId){
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.mapActivity -> {
//                    val intent = Intent(this, MapActivity::class.java)
//                    startActivity(intent)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, BlockFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.calendarActivity -> {
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                }

                R.id.myInfoFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MyInfoFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }
        binding.mainBnv.selectedItemId = R.id.homeFragment
    }

    fun fragmentToFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

}
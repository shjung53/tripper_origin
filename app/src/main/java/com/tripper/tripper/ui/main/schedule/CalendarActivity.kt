package com.tripper.tripper.ui.main.schedule
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.tripper.tripper.MainActivity
import com.tripper.tripper.dataClass.DatesData
import com.tripper.tripper.databinding.ActivityCalendarBinding
import gun0912.tedimagepicker.util.ToastUtil.context
import java.text.SimpleDateFormat
import java.util.*


class CalendarActivity : AppCompatActivity() {

    lateinit var binding: ActivityCalendarBinding

    private var transIndex = 0

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd",Locale.KOREAN)

    private val dates = ArrayList<DatesData>()

    private val yearRange = IntRange(0,3)

    private val monthRange = IntRange(5,6)

    private val dayRange = IntRange(8,9)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendar : DateRangeCalendarView = binding.calendarCalendarCv

        binding.calendarBackIv.setOnClickListener {
            finish()
        }

//        날짜 선택 디폴트값
        val preSelected1 = java.util.Calendar.getInstance()
        preSelected1.add(Calendar.MONTH, 0)
        val preSelected2 = java.util.Calendar.getInstance()
        preSelected2.add(Calendar.DATE, 1)
        calendar.setSelectedDateRange(preSelected1,preSelected2)


        //        교통수단 인덱스
        setTransIndex()
        changeTransVisibility(transIndex)


//        다음 버튼 클릭
        binding.calendarNextTv.setOnClickListener {
            val startDay = calendar.startDate!!.timeInMillis
            val endDay = if(calendar.endDate == null){startDay}else{calendar.endDate?.timeInMillis}
            val gap = endDay!! - startDay
            val days = gap / (24*60*60*1000) + 1
            val spf = context.getSharedPreferences("days", MODE_PRIVATE)
            val editor = spf.edit()
            editor.putLong("days",days)
            editor.apply()
            val startDate = dateFormat.format(calendar.startDate!!.time)
            val endDate = if(calendar.endDate == null){dateFormat.format(calendar.startDate!!.time)}else{dateFormat.format(calendar.endDate!!.time)}
            getDates(startDate, days.toInt())
            val intent = Intent(this, ScheduleActivity::class.java).apply {
                putParcelableArrayListExtra("dates",dates)
                putExtra("startDate",startDate)
                putExtra("endDate",endDate)
                putExtra("transIndex",transIndex)
            }
            startActivity(intent)
            finish()
        }
    }






    private fun getDates(startDate: String, days: Int){
        val year = startDate.slice(yearRange).toInt()
        val month = startDate.slice(monthRange).toInt()-1
        val day = startDate.slice(dayRange).toInt()
        val calendar = java.util.Calendar.getInstance()
        calendar.set(year,month,day)
        for(i in 1..days){
            calendar.add(java.util.Calendar.DATE,i-1)
            val date = dateFormat.format(calendar.time)
            dates.apply { add(DatesData(date))}
            calendar.set(year,month,day)
        }
    }

    private fun setTransIndex() {
        binding.calendarCarOffIv.setOnClickListener {
            transIndex = 0
            changeTransVisibility(transIndex)
        }
        binding.calendarTrainOffIv.setOnClickListener {
            transIndex = 1
            changeTransVisibility(transIndex)
        }
        binding.calendarBicycleOffIv.setOnClickListener {
            transIndex = 2
            changeTransVisibility(transIndex)
        }
        binding.calendarWalkOffIv.setOnClickListener {
            transIndex = 3
            changeTransVisibility(transIndex)
        }
    }


    //    교통수단 클릭 리스너
    private fun changeTransVisibility(index: Int) {
        when (index) {
            0 -> {
                binding.calendarCarOffIv.visibility = View.GONE
                binding.calendarCarOnIv.visibility = View.VISIBLE
                binding.calendarTrainOffIv.visibility = View.VISIBLE
                binding.calendarTrainOnIv.visibility = View.GONE
                binding.calendarBicycleOffIv.visibility = View.VISIBLE
                binding.calendarBicycleOnIv.visibility = View.GONE
                binding.calendarWalkOffIv.visibility = View.VISIBLE
                binding.calendarWalkOnIv.visibility = View.GONE
            }
            1 -> {
                binding.calendarCarOffIv.visibility = View.VISIBLE
                binding.calendarCarOnIv.visibility = View.GONE
                binding.calendarTrainOffIv.visibility = View.GONE
                binding.calendarTrainOnIv.visibility = View.VISIBLE
                binding.calendarBicycleOffIv.visibility = View.VISIBLE
                binding.calendarBicycleOnIv.visibility = View.GONE
                binding.calendarWalkOffIv.visibility = View.VISIBLE
                binding.calendarWalkOnIv.visibility = View.GONE
            }
            2 -> {
                binding.calendarCarOffIv.visibility = View.VISIBLE
                binding.calendarCarOnIv.visibility = View.GONE
                binding.calendarTrainOffIv.visibility = View.VISIBLE
                binding.calendarTrainOnIv.visibility = View.GONE
                binding.calendarBicycleOffIv.visibility = View.GONE
                binding.calendarBicycleOnIv.visibility = View.VISIBLE
                binding.calendarWalkOffIv.visibility = View.VISIBLE
                binding.calendarWalkOnIv.visibility = View.GONE
            }
            3 -> {
                binding.calendarCarOffIv.visibility = View.VISIBLE
                binding.calendarCarOnIv.visibility = View.GONE
                binding.calendarTrainOffIv.visibility = View.VISIBLE
                binding.calendarTrainOnIv.visibility = View.GONE
                binding.calendarBicycleOffIv.visibility = View.VISIBLE
                binding.calendarBicycleOnIv.visibility = View.GONE
                binding.calendarWalkOffIv.visibility = View.GONE
                binding.calendarWalkOnIv.visibility = View.VISIBLE
            }
        }
    }
}
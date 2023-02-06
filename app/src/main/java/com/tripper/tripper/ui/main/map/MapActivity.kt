package com.tripper.tripper.ui.main.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tripper.tripper.dataClass.MapFeedData
import com.tripper.tripper.R
import com.tripper.tripper.databinding.ActivityMapBinding
import net.daum.mf.map.api.*

class MapActivity: AppCompatActivity(){

    lateinit var binding: ActivityMapBinding
    private var datas = ArrayList<MapFeedData>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val mapView = MapView(this)
        val mapViewContainer = binding.mapMv
        mapViewContainer.addView(mapView)

        val nowPositionMarker = MapPOIItem()

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.mapFeedRv)

        binding.mapFeedRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var currentPosition = RecyclerView.NO_POSITION

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.layoutManager != null) {
                    val view = snapHelper.findSnapView(recyclerView.layoutManager)!!
                    val position = recyclerView.layoutManager!!.getPosition(view)

                    if (currentPosition != position) {
                        currentPosition = position
                        if (currentPosition == 0) {
                            // 모든 마커, polyline 삭제
                            mapView.removeAllPOIItems()
                            mapView.removeAllPolylines()
                            // 위치
                            val mapPoint1 = MapPoint.mapPointWithGeoCoord(37.505040340672004, 127.06208845369152)
                            val mapPoint2 = MapPoint.mapPointWithGeoCoord(37.5033806415067, 127.05884029883829)
                            val mapPoint3 = MapPoint.mapPointWithGeoCoord(37.50325722650525, 127.05990245352191)

                            //마커 생성
                            val marker1 = makeMarker("휘문고", mapPoint1, "학교")
                            val marker2 = makeMarker("스타벅스", mapPoint2, "카페")
                            val marker3 = makeMarker("아띠피자", mapPoint3, "음식점")

                            mapView.addPOIItem(marker1)
                            mapView.addPOIItem(marker2)
                            mapView.addPOIItem(marker3)

                            val polyline = MapPolyline()
                            polyline.lineColor = R.color.main

                            polyline.addPoint(mapPoint1)
                            polyline.addPoint(mapPoint2)
                            polyline.addPoint(mapPoint3)

                            mapView.addPolyline(polyline)

                            // Polyline이 모두 나오도록
                            val mapPointBounds = MapPointBounds(polyline.mapPoints)
                            mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, 200))
                        }
                        if (currentPosition == 1) {
                            // 모든 마커, polyline 삭제
                            mapView.removeAllPOIItems()
                            mapView.removeAllPolylines()
                            // 위치
                            val mapPoint1 = MapPoint.mapPointWithGeoCoord(37.55960706142799, 126.79451681455477)
                            val mapPoint2 = MapPoint.mapPointWithGeoCoord(33.510583445996225, 126.49131048215031)

                            //마커 생성
                            val marker1 = makeMarker("김포", mapPoint1, "음식점")
                            val marker2 = makeMarker("제주", mapPoint2, "숙박")

                            mapView.addPOIItem(marker1)
                            mapView.addPOIItem(marker2)

                            val polyline = MapPolyline()
                            polyline.lineColor = R.color.main

                            polyline.addPoint(mapPoint1)
                            polyline.addPoint(mapPoint2)

                            mapView.addPolyline(polyline)

                            // Polyline이 모두 나오도록
                            val mapPointBounds = MapPointBounds(polyline.mapPoints)
                            mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, 200))
                        }
                        if (currentPosition == 2) {
                            // 모든 마커, polyline 삭제
                            mapView.removeAllPOIItems()
                            mapView.removeAllPolylines()
                            // 위치
                            val mapPoint1 = MapPoint.mapPointWithGeoCoord(34.8951888754362, 128.6291523686934)
                            val mapPoint2 = MapPoint.mapPointWithGeoCoord(34.89739348024461, 128.62852596608238)
                            val mapPoint3 = MapPoint.mapPointWithGeoCoord(34.89549275456963, 128.6310901576705)
                            val mapPoint4 = MapPoint.mapPointWithGeoCoord(34.89759414389053, 128.63205688311433)

                            //마커 생성
                            val marker1 = makeMarker("초등학교", mapPoint1, "학교")
                            val marker2 = makeMarker("카페", mapPoint2, "카페")
                            val marker3 = makeMarker("공원", mapPoint3, "관광명소")
                            val marker4 = makeMarker("아파트", mapPoint4, "숙박")

                            mapView.addPOIItem(marker1)
                            mapView.addPOIItem(marker2)
                            mapView.addPOIItem(marker3)
                            mapView.addPOIItem(marker4)

                            val polyline = MapPolyline()
                            polyline.lineColor = R.color.main

                            polyline.addPoint(mapPoint1)
                            polyline.addPoint(mapPoint2)
                            polyline.addPoint(mapPoint3)
                            polyline.addPoint(mapPoint4)

                            mapView.addPolyline(polyline)

                            // Polyline이 모두 나오도록
                            val mapPointBounds = MapPointBounds(polyline.mapPoints)
                            mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, 200))
                        }
                    }
                }
            }
        })

        binding.mapBackIv.setOnClickListener {
            onBackPressed()
        }

        datas.apply {
            add(
                MapFeedData(R.drawable.picture6, R.drawable.ic_empty_profile, "사자", "제목1", "# 테스트1", R.drawable.ic_verybad_29dp)
            )
            add(
                MapFeedData(R.drawable.picture5, R.drawable.ic_empty_profile, "하제", "제목2", "# 테스트2", R.drawable.ic_bad_29dp, 1)
            )
            add(
                MapFeedData(R.drawable.picture4, R.drawable.ic_empty_profile, "제이슨", "제목3", "# 테스트3", R.drawable.ic_nomal_29dp, 2)
            )
            add(
                MapFeedData(R.drawable.picture3, R.drawable.ic_empty_profile, "에릭", "제목4", "# 테스트4", R.drawable.ic_good_29dp, 55)
            )
            add(
                MapFeedData(R.drawable.picture2, R.drawable.ic_empty_profile, "메이", "제목5", "# 테스트5", R.drawable.ic_verygood_29dp, 6)
            )
        }

        val mapFeedRVAdapter = MapFeedRVAdapter(datas)

        binding.mapFeedRv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.mapFeedRv.adapter = mapFeedRVAdapter

        mapFeedRVAdapter.notifyDataSetChanged()

        binding.mapMyLocationIv.setOnClickListener {
            val isNetworkEnabled: Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if(Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
            } else {
                when { // 프로바이드 제공자 활성화 여부 체크
                    isNetworkEnabled -> {
                        val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) // 인터넷기반으로 위치를 찾음
                        val longitude = location?.longitude!! // 경도
                        val latitude = location.latitude // 위도
                        Toast.makeText(this, "현재위치를 불러옵니다.", Toast.LENGTH_SHORT).show()
                        val uNowPosition = MapPoint.mapPointWithGeoCoord(latitude, longitude)
                        mapView.setMapCenterPoint(uNowPosition, true) // 현재 위치를 지도의 중심점으로 설정
                        mapView.setZoomLevel(1, true) // 확대 레벨 설정 (값이 적을 수록 더 확대됨)

                        // 기존에 있던 현재 위치 마커 삭제
                        mapView.removePOIItem(nowPositionMarker)

                        // 현재 위치 마커 생성
                        nowPositionMarker.itemName = "현재 위치"
                        nowPositionMarker.mapPoint = uNowPosition
                        nowPositionMarker.markerType = MapPOIItem.MarkerType.BluePin
                        nowPositionMarker.selectedMarkerType = MapPOIItem.MarkerType.RedPin

                        mapView.addPOIItem(nowPositionMarker)
                    }
                    else -> {

                    }
                }
            }
        }
        lm.removeUpdates(gpsLocationListener)
    }

    val gpsLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val provider: String = location.provider
            val longitude: Double = location.longitude
            val latitude: Double = location.latitude
            val altitude: Double = location.altitude
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun makeMarker(itemName: String, mapPoint: MapPoint, image: String) : MapPOIItem {
        val marker = MapPOIItem()
        marker.itemName = itemName
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.CustomImage
        // "관광명소", "음식점" + "카페", "숙박"
        if (image == "음식점" || image == "카페") {
            marker.customImageResourceId = R.drawable.ic_food_marker_5x
        }
        else if (image == "관광명소") {
            marker.customImageResourceId = R.drawable.ic_landscape_marker_5x
        }
        else if (image == "숙박") {
            marker.customImageResourceId = R.drawable.ic_bed_marker_5x
        }
        else {
            marker.customImageResourceId = R.drawable.ic_etc_marker_5x
        }
        marker.setCustomImageAnchor(0.5f, 0.73f)
        marker.isCustomImageAutoscale = true

        return marker
    }
}

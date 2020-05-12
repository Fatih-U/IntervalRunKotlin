package com.fatihucarci.routedrawingsample

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.fatihucarci.routedrawingsample.room.RunActivity
import com.fatihucarci.routedrawingsample.room.RunDatabase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_run_detail.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

class RunDetailActivity : BaseActivity(), OnMapReadyCallback {


    private lateinit var googleMap: GoogleMap

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) //display back button
        supportActionBar?.setDisplayShowHomeEnabled(true) //display back button

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val runId = intent.getIntExtra("RunID", 0)
        var runItem: RunActivity? = null

        launch {
            this@RunDetailActivity.let {
                runItem = RunDatabase(this@RunDetailActivity).getRunActivityDAO().getRun(runId)

                setupMap(runItem)

            }

        }


    }

    private fun setupMap(runItem: RunActivity?) {

        //Toast.makeText(this@RunDetailActivity, "${runItem?.startTimeMilli}", Toast.LENGTH_SHORT).show()


        val runningDistance = runItem?.pathPoints?.let {
            calculateDistance(it)/1000
        }

        val runningDuration = runItem?.let {
            calculateDuration(it)
        }

        val runningDate = runItem?.startTimeMilli?.let {
            convertLongToSimpleString(it)
        }

        tv_rundetail_distance.text = "%.2f".format(runningDistance) + " km"
        tv_rundetail_date.text = runningDate
        tv_rundetail_duration.text = runningDuration

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                runItem?.pathPoints?.first(),
                MAP_ZOOM
            )
        )
        googleMap.addPolyline(
            PolylineOptions()
                .color(Color.GREEN)
                .width(POLYLINE_WIDTH)
                .addAll(runItem?.pathPoints)
        )

        googleMap.addMarker(
            MarkerOptions()
                .position(runItem?.pathPoints?.first()!!)
                .icon(bitmapDescriptorFromVector(this,R.drawable.ic_startmarker))
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

            //.title("Başlangıç")
        )

        googleMap.addMarker(
            MarkerOptions()
                .position(runItem.pathPoints.last())
                .icon(bitmapDescriptorFromVector(this,R.drawable.ic_endmarker))
            //.title("Bitiş")
        )

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            this.googleMap = it
        }
        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL

    }

}

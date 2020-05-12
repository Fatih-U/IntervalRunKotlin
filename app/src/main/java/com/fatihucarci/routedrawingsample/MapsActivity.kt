package com.fatihucarci.routedrawingsample

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.widget.Chronometer
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fatihucarci.routedrawingsample.room.RunActivity
import com.fatihucarci.routedrawingsample.room.RunDatabase
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.shashank.sony.fancydialoglib.Animation
import com.shashank.sony.fancydialoglib.FancyAlertDialog
import com.shashank.sony.fancydialoglib.Icon
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


const val REQUEST_CODE_LOCATION_PERMISSION = 0
const val POLYLINE_WIDTH = 14f
const val MAP_ZOOM = 16f
const val TAG = "@ROUTEDRAWING"

class MapsActivity : BaseActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var locationPermissionGranted = false
    private var lastLocation: Location? = null
    private var isTracking = false
    private var isChronoRunning = false

    private lateinit var chronometer: Chronometer

    //Polyline points list:
    private val pathPoints = mutableListOf<LatLng>()

    //Running start & end time in milliseconds
    private var startTimeMillis: Long = 0
    private var endTimeInMillis: Long = 0


    @InternalCoroutinesApi
    @SuppressLint("MissingPermission", "ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        supportActionBar?.hide()

        if (!isLocationEnabled(this)) {
            createLocationRequest()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        chronometer = findViewById(R.id.chronometer)
        requestPermission()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        btnStartStop.onSwipedOnListener = {
            if (!isChronoRunning) {
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.start()
                isChronoRunning = true
            }

            if (locationPermissionGranted && !isTracking) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
                isTracking = true
            }

            //start time is current system time
            startTimeMillis = System.currentTimeMillis()
        }

        btnStartStop.onSwipedOffListener = {

            //assigning end time to running activity on stop clicked
            endTimeInMillis = System.currentTimeMillis()

            //calculating total distance
            if (pathPoints.isNotEmpty()) {
                calculateTotalDistance(pathPoints)
            }

            //stopping chronometer counter and tracking on stop clicked
            chronometer.stop()
            isChronoRunning = false

            //stopping tracking and adding marker to map on stop clicked
            if (isTracking) {
                isTracking = false
                if (pathPoints.isNotEmpty()) {
                    googleMap.addMarker(
                        MarkerOptions()
                            .icon(bitmapDescriptorFromVector(this, R.drawable.ic_startmarker))
                            .position(pathPoints.first()).title("Başlangıç")
                    )
                    googleMap.addMarker(
                        MarkerOptions().icon(
                            bitmapDescriptorFromVector(
                                this,
                                R.drawable.ic_endmarker
                            )
                        ).position(pathPoints.last()).title("Bitiş")
                    )
                }

                //removing locationmanager listener
                locationManager.removeUpdates(this)
            }

            //Alert dialog gelecek
            showSaveDialog()
        }


        /*
        btnStart.setOnClickListener {
            Toast.makeText(this, "Baslata bastik", Toast.LENGTH_SHORT).show()

            if (!isChronoRunning) {
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.start()
                isChronoRunning = true
            }

            if (locationPermissionGranted && !isTracking) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
                isTracking = true
            }
        }

 */

        /*
        btnStop.setOnClickListener {
            //Toast.makeText(this, "Durdura bastik", Toast.LENGTH_SHORT).show()
            /**
             * Mesafe hesaplaniyor
             *
             */
            var startLoc = Location("")
            var endLoc = Location("")
            var distance = 0.0f
            var ellapsedTime: Long = 0


            if (pathPoints.isNotEmpty()) {

                startLoc.latitude = pathPoints.first().latitude
                startLoc.longitude = pathPoints.first().longitude
                endLoc.latitude = pathPoints.last().latitude
                endLoc.longitude = pathPoints.last().longitude

                distance = startLoc.distanceTo(endLoc) //sonuc metre olarak doner
                calculateTotalDistance(pathPoints)
            }

            ellapsedTime = SystemClock.elapsedRealtime() - chronometer.base
            chronometer.stop()
            isChronoRunning = false
            Toast.makeText(this, "Mesafe : "+ "%.2f".format(distance) + " metre" + "Sure : $ellapsedTime", Toast.LENGTH_LONG).show()

            if (isTracking) {
                isTracking = false
                if (pathPoints.isNotEmpty()){
                    googleMap.addMarker(MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .position(pathPoints.first()).title("Başlangıç"))
                    googleMap.addMarker(MarkerOptions().position(pathPoints.last()).title("Bitiş"))
                }

                locationManager.removeUpdates(this)
            }

            //Alert dialog gelecek
            showDialog()


        }

       */

    }

    /**
     * alert dialog aciliyor. haritanin kaydedilip kaydedilmeyecegi soruluyor.
     */
    @InternalCoroutinesApi
    private fun showSaveDialog() {

        FancyAlertDialog.Builder(this)
            .setTitle("Activity completed")
            .setBackgroundColor(Color.WHITE)
            .setMessage("Do you want to save your activity?")
            .setNegativeBtnText("No")
            .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))
            .setPositiveBtnText("Yes")
            .setPositiveBtnBackground(Color.parseColor("#FF4081"))
            .setAnimation(Animation.POP)
            .isCancellable(true)
            .setIcon(R.drawable.ic_action_completed, Icon.Visible)
            .OnPositiveClicked {
                if (pathPoints.isNotEmpty()) {
                    saveActivity()
                } else {
                    Toast.makeText(this, "Activity has no values to be saved.", Toast.LENGTH_SHORT)
                        .show()
                    this.finish()
                }
            }
            .OnNegativeClicked {
                Toast.makeText(this, "Activity wont be saved.", Toast.LENGTH_SHORT).show()
                this.finish()
            }.build()
    }

    @InternalCoroutinesApi
    private fun saveActivity() {

        Toast.makeText(this, "Saving activity", Toast.LENGTH_SHORT).show()


        var runActivity = RunActivity(startTimeMillis, endTimeInMillis, pathPoints)

        /**
         * Inserting RunActivity object to database
         */
        launch {
            this@MapsActivity.let {
                RunDatabase(it).getRunActivityDAO().addRun(runActivity)
            }
        }

        //Returning to home page when activity save is successfull
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    /**
     * Calculating total distance of running path
     * Baslangic noktasindan itibaren kosulan yolun uzunlugu metre olarak hesaplaniyor
     */
    private fun calculateTotalDistance(pathPoints: MutableList<LatLng>) {
        var startLoc = Location("")
        var endLoc = Location("")
        var totalDistance = 0.0f
        var currentPoint = pathPoints.first()

        for (point in pathPoints) {
            Log.e("LOCATTION", "${point.latitude},${point.longitude}")
            if (point != pathPoints.first()) {
                startLoc.latitude = currentPoint.latitude
                startLoc.longitude = currentPoint.longitude
                endLoc.latitude = point.latitude
                endLoc.longitude = point.longitude
                totalDistance = totalDistance + startLoc.distanceTo(endLoc)
                currentPoint = point
            }
        }
        Log.e("TOTALDISTANCE", totalDistance.toString())


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        this.googleMap = googleMap

        setupMap(googleMap)


    }

    /**
     * Google map hazirlanirken mevcut konuma zoom yapiliyor. Mevcut konum yoksa daha önceden kullanilan
     * konuma mavi nokta koyuluyor.
     */
    private fun setupMap(googleMap: GoogleMap) {
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, MAP_ZOOM))
            }
        }
    }

    /**
     * On any location change, new location is being added to path list.
     */
    override fun onLocationChanged(newLocation: Location?) {
        if (isTracking) {

            addPathPoint(newLocation)

            //Log.d(TAG, "Location changed : ${newLocation?.latitude}, ${newLocation?.longitude}")
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        //Log.d(TAG, "Status change: $status $provider")
    }

    override fun onProviderEnabled(provider: String?) {
        //Log.d(TAG, "Provider enabled: $provider")
    }

    override fun onProviderDisabled(provider: String?) {
        //Log.d(TAG, "Provider disabled: $provider")
    }

    private fun addPathPoint(location: Location?) {
        if (location != null) {
            val position = LatLng(location.latitude, location.longitude)
            if (pathPoints.isEmpty()) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM))

            }
            //listeye yeni position ekleniyor
            pathPoints.add(position)
            //tum liste map uzerinde polyline araciligiyla ciziliyor
            googleMap.addPolyline(
                PolylineOptions()
                    .color(Color.RED)
                    .width(POLYLINE_WIDTH)
                    .addAll(pathPoints)
            )
            // mevcut konuma gore hareket etmesi saglaniyor.
            //googleMap.addMarker(MarkerOptions().position(position).title("Buradasın"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM))


        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
    }


    private fun isLocationEnabled(mContext: Context): Boolean {
        val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val result = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Log.e(TAG, "LOCATIONPROVIDERENABLED : ${result}")
        return result
        //|| lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun createLocationRequest() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel") { paramDialogInterface, paramInt ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        dialog.show()
    }


}

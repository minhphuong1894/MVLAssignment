package com.mvl.assignment.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.mvl.assignment.R
import com.mvl.assignment.base.BaseFragment
import com.mvl.assignment.databinding.FragmentMapBinding
import com.mvl.assignment.domain.model.LocationTemp
import com.mvl.assignment.domain.model.LocationTemps
import com.mvl.assignment.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.roundToInt


@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {

    private val viewModel: MapViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.fragment_map

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val args: MapFragmentArgs by navArgs()

    private var mLatLng: LatLng? = LatLng(10.490545, 107.173669)

    private lateinit var mMap: GoogleMap

    private var mLocationFirst: LocationTemp? = LocationTemp(1, "", "", 0.0, 0.0, "")
    private var mLocationSecond: LocationTemp? = LocationTemp(2, "", "", 0.0, 0.0, "")
    private var mLocationTemps = LocationTemps()

    private var isMoving = false

    private var mCircleRadius = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.mapView.onCreate(savedInstanceState)
        viewBinding.mapView.onResume()
        viewBinding.mapView.getMapAsync(this)
        setupLocationClient()
        initView()
        requestSinglePermissionWithListener()

        viewModel.uiState().observe(viewLifecycleOwner, { state ->
            viewBinding.tvAgi.text = state.data.data?.aqi.toString()
        })

        initBundle()
    }

    private fun initBundle() {
        val historyItemArgs = args.historyItem
        with(viewBinding) {
            historyItemArgs?.a?.let {
                if (it.nickName.isEmpty()) {
                    tvLocationA.text = it.address
                } else {
                    tvLocationA.text = it.nickName
                }
                mLocationFirst = it
                mLocationTemps.add(it)
            }

            historyItemArgs?.b?.let {
                if (it.nickName.isEmpty()) {
                    tvLocationB.text = it.address
                } else {
                    tvLocationB.text = it.nickName
                }
                mLocationSecond = it
                mLocationTemps.add(it)
                mLatLng = LatLng(it.lat, it.lng)
            }
        }
        refreshView()
    }

    private fun setupLocationClient() {
        mFusedLocationClient =
            activity?.let { LocationServices.getFusedLocationProviderClient(it) }!!
    }

    private fun getGeocoderAddress(lat: Double, lng: Double): Address {
        val addresses: List<Address?>
        val geocoder = Geocoder(context, Locale.getDefault())
        addresses = geocoder.getFromLocation(lat, lng, 1)
        return addresses[0]!!
    }

    private fun initView() {
        viewBinding.apply {
            btnSet.setOnClickListener {
                when {
                    tvLocationA.text.isNullOrEmpty() -> {
                        val address = getGeocoderAddress(
                            mLatLng!!.latitude,
                            mLatLng!!.longitude
                        ).getAddressLine(0)
                        viewBinding.tvLocationA.text = address
                        mLocationFirst =
                            LocationTemp(
                                1,
                                address, "", mLatLng!!.latitude, mLatLng!!.longitude,
                                viewBinding.tvAgi.text.toString()
                            )
                        mLocationTemps.add(mLocationFirst!!)
                        refreshView()
                    }
                    tvLocationB.text.isNullOrEmpty() -> {
                        val address = getGeocoderAddress(
                            mLatLng!!.latitude,
                            mLatLng!!.longitude
                        ).getAddressLine(0)
                        viewBinding.tvLocationB.text = address
                        mLocationSecond =
                            LocationTemp(
                                2,
                                address, "", mLatLng!!.latitude, mLatLng!!.longitude,
                                viewBinding.tvAgi.text.toString()
                            )
                        mLocationTemps.add(mLocationSecond!!)
                        refreshView()
                    }
                    tvLocationA.text.isNotEmpty() && viewBinding.tvLocationB.text.isNotEmpty() -> {
                        val actionFragment =
                            MapFragmentDirections.actionMapFragmentToBookFragment(mLocationTemps)
                        actionFragment.let { it1 -> findNavController().navigate(it1) }
                    }
                }
            }

            tvLocationA.setSingleClick {
                mLocationTemps.clear()
                mLocationTemps.add(mLocationFirst!!)
                mLocationTemps.add(mLocationSecond!!)
                if (viewBinding.tvLocationA.text.isNotEmpty()) {
                    val actionFragment =
                        mLocationFirst?.let { it1 ->
                            MapFragmentDirections.actionMapFragmentToNickNameFragment(
                                mLocationFirst!!.id, mLocationTemps
                            )
                        }
                    actionFragment?.let { it1 -> findNavController().navigate(it1) }
                } else {
                    val actionFragment =
                        MapFragmentDirections.actionMapFragmentToLocationFragment(
                            mLocationFirst!!.id,
                            mLocationTemps
                        )
                    actionFragment.let { it1 -> findNavController().navigate(it1) }
                }

            }

            tvLocationB.setSingleClick {
                mLocationTemps.clear()
                mLocationTemps.add(mLocationFirst!!)
                mLocationTemps.add(mLocationSecond!!)
                if (viewBinding.tvLocationB.text.isNotEmpty()) {
                    val actionFragment =
                        mLocationSecond?.let { it1 ->
                            MapFragmentDirections.actionMapFragmentToNickNameFragment(
                                mLocationSecond!!.id, mLocationTemps
                            )
                        }
                    actionFragment?.let { it1 -> findNavController().navigate(it1) }
                } else {
                    val actionFragment =
                        MapFragmentDirections.actionMapFragmentToLocationFragment(
                            mLocationSecond!!.id,
                            mLocationTemps
                        )
                    actionFragment.let { it1 -> findNavController().navigate(it1) }
                }

            }
        }
    }

    private fun refreshView() {
        if (viewBinding.tvLocationA.text.isEmpty()) {
            viewBinding.btnSet.text = "Set A"
        } else if (viewBinding.tvLocationB.text.isEmpty()) {
            viewBinding.btnSet.text = "Set B"
        } else if (viewBinding.tvLocationA.text.isNotEmpty() && viewBinding.tvLocationB.text.isNotEmpty()) {
            viewBinding.btnSet.text = "Book"
        }
    }

    private fun resizeLayout(backToNormalSize: Boolean) {
        val params = viewBinding.pinViewCircleFrame.layoutParams as FrameLayout.LayoutParams
        val vto: ViewTreeObserver = viewBinding.pinViewCircleFrame.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewBinding.pinViewCircleFrame.viewTreeObserver.removeGlobalOnLayoutListener(this)
                mCircleRadius = viewBinding.pinViewCircleFrame.measuredWidth
            }
        })
        if (backToNormalSize) {
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params.topMargin = 0
        } else {
            params.topMargin = ((mCircleRadius * 0.3).roundToInt())
            params.height = mCircleRadius - mCircleRadius / 3
            params.width = mCircleRadius - mCircleRadius / 3
        }
        viewBinding.pinViewCircleFrame.layoutParams = params
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.animateCamera(CameraUpdateFactory.newLatLng(mLatLng!!))
        mMap.animateCamera(CameraUpdateFactory.newLatLng(mLatLng!!))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng!!, 15f))

        mMap.setOnCameraMoveStartedListener {
            isMoving = true
            viewBinding.tvAgi.visibility = View.GONE
            viewBinding.pgAgi.visibility = View.GONE
            viewBinding.pinViewCircleFrame.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.circle_background_moving)
            resizeLayout(false)
        }

        mMap.setOnCameraIdleListener {
            isMoving = false
            viewBinding.tvAgi.visibility = View.INVISIBLE
            viewBinding.pgAgi.visibility = View.VISIBLE
            resizeLayout(true)

            mLatLng = mMap.cameraPosition.target
            viewModel.loadAqiByLocation(mLatLng!!.latitude, mLatLng!!.longitude)
            //viewModel.loadAqiByLocation(37.5642,127.0016)

            Handler(Looper.getMainLooper()).postDelayed({
                viewBinding.pinViewCircleFrame.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.circle_background)
                if (!isMoving) {
                    viewBinding.pinViewCircleFrame.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.circle_background)
                    viewBinding.tvAgi.visibility = View.VISIBLE
                    viewBinding.pgAgi.visibility = View.GONE
                }
            }, 1500)
        }

        MapsInitializer.initialize(requireContext())
    }

    // single permission
    private val singlePermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val singlePermissionCode = 2001

    /**
     * request single permission with listener
     */
    private fun requestSinglePermissionWithListener() {
        requestPermissions(
            singlePermission,
            singlePermissionCode,
            object : RequestPermissionListener {
                override fun onPermissionRationaleShouldBeShown(requestPermission: () -> Unit) {
                    showDialog(
                        message = "Please allow permission to use this feature",
                        textPositive = "OK",
                        positiveListener = {
                            requestPermission.invoke()
                        },
                        textNegative = "Cancel"
                    )
                }

                override fun onPermissionPermanentlyDenied(openAppSetting: () -> Unit) {
                    showDialog(
                        message = "Permission Disabled, Please allow permission to use this feature",
                        textPositive = "OK",
                        positiveListener = {
                            openAppSetting.invoke()
                        },
                        textNegative = "Cancel"
                    )
                }


                override fun onPermissionGranted() {
                    getCurrentLocation()
                }
            })
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        mFusedLocationClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful) {
                val locationLasted = it.result
                goToLocation(locationLasted.latitude, locationLasted.longitude)
            }
        }
    }

    private fun goToLocation(lat: Double, lng: Double) {
        mLatLng = LatLng(lat, lng)
        viewModel.loadAqiByLocation(lat, lng)
        //viewModel.loadAqiByLocation(37.5642,127.0016)
        val mCameraUpdate = CameraUpdateFactory.newLatLngZoom(mLatLng!!, 15f)
        mMap.moveCamera(mCameraUpdate)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // single permission
        handleOnRequestPermissionResult(
            singlePermissionCode,
            requestCode,
            permissions,
            grantResults,
            object : PermissionResultListener {
                override fun onPermissionRationaleShouldBeShown() {
                    showToast("permission denied")
                }

                override fun onPermissionPermanentlyDenied() {
                    showToast("permission permanently disabled")
                }

                override fun onPermissionGranted() {
                    getCurrentLocation()
                }
            })
    }

    fun showToast(message: String) {
        Toast.makeText(context, "Fragment $message", Toast.LENGTH_SHORT).show()
    }

}
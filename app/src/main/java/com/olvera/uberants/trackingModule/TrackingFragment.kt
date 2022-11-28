package com.olvera.uberants.trackingModule

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.google.maps.android.SphericalUtil
import com.olvera.uberants.R
import com.olvera.uberants.common.dataAccess.FakeDatabase
import com.olvera.uberants.common.dataAccess.RemoteDatabase
import com.olvera.uberants.common.entities.DeliveryInfo
import com.olvera.uberants.common.utils.Constants
import com.olvera.uberants.common.utils.MapUtils
import com.olvera.uberants.common.utils.MapUtils.locationRequest
import com.olvera.uberants.databinding.FragmentTrackingBinding
import kotlinx.coroutines.launch

class TrackingFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap

    private var locations = mutableListOf<LatLng>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var locationCallBack = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)

            result.locations.run {
                this.forEach{locations.add(LatLng(it.latitude, it.longitude))}
                MapUtils.addPolyline(map, locations)

                if (locations.isNotEmpty()) {
                    calcRealDistance(locations.last())
                    MapUtils.runDeliveryMap(requireActivity(), map, locations.last())
                }
            }
        }
    }

    private var mapFragment: SupportMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTrackingBinding.inflate(LayoutInflater.from(context))

        mapFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
        setupDeliveryUserToUI()

        MapUtils.setupMarkersData(requireActivity(), TrackingFragmentArgs.fromBundle(requireArguments()).totalProducts)
    }

    @SuppressLint("MissingPermission")
    private fun setupButtons() {
        binding.btnFinish.setOnClickListener {
            fusedLocationClient.removeLocationUpdates(locationCallBack)
            NavHostFragment.findNavController(this).navigate(R.id.action_tracking_to_products)
        }

        binding.btnGo.setOnClickListener {

            map.clear()
            MapUtils.addDestinationMarker(map, MapUtils.getDestinationDelivery())

            getEstimatedMeasure()
            MapUtils.addRoute(map)

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.getMainLooper())
        }
    }

    private fun setupDeliveryUserToUI() {
        val user = FakeDatabase.getDeliveryUser()
        with(binding) {
            tvName.setText(getString(R.string.tracking_name, user.name))
            Glide.with(this@TrackingFragment)
                .load(user.photoUrl)
                .circleCrop()
                .into(imgPhoto)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapFragment?.let {
            requireActivity().supportFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        MapUtils.setupMap(requireActivity(), googleMap)

        getEstimatedRoute()
    }

    private fun calcRealDistance(location: LatLng) {
        val distance = SphericalUtil.computeDistanceBetween(location, MapUtils.getDestinationDelivery())
        binding.tvDistance.setText(getString(R.string.tracking_distance, MapUtils.formatDistance(distance)))
        binding.btnFinish.isEnabled = distance < 60
    }

    private fun getEstimatedMeasure() {
        lifecycleScope.launch {
            val remoteDatabase = RemoteDatabase()
            val o = MapUtils.getOriginDelivery()
            val d = MapUtils.getDestinationDelivery()
            val result = remoteDatabase.getMeasures(Constants.METRIC_VALUE, "${o.latitude},${o.longitude}",
                "${d.latitude},${d.longitude}", Constants.KEY_DISTANCE_MATRIX)

            setupDeliveryInfoToUI(result)
        }
    }

    private fun setupDeliveryInfoToUI(info: DeliveryInfo) {
        with(binding) {
            tvTime.setText(info.rows[0].elements[0].duration.text)
            tvOrigin.setText(getString(R.string.tracking_origin, info.origin_addresses[0]))
            tvDestination.setText(getString(R.string.tracking_destination, info.destination_addresses[0]))
            tvTotalDistance.setText(getString(R.string.tracking_total_distance, info.rows[0].elements[0].distance.text))
        }
    }

    private fun getEstimatedRoute() {
        lifecycleScope.launch {
            val remoteDatabase = RemoteDatabase()
            val o = MapUtils.getOriginDelivery()
            val d = MapUtils.getDestinationDelivery()
            val result = remoteDatabase.getEstimatedRoute(Constants.METRIC_VALUE, "${o.latitude},${o.longitude}",
                "${d.latitude},${d.longitude}", Constants.DRIVING_VALUE, Constants.KEY_DISTANCE_MATRIX)

            MapUtils.addRoute(map, result.routes[0].bounds, result.routes[0].overview_polyline.points)
        }
    }


}
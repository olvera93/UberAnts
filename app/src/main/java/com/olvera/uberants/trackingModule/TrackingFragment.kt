package com.olvera.uberants.trackingModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.google.maps.android.SphericalUtil
import com.olvera.uberants.R
import com.olvera.uberants.common.dataAccess.FakeDatabase
import com.olvera.uberants.common.utils.MapUtils
import com.olvera.uberants.databinding.FragmentTrackingBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackingFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap

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

        val mapFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
        setupDeliveryUserToUI()

        MapUtils.setupMarkersData(requireActivity(), TrackingFragmentArgs.fromBundle(requireArguments()).totalProducts)
    }

    private fun setupButtons() {
        binding.btnFinish.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_tracking_to_products)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        MapUtils.setupMap(requireActivity(), googleMap)

        //todo move this
        calcRealDistance(MapUtils.getOriginDelivery())
    }

    private fun calcRealDistance(location: LatLng) {
        val distance = SphericalUtil.computeDistanceBetween(location, MapUtils.getDestinationDelivery())
        binding.tvDistance.setText(getString(R.string.tracking_distance, MapUtils.formatDistance(distance)))
        binding.btnFinish.isEnabled = distance < 60
    }


}
package com.mvl.assignment.ui.location

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvl.assignment.R
import com.mvl.assignment.base.BaseFragment
import com.mvl.assignment.base.getNavController
import com.mvl.assignment.databinding.FragmentLocationBinding
import com.mvl.assignment.domain.model.*
import com.mvl.assignment.util.setSingleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : BaseFragment<FragmentLocationBinding>() {

    override val layoutId: Int = R.layout.fragment_location

    private var mLocationTemps = LocationTemps()

    private val args: LocationFragmentArgs by navArgs()

    private val mLocationAdapter = LocationAdapter(itemClickListener = { it ->
        it.let {
            val locationA = args.locationTemp[0]
            val locationB = args.locationTemp[1]
            if (args.idLocation == 1) {
                locationA.address = it!!.address
                locationA.nickName = it.nickName
                locationA.lat = it.lat
                locationA.lng = it.lng
                locationA.aqi = it.aqi
            } else {
                locationB.address = it!!.address
                locationB.nickName = it.nickName
                locationB.lat = it.lat
                locationB.lng = it.lng
                locationB.aqi = it.aqi
            }
            val itemHistory = HistoryItem(locationA, locationB, 0.0)
            val actionFragment =
                LocationFragmentDirections.actionLocationFragmentToMapFragment(itemHistory)
            actionFragment.let { it1 -> findNavController().navigate(it1) }
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        initView()

        initHandle()
    }

    private fun initData() {
        mLocationTemps.add(tempSeoulLocation())
        mLocationTemps.add(tempCanadaLocation())
        mLocationTemps.add(tempHKLocation())
        mLocationTemps.add(tempUSALocation())
        mLocationAdapter.submitList(mLocationTemps)
    }

    private fun initView() {

        viewBinding.llToolbar.imvBackWebView.setSingleClick { getNavController()?.popBackStack() }

        viewBinding.recyclerLocation.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = mLocationAdapter
        }


    }

    private fun initHandle() {
        viewBinding.llToolbar.tvTitle.text = "Location"
        viewBinding.llToolbar.imvBackWebView.setSingleClick { getNavController()?.popBackStack() }

    }
}
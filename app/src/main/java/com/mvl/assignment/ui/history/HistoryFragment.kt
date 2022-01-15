package com.mvl.assignment.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvl.assignment.R
import com.mvl.assignment.base.BaseFragment
import com.mvl.assignment.base.getNavController
import com.mvl.assignment.databinding.FragmentHistoryBinding
import com.mvl.assignment.domain.model.HistoryItem
import com.mvl.assignment.domain.model.LocationTemp
import com.mvl.assignment.domain.model.tempHistoryLocation
import com.mvl.assignment.util.setSingleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    private val viewModel: HistoryViewModel by viewModels()

    private var historyAdapter = HistoryAdapter()

    private var mLocationFirst: LocationTemp? = LocationTemp(1,"", "", 0.0, 0.0, "")
    private var mLocationSecond: LocationTemp? = LocationTemp(2,"", "", 0.0, 0.0, "")


    override val layoutId: Int
        get() = R.layout.fragment_history

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        initHandle()
    }

    private fun initHandle() {
        viewBinding.llToolbar.imvBackWebView.setSingleClick {
            val itemHistory = HistoryItem(mLocationFirst, mLocationSecond, 0.0)
            val actionFragment =
                HistoryFragmentDirections.actionHistoryFragmentToMapFragment(itemHistory)
            actionFragment.let { it1 -> findNavController().navigate(it1) }
        }
    }

    private fun initView() {
        with(viewBinding) {
            historyAdapter = HistoryAdapter { it ->

                if (it.a != null && it.b != null) {
                    val locationA =
                        LocationTemp(1,it.a.address, it.a.nickName, it.a.lat, it.a.lng, it.a.aqi)
                    val locationB =
                        LocationTemp(2,it.b.address, it.b.nickName, it.b.lat, it.b.lng, it.b.aqi)
                    val itemHistory = HistoryItem(locationA, locationB, it.price)
                    val actionFragment =
                        HistoryFragmentDirections.actionHistoryFragmentToMapFragment(itemHistory)
                    actionFragment.let { it1 -> findNavController().navigate(it1) }
                }

            }
            viewModel.uiState().observe(viewLifecycleOwner, { state ->
                historyAdapter.submitList(state.histories.data)
            })
            historyAdapter.submitList(tempHistoryLocation().data)
            listHistory.apply {
                layoutManager =
                    LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                adapter = historyAdapter
            }
            valueTotalCount.text = historyAdapter.currentList.size.toString()
            valueTotalPrice.text =
                historyAdapter.currentList.sumOf { history -> history.price!! }.toString()
        }
    }
}
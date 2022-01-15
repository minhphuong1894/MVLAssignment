package com.mvl.assignment.ui.book

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mvl.assignment.R
import com.mvl.assignment.base.BaseFragment
import com.mvl.assignment.base.getNavController
import com.mvl.assignment.databinding.FragmentBookBinding
import com.mvl.assignment.util.setSingleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookFragment : BaseFragment<FragmentBookBinding>() {

    override val layoutId: Int = R.layout.fragment_book

    private val args: BookFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        initHandle()
    }

    private fun initView() {

        viewBinding.llToolbar.tvTitle.text = "Book"

        if (args.locationTemp.isNotEmpty()){
            val locationA = args.locationTemp[0]
            val locationB = args.locationTemp[1]
            if (locationA.nickName.isEmpty()) {
                viewBinding.tvFirstLocationName.text = locationA.address
                viewBinding.tvFirstAAqi.text = locationA.aqi
            } else {
                viewBinding.tvFirstLocationName.text = locationA.nickName
                viewBinding.tvFirstAAqi.text = locationA.aqi
            }

            if (locationB.nickName.isEmpty()) {
                viewBinding.tvSecondLocationName.text = locationB.address
                viewBinding.tvSecondAqi.text = locationA.aqi
            } else {
                viewBinding.tvSecondLocationName.text = locationB.nickName
                viewBinding.tvSecondAqi.text = locationA.aqi
            }
        }

    }

    private fun initHandle(){
        viewBinding.btnBook.setSingleClick {
            findNavController().navigate(
                BookFragmentDirections.actionBookFragmentToHistoryFragment()
            )
        }
        viewBinding.llToolbar.imvBackWebView.setSingleClick { getNavController()?.popBackStack() }

    }
}
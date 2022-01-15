package com.mvl.assignment.ui.nickName

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mvl.assignment.R
import com.mvl.assignment.base.BaseFragment
import com.mvl.assignment.base.getNavController
import com.mvl.assignment.databinding.FragmentNickNameBinding
import com.mvl.assignment.domain.model.HistoryItem
import com.mvl.assignment.ui.history.HistoryFragmentDirections
import com.mvl.assignment.util.setSingleClick
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NickNameFragment : BaseFragment<FragmentNickNameBinding>() {

    override val layoutId: Int = R.layout.fragment_nick_name

    private val args: NickNameFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        initHandle()
    }

    private fun initView() {

        if (args.locationTemp.isNotEmpty()){
            if (args.idLocation == 1){
                viewBinding.tvLocationName.text = args.locationTemp[0].address
                viewBinding.tvAqi.text = args.locationTemp[0].aqi
            }else{
                viewBinding.tvLocationName.text = args.locationTemp[1].address
                viewBinding.tvAqi.text = args.locationTemp[1].aqi
            }

        }
    }

    private fun initHandle(){
        viewBinding.llToolbar.imvBackWebView.setSingleClick { getNavController()?.popBackStack() }
        viewBinding.nickNameBtn.setSingleClick {
            if (args.idLocation == 1){
                args.locationTemp[0].nickName = viewBinding.nickNameEdit.text.toString().trim()
            }else{
                args.locationTemp[1].nickName = viewBinding.nickNameEdit.text.toString().trim()
            }
            val itemHistory = HistoryItem(args.locationTemp[0], args.locationTemp[1], 0.0)
            val actionFragment =
                NickNameFragmentDirections.actionNickNameFragmentToMapFragment2(itemHistory)
            actionFragment.let { it1 -> findNavController().navigate(it1) }

        }
    }
}
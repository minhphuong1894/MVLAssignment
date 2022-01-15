package com.mvl.assignment.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mvl.assignment.R
import com.mvl.assignment.util.dismissLLoadingDialog
import com.mvl.assignment.util.showDialog
import com.mvl.assignment.util.showLoadingDialog

abstract class BaseActivity<ViewBinding : ViewDataBinding, ViewModel : BaseViewModel> :
    AppCompatActivity() {

    protected lateinit var viewBinding: ViewBinding
    protected abstract val viewModel: ViewModel

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, layoutId)
        viewBinding.apply {
            setVariable(1, viewModel)
            viewBinding.lifecycleOwner = this@BaseActivity
            root.isClickable = true
            executePendingBindings()
        }
        observeErrorEvent()
    }

    private fun observeErrorEvent() {
        viewModel.apply {
            isLoading.observe(this@BaseActivity, {
                handleLoading(it == true)
            })
            errorMessage.observe(this@BaseActivity, {
                handleErrorMessage(it)
            })
            noInternetConnectionEvent.observe(this@BaseActivity, {
                handleErrorMessage(getString(R.string.no_internet_connection))
            })
            connectTimeoutEvent.observe(this@BaseActivity, {
                handleErrorMessage(getString(R.string.connect_timeout))
            })
            unknownErrorEvent.observe(this@BaseActivity, {
                handleErrorMessage(getString(R.string.unknown_error))
            })
        }
    }

    /**
     * override this if not use loading dialog (example progress bar)
     */
    protected open fun handleLoading(isLoading: Boolean) {
        if (isLoading) showLoadingDialog() else dismissLLoadingDialog()
    }

    protected open fun handleErrorMessage(message: String?) {
        if (message.isNullOrBlank()) return
        dismissLLoadingDialog()
        showDialog(
            message = message,
            textPositive = getString(R.string.ok)
        )
    }
}

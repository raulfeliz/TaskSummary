package com.fireflylearning.tasksummary.login.views

import android.app.AlertDialog
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import com.fireflylearning.tasksummary.FireflyApp
import com.fireflylearning.tasksummary.R
import com.fireflylearning.tasksummary.activities.LoginActivity
import com.fireflylearning.tasksummary.databinding.ActivityLoginBinding
import com.fireflylearning.tasksummary.dependencyinjection.modules.LoginModule
import com.fireflylearning.tasksummary.dependencyinjection.scopes.CustomScopes
import com.fireflylearning.tasksummary.login.lifecycleobservers.LoginLifecycleObserver
import com.fireflylearning.tasksummary.login.presenters.LoginPresenter
import com.fireflylearning.tasksummary.login.viewmodels.LoginViewModel
import com.fireflylearning.tasksummary.model.CustomLiveData
import com.fireflylearning.tasksummary.utils.FireflyConstants
import com.fireflylearning.tasksummary.utils.logger.LoggerHelper
import com.fireflylearning.tasksummary.utils.ui.BaseActivity
import com.fireflylearning.tasksummary.utils.ui.GlideBindingComponent
import javax.inject.Inject


@CustomScopes.ActivityScope
class LoginActivity : BaseActivity(), LoginView {

    @Inject
    lateinit var log: LoggerHelper

    @Inject
    lateinit var presenter: LoginPresenter

    @Inject
    lateinit var observer: LoginLifecycleObserver

    lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //dependency injection
        (application as FireflyApp).mComponent
                .getLoginSubcomponent(LoginModule(this)).inject(this)

        //databinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login, GlideBindingComponent())

        mBinding.presenter = presenter

    }


    // region LOGIN VIEW
    override fun addLifecycleObserver(observer: LoginLifecycleObserver) {
        if(observer is LifecycleObserver){
            lifecycle.addObserver(observer)
        }
    }

    override fun showErrorInHost(state: Boolean) {
        when(state){
            true -> showErrorInTextField(mBinding.host)
            false -> removeErrorInTextField(mBinding.host)
        }

    }

    override fun showErrorInToken(state: Boolean) {
        when(state){
            true -> showErrorInTextField(mBinding.token)
            false -> removeErrorInTextField(mBinding.token)
        }
    }

    private fun showErrorInTextField(text: EditText){
        text.error = getString(R.string.error_field_required)
        text.requestFocus()
    }

    private fun removeErrorInTextField(text: EditText){
        text.error = null
    }

    override fun goToTaskListView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showErrorFromResponse(error: FireflyConstants.TokenError) {
        val dialog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(this)
        }
        dialog.setCancelable(true)
        dialog.setTitle(R.string.title_error)

        val message: String = when (error) {
            FireflyConstants.TokenError.NETWORK_ERROR -> resources.getString(R.string.error_internet_connection)
            FireflyConstants.TokenError.HOST_ERROR -> resources.getString(R.string.error_invalid_host)
            FireflyConstants.TokenError.INVALID_TOKEN -> resources.getString(R.string.error_invalid_token)
            else -> ""
        }

        dialog.setMessage(message)

        dialog.setPositiveButton(resources.getString(R.string.accept)) { _, _ -> }

        val alert = dialog.create()
        alert.show()

    }

    override fun getLiveStatus(): CustomLiveData<FireflyConstants.TokenError> {
        return ViewModelProviders.of(this).get(LoginViewModel::class.java).status
    }

    //endregion
}

//todo hacer lo del texeditor
package com.fireflylearning.tasksummary.login.presenters

import android.text.TextUtils
import com.fireflylearning.tasksummary.dependencyinjection.scopes.CustomScopes
import com.fireflylearning.tasksummary.login.views.LoginView
import com.fireflylearning.tasksummary.model.Task
import com.fireflylearning.tasksummary.network.logic.NetworkManager
import com.fireflylearning.tasksummary.safe
import com.fireflylearning.tasksummary.utils.FireflyConstants
import com.fireflylearning.tasksummary.utils.logger.LoggerHelper
import com.fireflylearning.tasksummary.utils.preferences.PreferencesManager
import com.fireflylearning.tasksummary.utils.resources.ResourcesManager
import com.fireflylearning.tasksummary.utils.ui.MyLivedataObserver
import java.lang.ref.WeakReference
import javax.inject.Inject


/**
 * Created by Roll on 1/9/17.
 */
@CustomScopes.ActivityScope
class LoginPresenterAndroidImpl @Inject constructor(val mView: WeakReference<LoginView>): LoginPresenter, MyLivedataObserver {

    @Inject
    lateinit var preferences: PreferencesManager

    @Inject
    lateinit var resources: ResourcesManager

    @Inject
    lateinit var log: LoggerHelper

    @Inject
    lateinit var network: NetworkManager

    override fun attemptLogin(host: String, token: String) {
        // Reset errors.
        mView.safe {
            mView.get()!!.showErrorInHost(false)
            mView.get()!!.showErrorInToken(false)

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(token)) {
                mView.get()!!.showErrorInToken(true)
                return@safe
            }

            // Check for a valid host address.
            if (TextUtils.isEmpty(host)) {
                mView.get()!!.showErrorInHost(true)
                return@safe
            }

            // Store values at the time of the login attempt.
            preferences.setHost(host)
            preferences.setSecretToken(token)

            mView.get()!!.showProgressBar()
            network.login(host, token, mView.get()!!.getLiveStatus())

        }
    }


    //region OBSERVER
    override fun handleChangesInObservedStatus(status: FireflyConstants.TokenError) {
        log.d(this, "status updated")
        mView.safe {
            mView.get()!!.hideProgressBar()
            when (status) {
                FireflyConstants.TokenError.RESPONSE_OK -> {
                    //todo lanzar el task
                }
                FireflyConstants.TokenError.NO_OP -> {
                }
                FireflyConstants.TokenError.NETWORK_ERROR, FireflyConstants.TokenError.HOST_ERROR,
                FireflyConstants.TokenError.INVALID_TOKEN -> showErrorFromResponse(status)
            }
        }

    }

    override fun handleChangesInObservedTasks(tasks: MutableList<Task>) {
        //do nothing
    }
    // endregion

}
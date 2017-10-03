package com.fireflylearning.tasksummary.di.modules

import android.app.Application
import android.content.Context
import com.fireflylearning.tasksummary.utils.logger.AndroidLoggerHelperImpl
import com.fireflylearning.tasksummary.utils.logger.LoggerHelper
import com.fireflylearning.tasksummary.FireflyApp
import com.fireflylearning.tasksummary.network.logic.NetworkManager
import com.fireflylearning.tasksummary.network.logic.NetworkManagerAndroidImpl
import com.fireflylearning.tasksummary.persistence.PersistenceManager
import com.fireflylearning.tasksummary.persistence.PersistenceManagerImplAndroid
import com.fireflylearning.tasksummary.utils.resources.ResourcesManager
import com.fireflylearning.tasksummary.utils.resources.ResourcesManagerAndroidImpl
import com.fireflylearning.tasksummary.utils.preferences.PreferencesManagerImpl
import com.fireflylearning.tasksummary.utils.preferences.PreferencesManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Roll on 31/8/17.
 */
@Module
@Singleton
class FireflyAppModule {

    /*@Provides
    fun providesApplication(application: FireflyApp): FireflyApp{
        return application
    }*/

    @Provides
    fun providesContext(application: FireflyApp): Context{
        return application.applicationContext
    }

    @Provides
    fun providesNetworkManager(network: NetworkManagerAndroidImpl): NetworkManager {
        return network
    }

    @Provides
    fun providesLogHelper(logger: AndroidLoggerHelperImpl): LoggerHelper {
        return logger
    }

    @Provides
    fun providesResourcesManager(resources: ResourcesManagerAndroidImpl): ResourcesManager {
        return resources
    }

    @Provides
    fun providesPreferencesManager(preferences: PreferencesManagerImpl): PreferencesManager {
        return preferences
    }

    @Provides
    fun providesPersistenceManager(persistence: PersistenceManagerImplAndroid): PersistenceManager{
        return persistence
    }
}
package com.fireflylearning.tasksummary.dependencyinjection.components

import com.fireflylearning.tasksummary.dependencyinjection.modules.FireflyAppModule
import com.fireflylearning.tasksummary.dependencyinjection.modules.LoginModule
import com.fireflylearning.tasksummary.dependencyinjection.modules.TaskListModule
import com.fireflylearning.tasksummary.dependencyinjection.subcomponents.LoginSubcomponent
import com.fireflylearning.tasksummary.dependencyinjection.subcomponents.TaskListSubcomponent
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Roll on 31/8/17.
 */
@Singleton
@Component(modules = arrayOf(FireflyAppModule::class))
interface FireflyAppComponent {
    fun getTaskListSubcomponent(module: TaskListModule): TaskListSubcomponent
    fun getLoginSubcomponent(module: LoginModule): LoginSubcomponent

}
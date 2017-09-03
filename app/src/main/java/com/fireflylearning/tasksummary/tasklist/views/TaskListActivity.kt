package com.fireflylearning.tasksummary.tasklist.views

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.fireflylearning.tasksummary.FireflyApp
import com.fireflylearning.tasksummary.R
import com.fireflylearning.tasksummary.databinding.ActivityMainBinding
import com.fireflylearning.tasksummary.dependencyinjection.modules.TaskListModule
import com.fireflylearning.tasksummary.dependencyinjection.scopes.CustomScopes
import com.fireflylearning.tasksummary.model.CustomLiveData
import com.fireflylearning.tasksummary.model.Task
import com.fireflylearning.tasksummary.tasklist.adapters.TaskListAdapter
import com.fireflylearning.tasksummary.tasklist.lifecycleobservers.TaskListLifecycleObserver
import com.fireflylearning.tasksummary.tasklist.presenters.TaskListPresenter
import com.fireflylearning.tasksummary.tasklist.viewmodels.TaskListViewModel
import com.fireflylearning.tasksummary.utils.logger.LoggerHelper
import com.fireflylearning.tasksummary.utils.ui.BaseActivity
import com.rukiasoft.newrukiapics.preferences.interfaces.PreferencesManager
import javax.inject.Inject


@CustomScopes.ActivityScope
class TaskListActivity : BaseActivity(), TaksListView {

    @Inject
    lateinit var presenter: TaskListPresenter

    @Inject
    lateinit var observer: TaskListLifecycleObserver

    @Inject
    lateinit var log: LoggerHelper

    @Inject
    protected lateinit var adapter: TaskListAdapter

    @Inject
    lateinit var preferences: PreferencesManager

    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mBinding: ActivityMainBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //region DEPENDENCY INJECTION
        (application as FireflyApp).mComponent.getTaskListSubcomponent(TaskListModule(this))
                .inject(this)
        //endregion

        //todo eliminar esto
        preferences.setSecretToken("secret1")

        //region DATA BINDING
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //endregion

        mBinding.showMessage = ViewModelProviders.of(this).get(TaskListViewModel::class.java).showingEmpty

        //set the mAdapter for the recycler view
        mRecyclerView = mBinding.superheroList

        // use a linear layout manager
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = adapter
        //add a divider decorator
        val dividerItemDecoration = DividerItemDecoration(mRecyclerView.context,
                DividerItemDecoration.VERTICAL)
        mRecyclerView.addItemDecoration(dividerItemDecoration)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.task_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.close_sesion -> {
                log.d(this, "cerrar sesion")
                presenter.closeSession()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //region SUPERHEROLISTVIEW INTERFACE
    override fun addLifecycleObserver(observer: TaskListLifecycleObserver) {
        if(observer is LifecycleObserver){
            lifecycle.addObserver(observer)
        }
    }

    override fun getLiveTaks(): CustomLiveData<MutableList<Task>> {
        return ViewModelProviders.of(this).get(TaskListViewModel::class.java).tasks
    }

    override fun setTasksInView(tasks: List<Task>) {
        log.d(this, "show tasks in view")
        adapter.tasks.clear()
        adapter.tasks.addAll(tasks)
        adapter.notifyDataSetChanged()
    }

    override fun hideEmptyList() {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java).showingEmpty = false
        mBinding.errorMessage.visibility = View.INVISIBLE
    }

    override fun showEmptyList(message: String) {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java).showingEmpty = true
        mBinding.errorMessage.visibility = View.VISIBLE

    }

    override fun showLoader() {
        //hide error message because I'm gonna load data
        hideEmptyList()
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        mBinding.progressBar.visibility = View.INVISIBLE
    }

    override fun showTaskDetails(task: Task) {
        /*val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra(FireflyConstants.SUPERHERO, task)
        //trainsition
        if(taskView is View) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, taskView, getString(R.string.activity_image_trans))
            startActivity(intent, options.toBundle())
        }else {
            startActivity(intent)
        }*/
    }

    @VisibleForTesting
    fun setTaskList(tasks: MutableList<Task>){
        getLiveTaks().setLivedataValue(tasks)
    }

    //endregion
}

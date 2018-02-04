package de.vierheller.todocalendar.view.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import de.vierheller.todocalendar.R
import de.vierheller.todocalendar.TodoCalendarApplication
import de.vierheller.todocalendar.model.todo.Task
import de.vierheller.todocalendar.view.TaskActivity
import de.vierheller.todocalendar.view.main.list.ListViewFragment
import de.vierheller.todocalendar.view.main.projects.ProjectsFragment
import de.vierheller.todocalendar.viewmodel.TodoViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import org.jetbrains.anko.intentFor

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DayViewFragment.OnDayViewFragmentInteractionListener, ListViewFragment.OnFragmentInteractionListener {


    lateinit var todoViewModel : TodoViewModel

    val listViewFragment: ListViewFragment = ListViewFragment()
    val dayViewFragment: DayViewFragment = DayViewFragment()
    val projectsFragment: ProjectsFragment = ProjectsFragment()
    lateinit var currentFragment:Fragment

    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("Tag", "ON CREATE MAIN ACTIVITY")
        TodoCalendarApplication.graph.inject(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        onRestoreInstanceState(savedInstanceState)
    }



    fun startTaskActivity(taskId:Long?){
        if(taskId != null)
            startActivity(intentFor<TaskActivity>(TaskActivity.INTENT_ID to taskId))
        else
            startActivity(intentFor<TaskActivity>())
    }

    private fun openFragment(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()

        when(fragment){
            is DayViewFragment -> {
                title = getString(R.string.nav_main_calendar)
            }

            is ListViewFragment -> {
                title = getString(R.string.nav_main_list)
            }

            is ProjectsFragment -> {
                title = getString(R.string.nav_main_projects)
            }
        }
        currentFragment = fragment
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        Log.d("TAG", "Save Called! ${currentFragment::class.java.canonicalName}")

        outState!!.putString(STATE_FRAGMENT, currentFragment::class.java.canonicalName)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        val fragmentName = savedInstanceState?.getString(STATE_FRAGMENT)
        when(fragmentName){
            DayViewFragment::class.java.canonicalName ->{
                openFragment(dayViewFragment)
            }

            ListViewFragment::class.java.canonicalName ->{
                openFragment(listViewFragment)
            }

            ProjectsFragment::class.java.canonicalName ->{
                openFragment(projectsFragment)
            }

            else->{
                openFragment(listViewFragment)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_calendar -> {
                openFragment(dayViewFragment)
            }
            R.id.nav_list -> {
                openFragment(listViewFragment)
            }
            R.id.nav_projects -> {
                openFragment(projectsFragment)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    public fun isFirstAppUse():Boolean{
        return sharedPreferences.getBoolean(FIRST_USE, true)
    }

    override fun onStop() {
        super.onStop()

        sharedPreferences.edit().putBoolean(FIRST_USE, false).apply()
    }

    override fun onTaskClicked(task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val STATE_FRAGMENT = "FRAGMENT_STATE"
        val FIRST_USE = "first_use"
    }
}

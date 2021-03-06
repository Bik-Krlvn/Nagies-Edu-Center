package com.cheise_proj.parent_feature

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.cheise_proj.common_module.DELAY_HANDLER
import com.cheise_proj.common_module.PICSUM_URL
import com.cheise_proj.parent_feature.base.BaseActivity
import com.cheise_proj.parent_feature.utils.ConnectionLiveData
import com.cheise_proj.presentation.viewmodel.SharedViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.content_parent_navigation.*
import timber.log.Timber

class ParentNavigationActivity : BaseActivity() {
    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, ParentNavigationActivity::class.java)
    }

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var sharedViewModel: SharedViewModel
    private val textBadgeViews = arrayListOf<TextView>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        connectionLiveData = ConnectionLiveData(this)

        val snack = Snackbar.make(root, "", Snackbar.LENGTH_INDEFINITE)
        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)

        setupNavigation()
        initNavBadge()
        setProfileData(navView)
        configureViewModel()
        openNavigationMenu()
        subscribeNetworkChange(snack)
        firebaseMessageSubscription()
//        dialogUseBackgroundChanger()

    }

    private fun subscribeNetworkChange(snack: Snackbar) {
        connectionLiveData.observe(this, Observer {
            if (!it) {
                root.background = baseContext.getDrawable(R.drawable.no_internet)
                snack.setText(getString(R.string.no_network_connection))
                snack.show()
            } else {
                root.background = getDrawable(R.drawable.background)
                snack.dismiss()
            }
        })
    }

    private fun configureViewModel() {
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        sharedViewModel.getBadgeValue().observe(this, Observer {
            setNavMenuBadge(it)
        })
    }

    private fun setNavMenuBadge(badge: Pair<Int, Int?>?) {
        val str = if (badge?.second != null && badge.second!! > 0) "${badge.second} +" else ""
        when (badge?.first) {
            R.id.messageFragment -> textBadgeViews[0].text = str
            R.id.circularFragment2 -> textBadgeViews[1].text = str
            R.id.assignmentFragment -> textBadgeViews[2].text = str
            R.id.reportFragment2 -> textBadgeViews[3].text = str
            R.id.timeTableFragment -> textBadgeViews[4].text = str
            R.id.billFragment -> textBadgeViews[5].text = str
            R.id.videoFragment -> textBadgeViews[6].text = str
        }
    }

    private fun initNavBadge() {
        val menuNav = navView.menu
        textBadgeViews.add(menuNav.findItem(R.id.messageFragment).actionView as TextView)
        textBadgeViews.add(menuNav.findItem(R.id.circularFragment2).actionView as TextView)
        textBadgeViews.add(menuNav.findItem(R.id.assignmentFragment).actionView as TextView)
        textBadgeViews.add(menuNav.findItem(R.id.reportFragment2).actionView as TextView)
        textBadgeViews.add(menuNav.findItem(R.id.timeTableFragment).actionView as TextView)
        textBadgeViews.add(menuNav.findItem(R.id.billFragment).actionView as TextView)
        textBadgeViews.add(menuNav.findItem(R.id.videoFragment).actionView as TextView)

        textBadgeViews.forEach {
            it.apply {
                gravity = Gravity.CENTER_VERTICAL
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(ContextCompat.getColor(baseContext, android.R.color.holo_red_dark))
            }
        }

    }

    private fun openNavigationMenu() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(
            {
                drawerLayout.openDrawer(GravityCompat.START)
            }, 2000
        )
    }

    private fun dialogUseBackgroundChanger() {
        if (prefs.getFirstTimeLogin()) {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle(getString(R.string.background_changer_title))
            dialogBuilder.setMessage(getString(R.string.background_changer_message))
            dialogBuilder.setPositiveButton(getString(R.string.dialog_positive_response)) { dialog, _ ->
                prefs.setBackgroundChanger(true)
                dialog.dismiss()
            }
            dialogBuilder.setNegativeButton(getString(R.string.dialog_negative_response)) { dialog, _ ->
                prefs.setBackgroundChanger(false)
                dialog.dismiss()
            }
            prefs.setFirstTimeLogin(false)
            dialogBuilder.setCancelable(false)
            dialogBuilder.show()
        }
    }

    private fun setBackgroundChanger() {
        if (prefs.getBackgroundChanger()) {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                Glide.with(baseContext).load(PICSUM_URL)
                    .diskCacheStrategy(
                        DiskCacheStrategy.NONE
                    )
                    .into(object : CustomTarget<Drawable>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            root.background = resource
                        }
                    })
            }, DELAY_HANDLER + 500)
        }
    }

    private fun setupNavigation() {
        navController = findNavController(R.id.fragment_socket)
        setupActionBarWithNavController(navController, drawerLayout)
        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
        NavigationUI.setupWithNavController(navView, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> navigation.logout(this,getParentTopic())
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    private fun firebaseMessageSubscription() {
        FirebaseMessaging.getInstance().subscribeToTopic(getParentTopic()).addOnCompleteListener {
            if (!it.isSuccessful) {
                Timber.w("Task Failed")
                return@addOnCompleteListener
            }
            Timber.i("subscribe parent topic")
        }
    }

    private fun getParentTopic(): String {
        return if (BuildConfig.DEBUG) getString(R.string.fcm_topic_dev_parent) else getString(R.string.fcm_topic_parent)

    }
}

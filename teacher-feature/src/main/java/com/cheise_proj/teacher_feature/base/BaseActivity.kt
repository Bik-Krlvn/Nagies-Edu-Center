package com.cheise_proj.teacher_feature.base

import android.os.Bundle
import android.os.PersistableBundle
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cheise_proj.presentation.GlideApp
import com.cheise_proj.presentation.utils.IPreference
import com.cheise_proj.presentation.utils.IServerPath
import com.cheise_proj.teacher_feature.R
import com.cheise_proj.teacher_feature.TeacherNavigation
import com.cheise_proj.teacher_feature.utils.ConnectionLiveData
import com.google.android.material.navigation.NavigationView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.nav_header_teacher_navigation.view.*
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var navigation: TeacherNavigation

    @Inject
    lateinit var prefs: IPreference

    @Inject
    lateinit var serverPath: IServerPath

    protected lateinit var connectionLiveData: ConnectionLiveData
    fun setProfileData(navView: NavigationView) {
        val session = prefs.getUserSession()
        with(session) {
            val username = "user: ${name?.split(" ")?.get(0)}"
            navView.getHeaderView(0).nav_header_title.text = username
            GlideApp.with(baseContext).load(serverPath.setCorrectPath(photo))
                .circleCrop()
                .placeholder(R.drawable.default_user_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(navView.getHeaderView(0).img_sidebar)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        connectionLiveData = ConnectionLiveData(this)
    }


}
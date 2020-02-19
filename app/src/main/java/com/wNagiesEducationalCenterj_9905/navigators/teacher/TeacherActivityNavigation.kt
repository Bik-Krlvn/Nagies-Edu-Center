package com.wNagiesEducationalCenterj_9905.navigators.teacher

import android.app.Activity
import com.cheise_proj.login_feature.ui.role.RoleActivity
import com.cheise_proj.presentation.model.vo.UserSession
import com.cheise_proj.presentation.utils.IPreference
import com.cheise_proj.teacher_feature.TeacherNavigation
import javax.inject.Inject

class TeacherActivityNavigation @Inject constructor(
    private val pref: IPreference
) : TeacherNavigation {
    override fun goToLocation(location: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout(activity: Activity) {
        val session = UserSession(false, null, null, null)
        session.uuid = 0
        session.photo = null
        session.token = null
        session.name = null
        pref.setUserSession(session)
        activity.startActivity(RoleActivity.getIntent(activity))
        activity.finish()
    }
}
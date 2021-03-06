package com.cheise_proj.parentapp.navigators.parent

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import com.cheise_proj.login_feature.ui.role.RoleActivity
import com.cheise_proj.parent_feature.ParentNavigation
import com.cheise_proj.presentation.model.vo.UserSession
import com.cheise_proj.presentation.utils.IPreference
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class ParentActivityNavigation @Inject constructor(
    private val pref: IPreference
) : ParentNavigation {
    override fun goToLocation(location: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout(activity: Activity, topic: String) {
        removeNotification(activity)
        val session = UserSession(false, null, null, null)
        session.uuid = 0
        session.photo = null
        session.token = null
        session.name = null
        pref.setUserSession(session)
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
        activity.startActivity(RoleActivity.getIntent(activity))
        activity.finish()
    }

    private fun removeNotification(activity: Activity) {
        val notificationManager =
            activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }


}

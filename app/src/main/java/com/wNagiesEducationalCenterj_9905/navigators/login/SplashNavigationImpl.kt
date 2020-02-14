package com.wNagiesEducationalCenterj_9905.navigators.login

import android.app.Activity
import android.os.Bundle
import com.cheise_proj.login_feature.SplashNavigation
import com.cheise_proj.login_feature.di.LoginScope
import com.cheise_proj.login_feature.ui.role.RoleActivity
import com.cheise_proj.parent_feature.ParentNavigationActivity
import com.cheise_proj.teacher_feature.TeacherNavigationActivity
import com.wNagiesEducationalCenterj_9905.R
import javax.inject.Inject

@LoginScope
class SplashNavigationImpl @Inject constructor() : SplashNavigation {
    override fun goToRolePage(activity: Activity, bundle: Bundle?) {
        activity.startActivity(
            RoleActivity.getIntent(activity)
        )
        activity.finish()
    }

    override fun skipLoginPage(activity: Activity, bundle: Bundle) {
        val role = bundle.getString("role", null)
        role?.let { r ->
            when (r) {
                activity.getString(R.string.label_parent_login) -> {
                    activity.startActivity(
                        ParentNavigationActivity
                            .getIntent(activity).putExtras(bundle)
                    )
                    activity.finish()
                }
                activity.getString(R.string.label_teacher_login) -> {
                    activity.startActivity(
                        TeacherNavigationActivity
                            .getIntent(activity).putExtras(bundle)
                    )
                    activity.finish()
                }
            }
        }
    }
}
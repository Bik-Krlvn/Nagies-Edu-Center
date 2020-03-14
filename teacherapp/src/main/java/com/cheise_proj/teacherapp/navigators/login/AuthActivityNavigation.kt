package com.cheise_proj.teacherapp.navigators.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.cheise_proj.login_feature.AuthNavigation
import com.cheise_proj.login_feature.di.LoginScope
import com.cheise_proj.teacher_feature.TeacherNavigationActivity
import javax.inject.Inject

@LoginScope
class AuthActivityNavigation @Inject constructor() : AuthNavigation {
    override fun loginToParent(activity: Activity, bundle: Bundle?) {
        AlertDialog.Builder(activity).setMessage("Not Implemented").show()
    }

    override fun loginToTeacher(activity: Activity, bundle: Bundle?) {
        val intent = TeacherNavigationActivity.getIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        activity.finish()
    }
}
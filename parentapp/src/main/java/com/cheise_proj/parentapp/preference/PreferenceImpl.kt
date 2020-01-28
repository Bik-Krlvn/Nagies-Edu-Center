package com.cheise_proj.parentapp.preference

import android.content.Context
import android.content.SharedPreferences
import com.cheise_proj.parentapp.R
import com.cheise_proj.presentation.model.vo.UserSession
import com.cheise_proj.presentation.utils.IPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceImpl @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : IPreference {
    override fun setUserSession(userSession: UserSession) {
        val preferences = sharedPreferences.edit()
        with(userSession) {
            preferences.putBoolean(context.getString(R.string.pref_isLogin_key), isLogin)
            preferences.putString(context.getString(R.string.pref_login_username_key), username)
            preferences.putString(context.getString(R.string.pref_login_user_role_key), role)
            preferences.putString(context.getString(R.string.pref_login_name_key), name)
            preferences.putString(context.getString(R.string.pref_login_level_key), level)
            preferences.putString(context.getString(R.string.pref_login_user_token_key), token)
            preferences.putString(context.getString(R.string.pref_login_avatar_key), photo)
            preferences.putInt(context.getString(R.string.pref_login_uuid_key), uuid)
        }
        preferences.apply()
    }

    override fun getUserSession(): UserSession {
        val isLogin =
            sharedPreferences.getBoolean(
                context.getString(R.string.pref_isLogin_key),
                false
            )
        val username =
            sharedPreferences.getString(
                context.getString(R.string.pref_login_username_key),
                null
            )
        val role =
            sharedPreferences.getString(
                context.getString(R.string.pref_login_user_role_key),
                null
            )
        val level =
            sharedPreferences.getString(
                context.getString(R.string.pref_login_level_key),
                null
            )

        val name =
            sharedPreferences.getString(
                context.getString(R.string.pref_login_name_key),
                null
            )
        val token =
            sharedPreferences.getString(
                context.getString(R.string.pref_login_user_token_key),
                null
            )
        val photo =
            sharedPreferences.getString(
                context.getString(R.string.pref_login_avatar_key),
                null
            )
        val uuid = sharedPreferences.getInt(
            context.getString(R.string.pref_login_uuid_key),
            0
        )

        val session = UserSession(isLogin, role, username, level)
        session.uuid = uuid
        session.photo = photo
        session.token = token
        session.name = name
        return session
    }
}
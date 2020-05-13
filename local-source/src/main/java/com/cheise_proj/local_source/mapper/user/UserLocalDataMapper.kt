package com.cheise_proj.local_source.mapper.user

import com.cheise_proj.data.model.user.UserData
import com.cheise_proj.local_source.mapper.base.LocalMapper
import com.cheise_proj.local_source.model.user.UserLocal

internal class UserLocalDataMapper  :
    LocalMapper<UserLocal, UserData> {
    override fun localToData(l: UserLocal): UserData {
        return UserData(
            uuid = l.id,
            name = l.name,
            level = l.level,
            role = l.role,
            token = l.token,
            password = l.password,
            photo = l.photo,
            username = l.username
        )
    }

    override fun dataToLocal(d: UserData): UserLocal {
        return UserLocal(
            name = d.name,
            level = d.level,
            role = d.role,
            token = d.token,
            password = d.password,
            photo = d.photo,
            username = d.username
        )
    }
}
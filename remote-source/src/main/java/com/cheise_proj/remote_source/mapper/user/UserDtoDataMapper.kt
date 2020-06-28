package com.cheise_proj.remote_source.mapper.user

import com.cheise_proj.data.model.user.UserData
import com.cheise_proj.remote_source.mapper.RemoteMapper
import com.cheise_proj.remote_source.model.dto.user.UserDto

internal class UserDtoDataMapper :
    RemoteMapper<UserDto, UserData> {
    override fun dtoToData(t: UserDto): UserData {
        return UserData(
            username = "",
            uuid = t.uuid,
            photo = t.imageUrl,
            role = t.role,
            level = t.level,
            password = "",
            name = t.name,
            token = t.token
        )
    }

    override fun dataToDto(d: UserData): UserDto {
        return UserDto(
            uuid = d.uuid,
            name = d.name,
            imageUrl = d.photo,
            level = d.level,
            status = 0,
            message = "",
            role = d.role,
            token = d.token

        )
    }

}
package com.cheise_proj.data.extensions

import com.cheise_proj.data.mapper.files.FilesDataEntityMapper
import com.cheise_proj.data.model.files.FilesData
import com.cheise_proj.domain.entity.files.FilesEntity

internal fun FilesData.asEntity() = FilesDataEntityMapper().dataToEntity(this)

internal fun FilesEntity.asData() = FilesDataEntityMapper().entityToData(this)


internal fun List<FilesData>.asEntityList(): List<FilesEntity> =
    FilesDataEntityMapper().dataToEntityList(this)

internal fun List<FilesEntity>.asDataList(): List<FilesData> = FilesDataEntityMapper().entityToDataList(this)
package com.cheise_proj.presentation.model.files

interface IFiles {
    val id: Int
    val studentName: String
    val teacherName: String
    var photo: String?
    val date: String
    var path: String?
    val refNo: String
}
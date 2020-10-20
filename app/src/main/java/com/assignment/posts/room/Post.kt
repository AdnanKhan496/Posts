package com.assignment.posts.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
class  Post : Serializable {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    var id = 0

    @ColumnInfo(name = "userId")
    var userId: String? = null

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "body")
    var body: String? = null

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean? = false
}
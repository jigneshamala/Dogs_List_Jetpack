package com.example.dog.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class DogBreed(
    //Room column
    @ColumnInfo(name="bread_id")
    //Retrofit data acess
    @SerializedName("id")
    //Storing data
    val breedId:String?,

    @ColumnInfo(name="dog_name")
    @SerializedName("name")
    val dogBreed:String?,

    @ColumnInfo(name="life_span")
    @SerializedName("life_span")
    val lifespan:String?,

    @ColumnInfo(name="bread_group")
    @SerializedName("breed_group")
    val breedGroup:String?,

    @ColumnInfo(name="bread_for")
    @SerializedName("bred_for")
    val bredFor:String?,


    @SerializedName("temperament")
    val temperament:String?,

    @ColumnInfo(name="dog_url")
    @SerializedName("url")
    val imageUrl:String?
){
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}
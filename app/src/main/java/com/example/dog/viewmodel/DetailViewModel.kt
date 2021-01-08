package com.example.dog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dog.model.DogBreed

class DetailViewModel:ViewModel() {
    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(){
        val dog = DogBreed("1","labrador","20 years","breedGrop","BredFro","temperament","")

        dogLiveData.value=dog

    }
}
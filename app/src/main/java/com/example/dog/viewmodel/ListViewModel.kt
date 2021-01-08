package com.example.dog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dog.model.DogBreed
import com.example.dog.model.DogsApiService
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {

    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()


    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {

        //random data
//        val dog1 = DogBreed("1","labrador","20 years","breedGrop","BredFro","temperament","")
//        val dog2 = DogBreed("2","Corgi","12 years","breedGrop","BredFro","temperament","")
//        val dog3 = DogBreed("3","Rotwaller","10 years","breedGrop","BredFro","temperament","")
//        val doglist= arrayListOf<DogBreed>(dog1,dog2,dog3)
//
//        dogs.value=doglist
//        dogsLoadError.value=false
//        loading.value=false

        fetchFromRemote()

    }

    private fun fetchFromRemote() {
        loading.value=true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object:DisposableSingleObserver<List<DogBreed>>(){
                    override fun onSuccess(dogsList: List<DogBreed>) {
                        dogs.value=dogsList
                        dogsLoadError.value=false
                        loading.value=false
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value=true
                        loading.value=false
                        e.printStackTrace()
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
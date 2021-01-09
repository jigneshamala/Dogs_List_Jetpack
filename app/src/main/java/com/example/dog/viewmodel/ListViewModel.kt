package com.example.dog.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dog.model.DogBreed
import com.example.dog.model.DogDao
import com.example.dog.model.DogDatabase
import com.example.dog.model.DogsApiService
import com.example.dog.util.SharedPreferencesHelper
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch


class ListViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

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
//        dogs.value = dogList
//        dogsLoadError.value = false
//        loading.value = false

        val updateTime:Long? = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    fun refreshBypassCache(){
        fetchFromRemote()
    }

    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val dogs: List<DogBreed> = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrived(dogs)
            Toast.makeText(getApplication(), "Dogs Retrieved from database", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(dogList: List<DogBreed>) {
                        storeDataLocally(dogList)
                        Toast.makeText(
                            getApplication(),
                            "Dogs Retrieved from endpoint",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun dogsRetrived(dogList: List<DogBreed>) {
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    private fun storeDataLocally(list: List<DogBreed>) {
        launch {
            val dao: DogDao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDog()
            val result: List<Long> = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrived(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
package com.example.gymify.presentaion.excersices

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymify.data.local.appDataBase.ExerciseDao
import com.example.gymify.data.local.appDataBase.ExerciseEntity
import com.example.gymify.data.online.API
import com.example.gymify.di.ExerciseRetrofit
import com.example.gymify.domain.models.ExcersiceItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class ExcersisecViewModel @Inject constructor(
    @ExerciseRetrofit private val apiService: API,
    private val exerciseDao: ExerciseDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _exerciseList = MutableLiveData<List<ExcersiceItem>>()
    val exerciseList: LiveData<List<ExcersiceItem>> get() = _exerciseList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _exerciseById = MutableLiveData<ExcersiceItem>()
    val exerciseById: LiveData<ExcersiceItem> get() = _exerciseById



    init {
        fetchExercises(limit = 300, offset = 0)

    }


    fun fetchExercises(limit: Int, offset: Int) {
        viewModelScope.launch {
            if (context.isConnected()) {
                try {
                    val response = apiService.getExercises(limit, offset)
                    if (response.isSuccessful) {
                        val exercises = response.body() ?: emptyList()
                        _exerciseList.postValue(exercises)

                        // Save to Room
                        val entityList = exercises.map { it.toEntity() }
                        exerciseDao.insertExercises(entityList)

                    } else {
                        _errorMessage.postValue("Error: ${response.code()}")
                    }
                } catch (e: Exception) {
                    _errorMessage.postValue("Failure: ${e.message}")
                }
            } else {
                val cached = exerciseDao.getAllExercises()
                cached.collect { list ->
                    _exerciseList.postValue(list.map { it.toModel() })
                }
            }
        }
    }



    fun getExerciseById(id: String) {
        viewModelScope.launch {
            if (context.isConnected()) {
                try {
                    val response = apiService.getExerciseById(id)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _exerciseById.postValue(it)
                        } ?: run {
                            _errorMessage.postValue("Empty response")
                        }
                    } else {
                        _errorMessage.postValue("Error: ${response.code()}")
                    }
                } catch (e: Exception) {
                    _errorMessage.postValue("Failure: ${e.message}")
                }
            } else {
                val localExercise = exerciseDao.getExerciseById(id)
                if (localExercise != null) {
                    _exerciseById.postValue(localExercise.toModel())
                } else {
                    _errorMessage.postValue("No internet and no local data found.")
                }
            }
        }
    }






    fun ExcersiceItem.toEntity(): ExerciseEntity = ExerciseEntity(
        id = id,
        name = name,
        gifUrl = gifUrl,
        target = target,
        secondaryMuscles = secondaryMuscles,
        instructions = instructions,
        bodyPart = bodyPart,
        equipment = equipment
    )

    fun ExerciseEntity.toModel(): ExcersiceItem = ExcersiceItem(
        id = id,
        name = name,
        gifUrl = gifUrl,
        target = target,
        secondaryMuscles = secondaryMuscles,
        instructions = instructions,
        bodyPart = bodyPart,
        equipment = equipment
    )

    fun Context.isConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

}
package com.example.gallery_mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val application: Application, val repository: DataRepository): AndroidViewModel(application = application) {

    val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private val _media : MutableStateFlow<List<Media>> = MutableStateFlow(emptyList())
    val media: StateFlow<List<Media>> = _media


    fun getImagesFromRepo() {
        scope.launch {
            withContext(Dispatchers.IO){
                val tempList =  repository.getImages(application.contentResolver)
                _media.value = tempList
            }
        }
    }




        companion object {

            val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    val application = checkNotNull(extras[APPLICATION_KEY])

                    return MainViewModel(
                        application,
                        repository = DataRepository(),
                    ) as T
                }
            }
        }

    }





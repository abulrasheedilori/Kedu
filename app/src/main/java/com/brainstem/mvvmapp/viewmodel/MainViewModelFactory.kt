package com.brainstem.mvvmapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brainstem.mvvmapp.repository.Repository

class MainViewModelFactory(
    private val repository: Repository
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }




// creating a viewModel to pass repo, bundle, and savedstate

//    class MyViewModelFactory(
//        owner: SavedStateRegistryOwner,
//        private val repository: Repository,
//        defaultArgs: Bundle? = null
//    ) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
//        override fun <T : ViewModel> create(
//            key: String, modelClass: Class<T>, handle: SavedStateHandle
//        ): T {
//            return MyViewModel(
//                repository, handle
//            ) as T
//        }
//    }
}
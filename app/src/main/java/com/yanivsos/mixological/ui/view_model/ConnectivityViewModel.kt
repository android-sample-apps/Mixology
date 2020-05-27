package com.yanivsos.mixological.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.droidnet.DroidListener
import com.droidnet.DroidNet

class ConnectivityViewModel : ViewModel() {

    private val droidListener = DroidListener { isConnected -> onConnectivityChange(isConnected) }

    private val connectivityMutableLiveData = MutableLiveData<Boolean>()
    val connectivityLiveData: LiveData<Boolean> = connectivityMutableLiveData

    init {
        addConnectivityListener()
    }

    override fun onCleared() {
        super.onCleared()
        removeConnectivityListener()
    }

    private fun addConnectivityListener() {
        DroidNet
            .getInstance()
            .addInternetConnectivityListener(droidListener)
    }

    private fun removeConnectivityListener() {
        DroidNet
            .getInstance()
            .removeInternetConnectivityChangeListener(droidListener)
    }

    private fun onConnectivityChange(isConnected: Boolean) {
        connectivityMutableLiveData.postValue(isConnected)
    }

}
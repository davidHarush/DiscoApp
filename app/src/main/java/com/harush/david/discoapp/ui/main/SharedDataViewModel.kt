package com.harush.david.discoapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * A ViewModel that holds the last visited feed title to be used in the FirstPageFragment.
 * for this to work the ViewModel in operates within the Activity scope.
 */
@HiltViewModel
class SharedDataViewModel @Inject constructor() : ViewModel() {
    private val _lastVisitedFeedTitle = MutableLiveData<String?>()
    val lastVisitedFeedTitle: LiveData<String?> = _lastVisitedFeedTitle

    fun setLastVisitedFeedTitle(title: String) {
        _lastVisitedFeedTitle.value = title
    }

    fun clearLastVisitedFeedTitle() {
        _lastVisitedFeedTitle.value = null
    }
}

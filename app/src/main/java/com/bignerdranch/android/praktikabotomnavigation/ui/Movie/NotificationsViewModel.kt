package com.bignerdranch.android.praktikabotomnavigation.ui.Movie

import androidx.lifecycle.ViewModel
import Models.Movie

class NotificationsViewModel : ViewModel() {
    var selectedMovie: Movie  = Movie(0,"",0.0, "")
    var lastSearchQuery: String? = null
}
package com.example.android.january2022.ui.rework

import androidx.lifecycle.ViewModel
import com.example.android.january2022.db.GymRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val repo: GymRepository
) : ViewModel() {



}
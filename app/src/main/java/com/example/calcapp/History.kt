package com.example.calcapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    val equation: String,
    val answer: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

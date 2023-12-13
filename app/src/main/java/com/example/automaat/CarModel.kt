package com.example.automaat

data class CarModel(
    val id: Int,
    val brand: String,
    val model: String
) {
    companion object {
        fun getAutoId(): Int {
            return (100000..999999).random()
        }
    }
}

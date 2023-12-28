package com.example.automaat.entities

import android.content.Context

class CarHelper {
    companion object {
        fun getCarImageResourceId(brand: String?, model: String?, context: Context): Int {
            val path = "${brand?.replace(" ", "_")?.lowercase()}_${model?.replace(" ", "_")?.lowercase()}"
            return context.resources.getIdentifier(path, "drawable", context.packageName)
        }
    }
}
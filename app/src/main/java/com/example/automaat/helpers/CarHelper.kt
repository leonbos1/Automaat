package com.example.automaat.helpers

import android.content.Context

class CarHelper {
    companion object {
        fun getCarImageResourceId(brand: String?, model: String?, context: Context): Int {
            val path = "${brand?.replace(" ", "_")?.replace("-","_")?.lowercase()}_${model?.replace(" ", "_")?.replace("-", "_")?.lowercase()}"
            return context.resources.getIdentifier(path, "drawable", context.packageName)
        }
    }
}
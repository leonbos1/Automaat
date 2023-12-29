package com.example.automaat.api.synchers

import com.google.gson.JsonArray

interface ISyncManager {
    fun syncEntities(jsonArray: JsonArray)
}
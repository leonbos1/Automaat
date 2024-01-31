package com.example.automaat.api.synchers

import android.content.Context

interface ISyncManager {
    /**
     * Synchronizes entities from the server to the local database
     */
    fun syncEntities(context: Context)
}
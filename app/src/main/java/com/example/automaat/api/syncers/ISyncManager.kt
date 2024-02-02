package com.example.automaat.api.syncers

import android.content.Context

interface ISyncManager {
    /**
     * Synchronizes entities from the server to the local database
     */
    fun syncEntities(context: Context)
}
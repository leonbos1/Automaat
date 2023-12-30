package com.example.automaat.api.synchers

interface ISyncManager {
    /**
     * Synchronizes entities from the server to the local database
     */
    fun syncEntities()
}
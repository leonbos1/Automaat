package com.example.automaat.api.synchers

import android.util.Log
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.api.endpoints.Customers
import com.example.automaat.api.endpoints.Rentals
import com.example.automaat.entities.CustomerModel
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.RentalState
import com.example.automaat.repositories.CustomerRepository
import com.example.automaat.repositories.RentalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerSyncManager(private val customerRepository: CustomerRepository) : ISyncManager {
    override fun syncEntities() {
        Authentication().authenticate {
            CoroutineScope(Dispatchers.IO).launch {
                val jsonArray = Customers().getAllCustomers()
                                //TODO dont make new instance of Customers() here, use the one from the constructor
                if (jsonArray != null) {
                    jsonArray.forEach { jsonElement ->
                        jsonElement.asJsonObject.let {
                            val customerModel = CustomerModel(
                                it.get("id").asInt,
                                it.get("nr").asInt,
                                it.get("firstName").asString,
                                it.get("lastName").asString,
                                it.get("from").asString,
                                "Groningen" //location is another relation -_-. can't be bothered to implement it
                            )

                            customerRepository.addCustomer(customerModel)
                        }
                    }
                }
            }
        }
    }
}

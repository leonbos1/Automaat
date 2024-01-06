package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import com.example.automaat.entities.Body
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.CustomerModel
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.entities.FuelType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerRepository(private val customerDao: CustomerDao) {
    val readAllData: LiveData<List<CustomerModel>> = customerDao.readAllData()

    suspend fun addCustomer(customer: CustomerModel) {
        customerDao.addCustomer(customer)
    }

    suspend fun deleteAllCustomers() {
        customerDao.deleteAllCustomers()
    }
}
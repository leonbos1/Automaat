package com.example.automaat.models.Car

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.automaat.R
import java.util.ArrayList

class CarsAdapter(
    private var cars: List<CarModel>,
    context: Context,
    private val dbHelper: CarDbHelper
) : RecyclerView.Adapter<CarsAdapter.CarViewHolder>()
{
    class CarViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var titleTextView: TextView = view.findViewById(R.id.titleTextView)
        var contentTextView: TextView = view.findViewById(R.id.contentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = View.inflate(parent.context, R.layout.car_item, null)
        return CarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = cars[position]
        holder.titleTextView.text = car.brand
        holder.contentTextView.text = car.model
    }

    public fun updateCarsList() {
        cars = ArrayList()
        cars = dbHelper.getAllCars()
        notifyDataSetChanged()
    }
}
package com.example.automaat.models.Car

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.automaat.R
import com.example.automaat.models.Car.CarHelper.Companion.getCarImageResourceId
import java.util.ArrayList

class CarsAdapter(
    private var cars: List<CarModel>,
    context: Context,
    private val dbHelper: CarDbHelper
) : RecyclerView.Adapter<CarsAdapter.CarViewHolder>() {

    var onItemClick: ((CarModel) -> Unit)? = null

    class CarViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var titleTextView: TextView = view.findViewById(R.id.titleTextView)
        var contentTextView: TextView = view.findViewById(R.id.contentTextView)
        var imageView: ImageView = view.findViewById(R.id.carImageView)
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

        // Pass the context to get the resource ID
        val imageResourceId = getCarImageResourceId(car.brand, car.model, holder.itemView.context)

        // Set the image resource if it exists
        if (imageResourceId != 0) {
            holder.imageView.setImageResource(imageResourceId)
        } else {
            // Set a placeholder image or handle the case where the image doesn't exist
            holder.imageView.setImageResource(R.drawable.placeholder_image)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(car)
        }
    }

    public fun updateCarsList() {
        cars = ArrayList()
        cars = dbHelper.getAllCars()
        notifyDataSetChanged()
    }
}
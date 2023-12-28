package com.example.automaat.ui.home

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.automaat.R
import com.example.automaat.entities.CarHelper.Companion.getCarImageResourceId
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.entities.toReadableString

class HomeAdapter() : RecyclerView.Adapter<HomeAdapter.CarViewHolder>() {

    var onItemClick: ((CarModel) -> Unit)? = null
    private var carList = emptyList<CarWithRental>()

    class CarViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var brandTextView: TextView = view.findViewById(R.id.brandTextView)
        var modelTextView: TextView = view.findViewById(R.id.modelTextView)
        var imageView: ImageView = view.findViewById(R.id.carImageView)
        var fuelTypeTextView: TextView = view.findViewById(R.id.fuelTypeTextView)
        var priceTextView: TextView = view.findViewById(R.id.priceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = View.inflate(parent.context, R.layout.car_item, null)
        return CarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val carWithRental = carList[position]
        holder.brandTextView.text = carWithRental.car.brand
        holder.modelTextView.text = carWithRental.car.model
        holder.fuelTypeTextView.text = carWithRental.car.fuelType.toReadableString()
        holder.priceTextView.text = carWithRental.car.price.toString()

        // Pass the context to get the resource ID
        val imageResourceId = getCarImageResourceId(carWithRental.car.brand, carWithRental.car.model, holder.itemView.context)

        // Set the image resource if it exists
        if (imageResourceId != 0) {
            holder.imageView.setImageResource(imageResourceId)
        } else {
            // Set a placeholder image or handle the case where the image doesn't exist
            holder.imageView.setImageResource(R.drawable.placeholder_image)
        }

        holder.itemView.setOnClickListener {
            println("onItemClick called")
            onItemClick?.invoke(carWithRental.car)
        }
    }

    fun setData(cars: List<CarWithRental>){
        this.carList = cars
        notifyDataSetChanged()
    }
}
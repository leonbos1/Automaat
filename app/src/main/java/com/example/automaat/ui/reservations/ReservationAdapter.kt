package com.example.automaat.ui.reservations

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.automaat.R
import com.example.automaat.api.datamodels.Rental
import com.example.automaat.helpers.CarHelper.Companion.getCarImageResourceId
import com.example.automaat.entities.relations.RentalWithCarWithCustomer

class ReservationAdapter(private val onDamageButtonClick: (RentalWithCarWithCustomer) -> Unit) :
    RecyclerView.Adapter<ReservationAdapter.CarViewHolder>() {

    private var rentalList = emptyList<RentalWithCarWithCustomer>()

    class CarViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.reservationImage)
        var titleTextView: TextView = view.findViewById(R.id.reservationTitle)
        var textTextView: TextView = view.findViewById(R.id.reservationText)
        var damageButton: Button = view.findViewById(R.id.damageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.reservation_item, parent, false)
        return CarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rentalList.size
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val rental = rentalList[position]
        val imageResourceId =
            getCarImageResourceId(rental.car?.brand, rental.car?.model, holder.itemView.context)

        if (imageResourceId != 0) {
            holder.imageView.setImageResource(imageResourceId)
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder_image)
        }

        holder.titleTextView.text = rental.car?.brand + " " + rental.car?.model
        holder.textTextView.text = rental.car?.price.toString() + "€/day"

        holder.damageButton.setOnClickListener {
            onDamageButtonClick(rental)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(rentals: List<RentalWithCarWithCustomer>) {
        this.rentalList = rentals
        notifyDataSetChanged()
    }
}
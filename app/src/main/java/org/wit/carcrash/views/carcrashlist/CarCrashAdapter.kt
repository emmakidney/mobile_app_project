package org.wit.carcrash.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.carcrash.databinding.CardCarcrashBinding
import org.wit.carcrash.models.CarCrashModel

interface CarCrashListener {
    fun onCarCrashClick(carcrash: CarCrashModel)
}

class CarCrashAdapter constructor(
    private var carcrashs: List<CarCrashModel>,
    private val listener: CarCrashListener
) :
    RecyclerView.Adapter<CarCrashAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardCarcrashBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val carcrash = carcrashs[holder.adapterPosition]
        holder.bind(carcrash, listener)
    }

    override fun getItemCount(): Int = carcrashs.size

    class MainHolder(private val binding: CardCarcrashBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(carcrash: CarCrashModel, listener: CarCrashListener) {
            binding.crashType.text = carcrash.title
            binding.description.text = carcrash.description
            Picasso.get().load(carcrash.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onCarCrashClick(carcrash) }
        }
    }
}

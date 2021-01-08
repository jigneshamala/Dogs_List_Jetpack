package com.example.dog.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.dog.R
import com.example.dog.model.DogBreed
import com.example.dog.util.getProgessDrawable
import com.example.dog.util.loadImage
import kotlinx.android.synthetic.main.item_dog.view.*

class DogListAdapter(val dogsList:ArrayList<DogBreed>):RecyclerView.Adapter<DogListAdapter.DogViewHolder>() {

    fun updateDogList(newDogsList:List<DogBreed>){

        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()

    }

    class DogViewHolder(var view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater:LayoutInflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.item_dog,parent,false)
        return DogViewHolder(view)

    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.name.text=dogsList[position].dogBreed
        holder.view.lifespan.text=dogsList[position].lifespan
        holder.view.setOnClickListener {
            Navigation.findNavController(it).navigate(ListFragmentDirections.actionDetailFragment())
        }
        holder.view.imageView.loadImage(dogsList[position].imageUrl, getProgessDrawable(holder.view.imageView.context))
    }

    override fun getItemCount()= dogsList.size

}
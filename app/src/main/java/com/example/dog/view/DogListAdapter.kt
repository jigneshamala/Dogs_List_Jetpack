package com.example.dog.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.dog.R
import com.example.dog.databinding.ItemDogBinding
import com.example.dog.model.DogBreed
import com.example.dog.util.getProgessDrawable
import com.example.dog.util.loadImage
import kotlinx.android.synthetic.main.item_dog.view.*

class DogListAdapter(val dogsList:ArrayList<DogBreed>):RecyclerView.Adapter<DogListAdapter.DogViewHolder>(),DogClickListener {

    fun updateDogList(newDogsList:List<DogBreed>){

        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater:LayoutInflater = LayoutInflater.from(parent.context)
        //val view= inflater.inflate(R.layout.item_dog,parent,false)
        val view=DataBindingUtil.inflate<ItemDogBinding>(inflater,R.layout.item_dog,parent,false)
        return DogViewHolder(view)

    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        //normal method

//        holder.view.name.text=dogsList[position].dogBreed
//        holder.view.lifespan.text=dogsList[position].lifespan
//        holder.view.setOnClickListener {
//            val action:ListFragmentDirections.ActionDetailFragment=ListFragmentDirections.actionDetailFragment()
//            action.dogUuid=dogsList[position].uuid
//            Navigation.findNavController(it).navigate(action)
//        }
//        holder.view.imageView.loadImage(dogsList[position].imageUrl, getProgessDrawable(holder.view.imageView.context))

        //databinding
        holder.view.dog=dogsList[position]
        holder.view.listener=this

    }

    override fun getItemCount()= dogsList.size

    class DogViewHolder(var view: ItemDogBinding):RecyclerView.ViewHolder(view.root)
//databinding click
    override fun onDogClicked(v: View) {
        val uuid:Int=v.dogId.text.toString().toInt()
        val action:ListFragmentDirections.ActionDetailFragment=ListFragmentDirections.actionDetailFragment()
            action.dogUuid=uuid
            Navigation.findNavController(v).navigate(action)
    }

}
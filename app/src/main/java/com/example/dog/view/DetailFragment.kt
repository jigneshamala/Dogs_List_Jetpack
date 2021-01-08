package com.example.dog.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dog.R
import com.example.dog.model.DogBreed
import com.example.dog.viewmodel.DetailViewModel
import com.example.dog.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_list.*

class DetailFragment : Fragment() {

    private var dogUuid = 0
    private lateinit var viewModel: DetailViewModel
   // private  val dogListAdapter=DogListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.fetch()
        arguments?.let{
            dogUuid=DetailFragmentArgs.fromBundle(it).dogUuid


        }

        observeViewModel()


//        buttonList.setOnClickListener{
//            val action:NavDirections=DetailFragmentDirections.actionListFragment()
//            Navigation.findNavController(it).navigate(action)
//        }
    }

    private fun observeViewModel() {
       viewModel.dogLiveData.observe(this, Observer { dog:DogBreed->
           dog?.let {
               dogName.text=dog.dogBreed
               dogPurpose.text=dog.bredFor
               dogTemperament.text=dog.temperament
               dogLifespan.text=dog.lifespan
           }

       })
    }



}
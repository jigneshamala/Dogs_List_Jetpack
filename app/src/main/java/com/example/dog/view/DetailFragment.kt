package com.example.dog.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dog.R
import com.example.dog.databinding.FragmentDetailBinding
import com.example.dog.databinding.ItemDogBinding
import com.example.dog.model.DogBreed
import com.example.dog.util.getProgessDrawable
import com.example.dog.util.loadImage
import com.example.dog.viewmodel.DetailViewModel
import com.example.dog.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_list.*

class DetailFragment : Fragment() {


    private lateinit var viewModel: DetailViewModel
    private var dogUuid = 0
    // private  val dogListAdapter=DogListAdapter(arrayListOf())

    private lateinit var dataBinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false)
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid


        }
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.fetch(dogUuid)


        observeViewModel()


//        buttonList.setOnClickListener{
//            val action:NavDirections=DetailFragmentDirections.actionListFragment()
//            Navigation.findNavController(it).navigate(action)
//        }
    }

    private fun observeViewModel() {
        viewModel.dogLiveData.observe(this, Observer { dog: DogBreed ->
            dog?.let {
//                dogName.text = dog.dogBreed
//                dogPurpose.text = dog.bredFor
//                dogTemperament.text = dog.temperament
//                dogLifespan.text = dog.lifespan
//                context?.let {
//                    dogImage.loadImage(dog.imageUrl, getProgessDrawable(it))
//                }

                //databinding

                dataBinding.dog = dog
                context?.let {
                    dogImage.loadImage(dog.imageUrl, getProgessDrawable(it))
                }

            }

        })
    }


}
package com.example.dog.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dog.R
import com.example.dog.model.DogBreed
import com.example.dog.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    private lateinit var viewModel:ListViewModel
    private  val dogListAdapter=DogListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        buttonDetail.setOnClickListener{
//            val action:ListFragmentDirections.ActionDetailFragment=ListFragmentDirections.actionDetailFragment()
//            action.dogUuid = 5
//            Navigation.findNavController(it).navigate(action)
//        }
//
        viewModel=ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        dogsList.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=dogListAdapter
        }

        refreshLayout.setOnRefreshListener {
            dogsList.visibility=View.GONE
            listError.visibility=View.GONE
            loadingView.visibility=View.VISIBLE
            viewModel.refreshBypassCache()
            refreshLayout.isRefreshing=false
        }

        observeViewModel()
    }

     fun observeViewModel() {
        viewModel.dogs.observe(this, Observer { dogs:List<DogBreed> ->
            dogs?.let{
                dogsList.visibility=View.VISIBLE
                dogListAdapter.updateDogList(dogs)
            }
        })

         viewModel.dogsLoadError.observe(this, Observer { isError:Boolean->
             isError?.let {
                 listError.visibility=if (it) View.VISIBLE else View.GONE
             }
         })

         viewModel.loading.observe(this, Observer { isLoading:Boolean->
             isLoading?.let {
                 loadingView.visibility=if (it) View.VISIBLE else View.GONE

                 if(it){
                     listError.visibility=View.GONE
                     dogsList.visibility=View.GONE
                 }
             }
         })
    }

}
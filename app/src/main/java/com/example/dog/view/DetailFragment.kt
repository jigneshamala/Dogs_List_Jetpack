package com.example.dog.view

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.dog.R
import com.example.dog.databinding.FragmentDetailBinding
import com.example.dog.databinding.ItemDogBinding
import com.example.dog.databinding.SendSmsDialogBinding
import com.example.dog.databinding.SendSmsDialogBindingImpl
import com.example.dog.model.DogBreed
import com.example.dog.model.DogPalette
import com.example.dog.model.SmsInfo
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
    private var sendSmsStarted =false
    private var currentDog:DogBreed? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
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
            currentDog = dog
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
                it.imageUrl?.let {
                    setupBackgroundColor(it)
                }

            }

        })
    }

    private fun setupBackgroundColor(url:String){
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object: CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate{palette:Palette?->
                            val intColor:Int =palette?.vibrantSwatch?.rgb?:0
                            val myPalette=DogPalette(intColor)
                            dataBinding.palette=myPalette
                        }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_send_sms->{
                sendSmsStarted=true
                (activity as MainActivity).checkSmsPermission()
            }
            R.id.action_share->{
                val intent =Intent(Intent.ACTION_SEND)
                intent.type="text/pain"
                intent.putExtra(Intent.EXTRA_SUBJECT,"Check out this Dog Breed")
                intent.putExtra(Intent.EXTRA_TEXT,"${currentDog?.dogBreed} bred for${currentDog?.bredFor}\"")
                intent.putExtra(Intent.EXTRA_STREAM,currentDog?.imageUrl)
                startActivity(Intent.createChooser(intent,"Share With"))
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun onPermissionResult(permissionGranted: Boolean){
        if(sendSmsStarted && permissionGranted){
            context?.let{
                val smsinfo=SmsInfo("","${currentDog?.dogBreed} bred for${currentDog?.bredFor}",currentDog?.imageUrl)
                val dialogBinding=DataBindingUtil.inflate<SendSmsDialogBinding>(LayoutInflater.from(it),R.layout.send_sms_dialog,null,false)

                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMs"){dialog:DialogInterface,which:Int->
                        if(!dialogBinding.smsDestination.text.isNullOrEmpty()){

                            smsinfo.to=dialogBinding.smsDestination.text.toString()
                            sendSms(smsinfo)

                        }
                    }.setNegativeButton("Cancel"){
                        dialog:DialogInterface,which:Int->{

                    }
                    }.show()
                dialogBinding.smsinfo=smsinfo
            }
        }
    }

    private fun sendSms(smsinfo: SmsInfo) {

        val intent=Intent(context,MainActivity::class.java)
        val pi:PendingIntent= PendingIntent.getActivity(context,0,intent,0)
        val sms:SmsManager = SmsManager.getDefault()
        sms.sendTextMessage(smsinfo.to,null,smsinfo.text,pi,null)
    }
}
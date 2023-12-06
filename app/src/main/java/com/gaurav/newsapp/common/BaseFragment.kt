package com.gaurav.newsapp.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<VB:ViewBinding>(val bindingInflater:(LayoutInflater) -> VB):Fragment() {

    val binding:VB by lazy {
        bindingInflater(layoutInflater)
    }

    var mContext: Context? = null


    open fun init(){
        setVariables()
    }
    open fun setVariables() {}
    open fun parseArguments() {}
    open fun resume() {}
    open fun pause() {}

    open fun setUpViews() {}

    open fun observeData() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        parseArguments()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }


    override fun onResume() {
        resume()
        super.onResume()
    }

    override fun onPause() {
        pause()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mContext = null
    }



    fun snackBarWithAction(view: View, msg: String, retry:()->Unit) {
        Snackbar.make(view, msg,Snackbar.LENGTH_INDEFINITE)
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
            .setAction("Retry"){
                retry.invoke()
            }.show()
    }

    fun toast(msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }
}
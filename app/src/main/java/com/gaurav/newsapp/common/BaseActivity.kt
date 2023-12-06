package com.gaurav.newsapp.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.viewbinding.ViewBinding
import com.gaurav.newsapp.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<VB:ViewBinding>(val bindingInflater: (LayoutInflater) -> VB):AppCompatActivity() {

    val binding:VB by lazy {
        bindingInflater(layoutInflater)
    }

    private var loadingDialog: AppCompatDialog? = null

    lateinit var mContext: Context

    open fun init(){
        setVariables()
    }

    open fun setVariables(){}

    open fun setUpViews(){}

    open fun observeData(){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(binding.root)
        init()
        setUpViews()
        observeData()
        initLoadingDialog()
    }

    fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackbar(view: View, message:String){
        Snackbar.make(view,message,Snackbar.LENGTH_SHORT)
            .show()
    }

    // Initialize Loading Dialog
    private fun initLoadingDialog() {
        loadingDialog = AppCompatDialog(mContext)
        loadingDialog!!.setCancelable(true)
        loadingDialog!!.setContentView(R.layout.dialog_loading)
        loadingDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    fun showLoading(){
        loadingDialog?.let {dialog->
            if (!dialog.isShowing){
                dialog.create()
                dialog.show()
            }
        }
    }


    fun hideLoading(){
        loadingDialog?.dismiss()
    }

    fun showDeleteAlertDialog(message:String,buttonClick: () -> Unit){

        val alertDialog = AlertDialog.Builder(mContext).apply {
            setTitle("Alert!")
            setMessage(message)
            setIcon(R.drawable.baseline_delete_24)
            setCancelable(false)

            setPositiveButton("Delete") { dialog, _ ->
               buttonClick.invoke()
                dialog.dismiss()
            }

            setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }
        }.create()

        alertDialog.show()
    }
}

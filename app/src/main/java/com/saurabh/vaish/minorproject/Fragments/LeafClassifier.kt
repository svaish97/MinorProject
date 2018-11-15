package com.saurabh.vaish.minorproject.Fragments

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saurabh.vaish.minorproject.MainActivity
import com.saurabh.vaish.minorproject.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_leaf_classify.*
import okhttp3.*
import java.io.File
import java.net.URL

class LeafClassifier: Fragment() {
    var filepath:Uri?=null
    lateinit var file:File
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        classifyImage.setOnClickListener {
            val intent=Intent()
            intent.type="image/*"
            intent.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(intent,123)
        }

        classifyLeaf.setOnClickListener {

            file=File(filepath!!.path)
            Log.d("File",filepath!!.path)
            val networkState =NetworkState()
            networkState.execute("http://192.168.43.99:5001/index")
        }
    }

    inner class NetworkState:AsyncTask<String,Void,String>(){
        override fun doInBackground(vararg params: String?): String? {
            val stringUrl=params[0]

            val url= URL(stringUrl)

            val client=OkHttpClient()
            val body =MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file",filepath.toString(),
                            RequestBody.create(MediaType.parse("image/*"),file))
                    .build()
            val request=Request.Builder().url(url)
                    .post(body).build()
            val response=client.newCall(request).execute()
            Log.d("Okhttp",response.body().toString())
            return null
        }

        override fun onPostExecute(result: String?) {
            leafClassifyResult.text=result
            super.onPostExecute(result)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data!=null){
            filepath=data.data
        }
        Picasso.get().load(filepath).resize(300,319).into(classifyImage)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_leaf_classify,null)
    }
}
package com.saurabh.vaish.minorproject.Fragments

import android.content.Intent
import android.net.Uri
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
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
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

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

            file=File(getPath(filepath!!))
            Log.d("File",filepath!!.path)
            val networkState =NetworkState()
            networkState.execute("http://192.168.43.209:2001/leaf")
        }
    }

    private fun getPath(uri: Uri): String? {
        var projection = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = context!!.contentResolver.query(uri, projection, null, null, null)

        if (cursor == null) {
            return null
        }
        var column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        var s = cursor.getString(column_index)
        cursor.close()
        return s
    }

    inner class NetworkState:AsyncTask<String,Void,String>(){
        override fun doInBackground(vararg params: String?): String? {
            val stringUrl=params[0]

            val url= URL(stringUrl)
            val httpConnection=url.openConnection() as HttpURLConnection

            val client=OkHttpClient()
            val body =MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file",filepath.toString(),
                            RequestBody.create(MediaType.parse("image/*"),file))
                    .build()
            val request=Request.Builder().url(url)
                    .post(body).build()
            val response=client.newCall(request).execute()
                val myresult= response.body()?.string()


        return myresult
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
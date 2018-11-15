package com.saurabh.vaish.minorproject.Fragments

import android.app.Activity
import android.net.Network
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.saurabh.vaish.minorproject.R
import kotlinx.android.synthetic.main.layout_msp.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MSP:android.support.v4.app.Fragment(){
    lateinit var cropValue:String
    lateinit var yearValue:String
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val cropData:Array<String> = resources.getStringArray(R.array.crops)
        val adapter=ArrayAdapter(context,android.R.layout.simple_list_item_1,cropData)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        crop.prompt="Select Crop"
        crop.adapter=adapter

        crop.onItemSelectedListener=object:AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cropValue=cropData[position]
            }

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

        }

        val yearData:Array<String> = resources.getStringArray(R.array.year)
        val adapter2=ArrayAdapter(context,android.R.layout.simple_list_item_1,yearData)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        year.adapter=adapter2


        year.onItemSelectedListener=object :AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                yearValue= yearData[position]
            }

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

        }
          super.onActivityCreated(savedInstanceState)

        btnPredict.setOnClickListener {
            val networkState =NetworkState()
            networkState.execute("http://192.168.43.117:5001/msp")
        }

    }

    inner class NetworkState:AsyncTask<String,Void,String>()
    {
        override fun doInBackground(vararg params: String?): String? {
            val stringUrl=params[0]

            val url=URL(stringUrl)
            val httpConnection=url.openConnection() as HttpURLConnection

            val client=OkHttpClient()
            val JSON=MediaType.parse("application/json; charset=utf-8")
            val actualData=JSONObject()
            actualData.put("Crop",cropValue)
            actualData.put("Year",yearValue)

            val body = RequestBody.create(JSON,actualData.toString())
            val request=Request.Builder().url(url).post(body).build()
            val response=client.newCall(request).execute()
            Log.d("Okhttp",response.body().toString())


            //for reading server response
            val inputStream=httpConnection.inputStream
            val s=Scanner(inputStream)
            s.useDelimiter("\\A")
            if(s.hasNext()){
                val str=s.next()
                return str
            }

            return null


        }

        override fun onPostExecute(result: String?){
            mspResult.text=result
            super.onPostExecute(result)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.layout_msp,null)
        return view
    }
}

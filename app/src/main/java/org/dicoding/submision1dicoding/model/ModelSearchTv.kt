package org.dicoding.submision1dicoding.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ModelSearchTv :ViewModel(){
    private val listTv = MutableLiveData<ArrayList<ModelTv>>()

    internal fun setTv(string: String){
        val client = AsyncHttpClient()
        val listItem = ArrayList<ModelTv>()
        val url = "https://api.themoviedb.org/3/search/tv?api_key=5b97be1345e3cfbb2bd2101fc9b77932&language=en-US&query=$string"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responObject = JSONObject(result)
                    val list = responObject.getJSONArray("results")

                    for (i in 0 until list.length()){
                        val tvJSON = list.getJSONObject(i)
                        val itemTV = ModelTv()

                        itemTV.id = tvJSON.getInt("id")
                        itemTV.judul = tvJSON.getString("name")
                        itemTV.Deskripsi = tvJSON.getString("overview")
                        itemTV.tanggal = tvJSON.getString("first_air_date")
                        itemTV.gambar =  tvJSON.getString("poster_path")
                        listItem.add(itemTV)
                    }
                    listTv.postValue(listItem)

                }catch (e : Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("error", "error")
            }


        })
    }

    internal fun geTV(): LiveData<ArrayList<ModelTv>> {
        return listTv
    }
}
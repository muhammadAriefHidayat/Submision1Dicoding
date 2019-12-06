package org.dicoding.submision1dicoding.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class ModelSearchFilm:ViewModel(){
    private val listMovie = MutableLiveData<ArrayList<Modelfilm>>()

    internal fun setMovie(string: String) {
        val client = AsyncHttpClient()
        val listItems = ArrayList<Modelfilm>()
        val url = "https://api.themoviedb.org/3/search/movie?api_key=5b97be1345e3cfbb2bd2101fc9b77932&language=en-US&query=$string"

        client.get(url,object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")

                    for (i in 0 until list.length()) {
                        val film = list.getJSONObject(i)
                        val filmItem = Modelfilm()

                        filmItem.id = film.getInt("id")
                        filmItem.title = film.getString("title")
                        filmItem.release_date= film.getString("release_date")
                        filmItem.poster_path= film.getString("poster_path")
                        filmItem.overview = film.getString("overview")
                        listItems.add(filmItem)
                    }
                    listMovie.postValue(listItems)

                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())

                }
            }

            override fun onFailure(
                statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("error", "error")
            }

        })
    }

    internal fun geMoview(): LiveData<ArrayList<Modelfilm>> {
        return listMovie
    }
}
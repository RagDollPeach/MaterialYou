package com.geekbrains.materialyou.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.materialyou.BuildConfig
import com.geekbrains.materialyou.model.PODServerResponseData
import com.geekbrains.materialyou.model.PictureOfTheDayData
import com.geekbrains.materialyou.model.retrofit.PODRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {

    fun getData(): LiveData<PictureOfTheDayData> {
        pictureOfTheDay()
        return liveDataForViewToObserve
    }

    fun pictureOfTheDay() {
        sendServerRequest(null)
    }

    fun pictureOfTheDayWithDate(date: LocalDate?) {
        sendServerRequest(date)
    }

    private val apiResponse = object :
        Callback<PODServerResponseData> {
        override fun onResponse(call: Call<PODServerResponseData>, response: Response<PODServerResponseData>) {
            if (response.isSuccessful && response.body() != null) {
                liveDataForViewToObserve.value =
                    PictureOfTheDayData.Success(response.body()!!)
            } else {
                val message = response.message()
                if (message.isNullOrEmpty()) {
                    liveDataForViewToObserve.value = PictureOfTheDayData.Error(Throwable("Unidentified error"))
                } else {
                    liveDataForViewToObserve.value = PictureOfTheDayData.Error(Throwable(message))
                }
            }
        }

        override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
            liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
        }
    }

    private fun sendServerRequest(date: LocalDate?) {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            if (date == null) {
                retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey).enqueue(apiResponse)
            } else {
                retrofitImpl.getRetrofitImpl().getPictureOfTheDayByDate(apiKey,date.toString()).enqueue(apiResponse)
            }
        }
    }
}

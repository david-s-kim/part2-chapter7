package com.example.part2_chapter7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)

        service.getVillageForecast(
            serviceKey = "CVIIv82mewJQHJ63RBhr2tM95Hy11o94WwpU59fSDh40untUddOuIKa1XfKtV4XNLPvGHxIB6DOSY0cYBwtYBQ==",
            baseDate = "20230505",
            baseTime = "0500",
            nx = 55,
            ny = 127
        ).enqueue(object :Callback<WeatherEntity>{
            override fun onResponse(call: Call<WeatherEntity>, response: Response<WeatherEntity>) {


                val forecastDateTimeMap = mutableMapOf<String, Forecast>()
                val forecastList = response.body()?.response?.body?.items?.forecastEntities.orEmpty()

                for(forecast in forecastList) {

                    if(forecastDateTimeMap["${forecast.forecastDate}/${forecast.forecastTime}"] == null) {
                        forecastDateTimeMap["${forecast.forecastDate}/${forecast.forecastTime}"] =
                            Forecast(forecastDate = forecast.forecastDate, forecastTime = forecast.forecastTime)
                    }


                    forecastDateTimeMap["${forecast.forecastDate}/${forecast.forecastTime}"]?.apply {

                        when(forecast.category) {
                            Category.POP -> precipitation = forecast.forecastValue.toInt()
                            Category.PTY -> precipitationType = transformRainType(forecast)
                            Category.SKY -> sky = transformSky(forecast)
                            Category.TMP -> temperature = forecast.forecastValue.toDouble()
                            else -> {}
                        }
                    }
                }
                Log.e("Forecast", forecastDateTimeMap.toString())

            }

            override fun onFailure(call: Call<WeatherEntity>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun transformRainType(forecast: ForecastEntity): String {
        return when(forecast.forecastValue.toInt()) {
            0 -> "없음"
            1 -> "비"
            2 -> "비/눈"
            3 -> "눈"
            4 -> "소나기"
            else -> ""
        }
    }

    private fun transformSky(forecast: ForecastEntity): String {
        return when(forecast.forecastValue.toInt()) {
            1 -> "맑음"
            3 -> "구름많음"
            4 -> "흐림"
            else -> ""
        }
    }
}
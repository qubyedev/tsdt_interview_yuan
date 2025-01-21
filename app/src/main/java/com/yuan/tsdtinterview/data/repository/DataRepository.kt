package com.yuan.tsdtinterview.data.repository

import android.util.Log
import com.yuan.tsdtinterview.data.model.BwibbuAll
import com.yuan.tsdtinterview.data.model.StockDayAll
import com.yuan.tsdtinterview.data.model.StockDayAvgAll
import com.yuan.tsdtinterview.network.ApiPath
import com.yuan.tsdtinterview.network.MyClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

class DataRepository {
    companion object{
        suspend fun getBwibbuAllList(): Result<List<BwibbuAll>> {
            return try {
                val url = "${ApiPath.DOMAIN}${ApiPath.BWIBBU_ALL}"

                val result: HttpResponse = MyClient.httpClient.get {
                    url(urlString = url)
                }

                val body = result.bodyAsText()
                val jsonArray = MyClient.serializeJson.parseToJsonElement(body).jsonArray

                if (result.status.value in 200..299) {
                    val stocks = jsonArray.map {
                        MyClient.serializeJson.decodeFromJsonElement<BwibbuAll>(it)
                    }
                    Result.success(stocks)
                } else {
                    Log.e("DataRepository", "❌getBwibbuAllList FAIL: $body")
                    Result.failure(Exception("getBwibbuAllList FAIL: $body"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        suspend fun getStockDayAvgAllList(): Result<List<StockDayAvgAll>> {
            return try {
                val url = "${ApiPath.DOMAIN}${ApiPath.STOCK_DAY_AVG_ALL}"

                val result: HttpResponse = MyClient.httpClient.get {
                    url(urlString = url)
                }

                val body = result.bodyAsText()
                val jsonArray = MyClient.serializeJson.parseToJsonElement(body).jsonArray

                if (result.status.value in 200..299) {
                    val stocks = jsonArray.map {
                        MyClient.serializeJson.decodeFromJsonElement<StockDayAvgAll>(it)
                    }
                    Result.success(stocks)
                } else {
                    Log.e("DataRepository", "❌getStockDayAvgAllList FAIL: $body")
                    Result.failure(Exception("getStockDayAvgAllList FAIL: $body"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        suspend fun getStockDayAllList(): Result<List<StockDayAll>> {
            return try {
                val url = "${ApiPath.DOMAIN}${ApiPath.STOCK_DAY_ALL}"

                val result: HttpResponse = MyClient.httpClient.get {
                    url(urlString = url)
                }

                val body = result.bodyAsText()
                val jsonArray = MyClient.serializeJson.parseToJsonElement(body).jsonArray

                if (result.status.value in 200..299) {
                    val stocks = jsonArray.map {
                        MyClient.serializeJson.decodeFromJsonElement<StockDayAll>(it)
                    }
                    Result.success(stocks)
                } else {
                    Log.e("DataRepository", "❌getStockDayAllList FAIL: $body")
                    Result.failure(Exception("getStockDayAllList FAIL: $body"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
package com.yuan.tsdtinterview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockDayAvgAll (
    @SerialName("Code") val code: String?,
    @SerialName("Name") val name: String?,
    @SerialName("ClosingPrice") val closingPrice: String?,
    @SerialName("MonthlyAveragePrice") val monthlyAveragePrice: String?
)
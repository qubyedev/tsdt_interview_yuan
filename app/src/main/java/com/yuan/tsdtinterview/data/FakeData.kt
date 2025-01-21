package com.yuan.tsdtinterview.data

import com.yuan.tsdtinterview.data.model.BwibbuAll
import com.yuan.tsdtinterview.data.model.StockDayAll
import com.yuan.tsdtinterview.data.model.StockDayAvgAll

object FakeData {
    val fakeBwibbuAllList = listOf(
        BwibbuAll(
            code = "1102",
            name = "亞泥",
            peRatio = "15.32",
            dividendYield = "4.20",
            pbRatio = "1.12"
        ),
        BwibbuAll(
            code = "1103",
            name = "中鋼",
            peRatio = "18.45",
            dividendYield = "3.80",
            pbRatio = "1.05"
        )
    )

    val fakeStockDayAvgAllList = listOf(
        StockDayAvgAll(
            code = "0050",
            name = "元大台灣50",
            closingPrice = "198.30",
            monthlyAveragePrice = "197.90"
        ),
        StockDayAvgAll(
            code = "0051",
            name = "元大中型100",
            closingPrice = "76.45",
            monthlyAveragePrice = "76.20"
        )
    )

    val fakeStockDayAllList = listOf(
        StockDayAll(
            code = "0050",
            name = "元大台灣50",
            tradeVolume = "7861450",
            tradeValue = "1558092202",
            openingPrice = "197.15",
            highestPrice = "198.65",
            lowestPrice = "197.15",
            closingPrice = "198.30",
            change = "2.0000",
            transaction = "13583"
        ),
        StockDayAll(
            code = "0051",
            name = "元大中型100",
            tradeVolume = "49029",
            tradeValue = "3748275",
            openingPrice = "76.15",
            highestPrice = "76.45",
            lowestPrice = "76.05",
            closingPrice = "76.45",
            change = "0.7500",
            transaction = "322"
        )
    )
}
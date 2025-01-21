package com.yuan.tsdtinterview.ui.main


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuan.tsdtinterview.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yuan.tsdtinterview.data.FakeData
import com.yuan.tsdtinterview.data.model.BwibbuAll
import com.yuan.tsdtinterview.data.model.StockDayAll
import com.yuan.tsdtinterview.data.model.StockDayAvgAll
import com.yuan.tsdtinterview.ui.components.LoadingIndicator
import com.yuan.tsdtinterview.ui.theme.MyTextStyles
import com.yuan.tsdtinterview.ui.theme.TsdtinterviewyuanTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val viewModel: MainViewModel = viewModel()

    val isLoading by viewModel.isLoading
    var showBottomSheet by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    var selectedBwibbu by remember{ mutableStateOf<BwibbuAll?>(null) }
    val scope = rememberCoroutineScope()
    val bwibbuAllList = viewModel.bwibbuAllList
    val stockDayAllList = viewModel.stockDayAllList
    val stockDayAvgAllList = viewModel.stockDayAvgAllList
    val apiError by viewModel.apiError

    LaunchedEffect(Unit) {
        viewModel.getAllData()
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        IconButton(
            onClick = {
                showBottomSheet = !showBottomSheet
            },
            modifier = modifier
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "menu"
            )
        }

        LazyColumn {
            items(stockDayAllList.zip(stockDayAvgAllList)) { (stock, avgStock) ->
                StockCardView(
                    stock = stock,
                    stockDayAvgAll = avgStock,
                    onClick = { code ->
                        val foundBwibbu = bwibbuAllList.find { it.code == code}
                        foundBwibbu?.let {
                            selectedBwibbu = foundBwibbu
                            showAlert = true
                        }
                    })
            }
        }
    }

    LoadingIndicator(isLoading)

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.sortStockListsAscending()
                            showBottomSheet = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(stringResource(R.string.sort_by_stock_id_asc))
                }

                Button(
                    onClick = {
                        scope.launch {
                            viewModel.sortStockListsDescending()
                            showBottomSheet = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(stringResource(R.string.sort_by_stock_id_desc))
                }
            }
        }
    }

    if (showAlert) {
        AlertDialog(
            onDismissRequest = {
                selectedBwibbu = null
                showAlert = false
                               },
            confirmButton = {
                Button(
                    onClick = {
                        selectedBwibbu = null
                        showAlert = false
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
                            },
            text = {
                Column() {
                    Text(selectedBwibbu?.name ?: "")
                    DataRow(title = stringResource(R.string.price_to_earnings_ratio), content = selectedBwibbu?.peRatio ?: "", contentColor = Color.White)
                    DataRow(title = stringResource(R.string.dividend_yield), content = selectedBwibbu?.dividendYield ?: "", contentColor = Color.White)
                    DataRow(title = stringResource(R.string.price_to_book_ratio), content = selectedBwibbu?.pbRatio ?: "", contentColor = Color.White)
                }
            }
        )
    }
}

@Composable
private fun StockCardView(
    modifier: Modifier = Modifier,
    stock: StockDayAll,
    stockDayAvgAll: StockDayAvgAll,
    onClick: (String) -> Unit
){
    Card(
        modifier = modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .clickable { onClick(stock.code ?: "") },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            stock.code?.let { Text(text = it, style = MyTextStyles.w400s15) }
            stock.name?.let { Text(text = it, style = MyTextStyles.w700s18) }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DataRow(
                    title = stringResource(R.string.open_price),
                    content = stock.openingPrice ?: "",
                    contentColor = comparePriceAndGetColor(
                        price = stock.openingPrice ?: "",
                        monthAvg = stockDayAvgAll.monthlyAveragePrice ?: ""
                    )
                )
                DataRow(
                    title = stringResource(R.string.close_price),
                    content = stock.closingPrice ?: "",
                    contentColor = comparePriceAndGetColor(
                        price = stock.closingPrice ?: "",
                        monthAvg = stockDayAvgAll.monthlyAveragePrice ?: ""
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DataRow(title = stringResource(R.string.highest_price), content = stock.highestPrice ?: "")
                DataRow(title = stringResource(R.string.lowest_price), content = stock.lowestPrice ?: "")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DataRow(title = stringResource(R.string.price_difference), content = stock.change ?: "", contentColor = getColorByPositiveNegative(stock.change ?: ""))
                DataRow(title = stringResource(R.string.monthly_average_price), content = stockDayAvgAll.monthlyAveragePrice ?: "")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DataRowSmall(title = stringResource(R.string.transaction_count), content = stock.transaction ?: "")
                DataRowSmall(title = stringResource(R.string.transaction_shares), content = stock.tradeVolume ?: "")
                DataRowSmall(title = stringResource(R.string.transaction_amount), content = stock.tradeValue ?: "")
            }
        }
    }
}

@Composable
private fun DataRow(
    title: String,
    content: String,
    contentColor: Color = Color.Black
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MyTextStyles.w400s15)
        Text(text = content, style = MyTextStyles.w400s24, color = contentColor)
    }
}

@Composable
private fun DataRowSmall(
    title: String,
    content: String
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MyTextStyles.w400s12)
        Text(text = content, style = MyTextStyles.w400s12)
    }
}

private fun comparePriceAndGetColor(
    price: String,
    monthAvg: String
): Color {
    return try {
        val priceNum = price.replace("[^0-9.-]".toRegex(), "").toDouble()
        val monthAvgNum = monthAvg.replace("[^0-9.-]".toRegex(), "").toDouble()

        when {
            priceNum > monthAvgNum -> Color.Red
            priceNum < monthAvgNum -> Color.Green
            else -> Color.Black
        }
    } catch (e: Exception) {
        Color.Black
    }
}

private fun getColorByPositiveNegative(
    value: String
): Color {
    return try {
        val number = value.replace("[^0-9.-]".toRegex(), "").toDouble()
        when {
            number > 0 -> Color.Red
            number < 0 -> Color.Green
            else -> Color.Black //0
        }
    } catch (e: Exception) {
        Color.Black
    }
}

@Preview(showBackground = true)
@Composable
private fun StockCardViewPreview() {

    val d = FakeData.fakeStockDayAllList.first()
    val dAvg = FakeData.fakeStockDayAvgAllList.first()

    TsdtinterviewyuanTheme {
        Surface(modifier = Modifier) {
            StockCardView(stock = d, stockDayAvgAll = dAvg, onClick = {})
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_7_PRO)
@Composable
private fun MainScreenPreview() {
    TsdtinterviewyuanTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            MainScreen()
        }
    }
}
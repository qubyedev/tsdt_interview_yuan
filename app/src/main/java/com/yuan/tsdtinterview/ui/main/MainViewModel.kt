package com.yuan.tsdtinterview.ui.main

import android.provider.ContactsContract.Data
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuan.tsdtinterview.data.model.BwibbuAll
import com.yuan.tsdtinterview.data.model.StockDayAll
import com.yuan.tsdtinterview.data.model.StockDayAvgAll
import com.yuan.tsdtinterview.data.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _bwibbuAllList = mutableStateListOf<BwibbuAll>()
    val bwibbuAllList: List<BwibbuAll> = _bwibbuAllList

    private val _stockDayAvgAllList = mutableStateListOf<StockDayAvgAll>()
    val stockDayAvgAllList: List<StockDayAvgAll> = _stockDayAvgAllList

    private val _stockDayAllList = mutableStateListOf<StockDayAll>()
    val stockDayAllList: List<StockDayAll> = _stockDayAllList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _apiError = mutableStateOf<String?>(null)
    val apiError: State<String?> = _apiError

    fun getAllData() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                fetchBwibbuAll()
                fetchStockDayAvgAll()
                fetchStockDayAll()
            } catch (e: Exception) {
                _apiError.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchBwibbuAll() = DataRepository.getBwibbuAllList()
        .onSuccess {
            _bwibbuAllList.clear()
            _bwibbuAllList.addAll(it)
        }
        .onFailure {
            _apiError.value = it.message
    }

    private suspend fun fetchStockDayAvgAll() = DataRepository.getStockDayAvgAllList()
        .onSuccess {
            _stockDayAvgAllList.clear()
            _stockDayAvgAllList.addAll(it)
        }
        .onFailure {
            _apiError.value = it.message
        }

    private suspend fun fetchStockDayAll() = DataRepository.getStockDayAllList()
        .onSuccess {
            _stockDayAllList.clear()
            _stockDayAllList.addAll(it)
        }
        .onFailure {
            _apiError.value = it.message
        }

    fun sortStockListsAscending() {
        val sortedStockList = _stockDayAllList.zip(_stockDayAvgAllList)
            .sortedBy { it.first.code }
            .unzip()

        _stockDayAllList.clear()
        _stockDayAvgAllList.clear()
        _stockDayAllList.addAll(sortedStockList.first)
        _stockDayAvgAllList.addAll(sortedStockList.second)
    }

    fun sortStockListsDescending() {
        val sortedStockList = _stockDayAllList.zip(_stockDayAvgAllList)
            .sortedByDescending { it.first.code }
            .unzip()

        _stockDayAllList.clear()
        _stockDayAvgAllList.clear()
        _stockDayAllList.addAll(sortedStockList.first)
        _stockDayAvgAllList.addAll(sortedStockList.second)
    }
}
package com.a.digistask.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.a.digistask.R
import com.a.digistask.models.DataModel
import com.a.digistask.models.Label.*
import com.a.digistask.network.Api
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 *
 *Created by Atef on 24/12/20
 *
 */
class MainViewModel @ViewModelInject constructor(
    private val api: Api,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "MainViewModel";

    private var startTime: Long = System.currentTimeMillis()

    private val _newRSRP = MutableLiveData<Entry>(null)
    private val _currentRSRPProgressColor: MutableLiveData<Int> = MutableLiveData(R.color.black)

    private val _newRSRQ = MutableLiveData<Entry>(null)
    private val _currentRSRQProgressColor: MutableLiveData<Int> = MutableLiveData(R.color.black)

    private val _newSINR = MutableLiveData<Entry>(null)
    private val _currentSINRProgressColor: MutableLiveData<Int> = MutableLiveData(R.color.black)

    fun startLoadingData() {
        startTime = System.currentTimeMillis()
        viewModelScope.launch {
            while (isActive) {
                val data = fetchData()
                if (data != null)
                    handleData(data)
                else {
                    //TODO Do nothing for now
                }
                delay(2000)
            }
        }
    }

    private suspend fun fetchData(): DataModel? {
        try {
            return api.getData()
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO Do nothing for now
        }
        return null
    }

    private fun handleData(dataModel: DataModel) {

        val second: Float = getEntryTimeInSeconds().toFloat()
        _newRSRP.value = Entry(second, dataModel.rsrp.toFloat())
        _newRSRQ.value = Entry(second, dataModel.rsrq.toFloat())
        _newSINR.value = Entry(second, dataModel.sinr.toFloat())

        _currentRSRPProgressColor.value = getRSRPColor(dataModel.rsrp)
        _currentRSRQProgressColor.value = getRSRQColor(dataModel.rsrq)
        _currentSINRProgressColor.value = getSINRColor(dataModel.sinr)
    }

    private fun getEntryTimeInSeconds(): Long = (System.currentTimeMillis() - startTime) / 1000

    fun createRSRPDataSet() = createDataSet(RSRP.toString(), android.graphics.Color.RED)
    fun createRSRQDataSet() = createDataSet(RSRQ.toString(), android.graphics.Color.GREEN)
    fun createSINRDataSet() = createDataSet(SINR.toString(), android.graphics.Color.BLUE)

    private fun createDataSet(label: String, lineColor: Int): LineDataSet =
        LineDataSet(ArrayList<Entry>(), label).apply {
            color = lineColor
            setDrawValues(false)
            setDrawCircleHole(false)
            setCircleColor(lineColor)
            setDrawCircles(true)
            mode = LineDataSet.Mode.LINEAR
        }

    private fun getRSRPColor(rsrp: Double): Int {
        return when {
            rsrp < -110 -> R.color.rsrp_less_minus_110
            rsrp < -100 -> R.color.rsrp_less_minus_100
            rsrp < -90 -> R.color.rsrp_less_minus_90
            rsrp < -80 -> R.color.rsrp_less_minus_80
            rsrp < -70 -> R.color.rsrp_less_minus_70
            rsrp < -60 -> R.color.rsrp_less_minus_60
            else -> R.color.rsrp_more_minus_60
        }
    }

    private fun getRSRQColor(rsrq: Double): Int {
        return when {
            rsrq < -19.5 -> R.color.rsrq_less_minus_19_5
            rsrq < -14 -> R.color.rsrq_less_minus_14
            rsrq < -9 -> R.color.rsrq_less_minus_9
            rsrq < -3 -> R.color.rsrq_less_minus_3
            else -> R.color.rsrq_more_minus_3
        }
    }

    private fun getSINRColor(sinr: Double): Int {
        return when {
            sinr < 0 -> R.color.sinr_less_0
            sinr < 5 -> R.color.sinr_less_5
            sinr < 10 -> R.color.sinr_less_10
            sinr < 15 -> R.color.sinr_less_15
            sinr < 20 -> R.color.sinr_less_20
            sinr < 25 -> R.color.sinr_less_25
            sinr < 30 -> R.color.sinr_less_30
            else -> R.color.sinr_more_30
        }
    }

    fun rsrp(): LiveData<Entry> = _newRSRP
    fun rsrq(): LiveData<Entry> = _newRSRQ
    fun sinr(): LiveData<Entry> = _newSINR

    fun currentRSRPProgressColor(): LiveData<Int> = _currentRSRPProgressColor
    fun currentRSRQProgressColor(): LiveData<Int> = _currentRSRQProgressColor
    fun currentSINRProgressColor(): LiveData<Int> = _currentSINRProgressColor
}


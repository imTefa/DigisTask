package com.a.digistask.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.a.digistask.R
import com.a.digistask.databinding.ActivityMainBinding
import com.a.digistask.models.Label.*
import com.a.digistask.network.Api
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    @Inject
    lateinit var api: Api
    private val viewModel: MainViewModel by viewModels()

    lateinit var binding: ActivityMainBinding
    private val lineData: LineData = LineData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initiateChart()
        observeData()
        viewModel.startLoadingData()
    }

    private fun initiateChart() {
        //Customize left Y axis
        binding.chart.axisLeft.apply {
            isEnabled = true
            setDrawAxisLine(true)
            gridColor = R.color.black
            axisLineColor = R.color.black
            setDrawLabels(true)
        }
        //Hide right y axis
        binding.chart.axisRight.isEnabled = false

        //Customize x axis
        binding.chart.xAxis.apply {
            isEnabled = true
            setDrawAxisLine(true)
            gridColor = Color.TRANSPARENT
            axisLineColor = R.color.black
            setDrawLabels(true)
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.BLACK
        }

        binding.chart.description.isEnabled = false
        binding.chart.legend.isEnabled = false
        binding.chart.setTouchEnabled(false)
        binding.chart.isScaleXEnabled = false
        binding.chart.data = lineData

        //Add data sets to line data
        lineData.apply {
            addDataSet(viewModel.createRSRPDataSet())
            addDataSet(viewModel.createRSRQDataSet())
            addDataSet(viewModel.createSINRDataSet())
        }

        notifyChart()
    }

    private fun observeData() {
        viewModel.rsrp().observe(this) { entry ->
            if (entry != null) {
                lineData.getDataSetByLabel(RSRP.toString(), false).addEntry(entry)
                notifyChart()
            }
        }

        viewModel.rsrq().observe(this) { entry ->
            if (entry != null) {
                lineData.getDataSetByLabel(RSRQ.toString(), false).addEntry(entry)
                notifyChart()
            }
        }

        viewModel.sinr().observe(this) { entry ->
            if (entry != null) {
                lineData.getDataSetByLabel(SINR.toString(), false).addEntry(entry)
                notifyChart()
            }
        }
    }

    private fun notifyChart() {
        lineData.notifyDataChanged()
        binding.chart.notifyDataSetChanged()
        binding.chart.invalidate()
    }

}

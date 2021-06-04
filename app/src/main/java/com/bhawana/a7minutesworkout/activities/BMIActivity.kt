package com.bhawana.a7minutesworkout.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bhawana.a7minutesworkout.R
import kotlinx.android.synthetic.main.activity_b_m_i.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val US_UNITS_VIEW = "US_UNIT_VIEW"

    var currentVisibleView : String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)

        setSupportActionBar(toolbar_bmi_activity)
        val actionbar = supportActionBar
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title = "CALCULATE BMI"
        }

        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        btnCalculateUnits.setOnClickListener {
            if (currentVisibleView == METRIC_UNITS_VIEW) {
                if (validateMetricValues()) {
                    val heightValue: Float = etHeight.text.toString().toFloat() / 100
                    val weightValue: Float = etWeight.text.toString().toFloat()

                    val bmi = weightValue / (heightValue * heightValue)
                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(
                        this@BMIActivity,
                        "Please enter valid values",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                if(validateUSValues()){
                    val weightValue : Float = etUSWeight.text.toString().toFloat()
                    val heightFeet : String = etHeightFeet.text.toString()
                    val heightInch : String = etHeightInch.text.toString()

                    val heightValue = heightInch.toFloat() + (heightFeet.toFloat() *12)

                    val bmi = 703 * (weightValue / (heightValue * heightValue))
                    displayBMIResult(bmi)
                }else{
                    Toast.makeText(this@BMIActivity, "Please enter valid values",Toast.LENGTH_SHORT).show()
                }
            }
        }

        makeVisibleMetricUnitsView()
        rgUnits.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUSUnitsView()
            }
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        tilMetricUnitHeight.visibility = View.VISIBLE
        tilMetricUnitWeight.visibility = View.VISIBLE

        etHeight.text!!.clear()
        etWeight.text!!.clear()

        tilUSUnitWeight.visibility = View.GONE
        llUSUnitsHeight.visibility = View.GONE
        llDisplayBMIResult.visibility = View.GONE
    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView= US_UNITS_VIEW

        tilUSUnitWeight.visibility = View.VISIBLE
        llUSUnitsHeight.visibility = View.VISIBLE

        etUSWeight.text!!.clear()
        etHeightFeet.text!!.clear()
        etHeightInch.text!!.clear()

        tilMetricUnitWeight.visibility = View.GONE
        tilMetricUnitHeight.visibility = View.GONE
        llDisplayBMIResult.visibility = View.GONE


    }

    private fun displayBMIResult(bmi : Float){
        val bmiLabel : String
        val bmiDescription : String

        if(bmi.compareTo(15f) <= 0){
            bmiLabel = "Very Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more"
        }else if(bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more"
        }else if(bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <=0){
            bmiLabel = "Underweight"
            bmiDescription ="Oops!! You need to take care of Yourself! Eat more"
        }else if(bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <=0){
            bmiLabel = "Normal"
            bmiDescription ="Congratulations! You are in good shape!"
        }else if(bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You need to take care of yourself! Workout maybe"
        }else if(bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Obese Class | Moderately Obese"
            bmiDescription = "Oops! You really need to take care of yourself! Workout maybe"
        }else if(bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Obese Class | Severely Obese"
            bmiDescription = "OMG! You are in a dangerous condition! Act now!"
        }else{
            bmiLabel = "Obese Class ! Very Severely obese"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        llDisplayBMIResult.visibility = View.VISIBLE

        /*tvYourBMI.visibility = View.VISIBLE
        tvBMIValue.visibility = View.VISIBLE
        tvBMIType.visibility = View.VISIBLE
        tvBMIDescription.visibility = View.VISIBLE*/

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()
        tvBMIValue.text = bmiValue
        tvBMIType.text = bmiLabel
        tvBMIDescription.text = bmiDescription
    }

    private fun validateUSValues() : Boolean{
        var isValid = true

        when {
            etUSWeight.text.toString().isEmpty() -> isValid = false
            etHeightFeet.text.toString().isEmpty() -> isValid = false
            etHeightInch.text.toString().isEmpty() -> isValid = false
        }
        return isValid
    }

    private fun validateMetricValues() : Boolean{
        var isValid = true

        if(etWeight.text.toString().isEmpty())
            isValid = false
        else if(etHeight.text.toString().isEmpty())
            isValid = false

        return isValid
    }
}
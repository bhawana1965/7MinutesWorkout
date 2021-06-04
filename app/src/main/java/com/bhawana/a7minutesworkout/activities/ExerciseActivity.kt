package com.bhawana.a7minutesworkout.activities

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhawana.a7minutesworkout.*
import com.bhawana.a7minutesworkout.adapters.ExerciseStatusAdapter
import com.bhawana.a7minutesworkout.database.Constants
import com.bhawana.a7minutesworkout.models.ExerciseModel
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.diialog_custom_back.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var restTimer : CountDownTimer? = null
    private var restProgress =0
    private var restTimerDuration : Long = 10

    private var exerciseTimer : CountDownTimer? = null
    private var exerciseProgress =0
    private var exerciseTimerDuration: Long =30

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts : TextToSpeech? = null
    private var player : MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        val actionbar = supportActionBar

        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_exercise_activity.setNavigationOnClickListener{
            customDialogForBackButton()
        }

        tts = TextToSpeech(this,this)

        exerciseList= Constants.defaultExerciseList()

        setUpRestView()

        setupExerciseStatusRecyclerView()
    }

    override fun onDestroy() {

        if(restTimer != null){
            restTimer!!.cancel()
            restProgress =0
        }
        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player != null){
            player!!.stop()
        }
        super.onDestroy()
    }
    private fun setRestProgressBar(){
        progressBar.progress = restProgress
        restTimer = object : CountDownTimer(restTimerDuration*1000,1000){

            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = 10-restProgress
                tvTimer.text = (10-restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setUpExerciseView()

            }
        }.start()
    }

    private fun setExerciseProgressBar(){
        progressBarExercise.progress = exerciseProgress
        exerciseTimer = object :  CountDownTimer(exerciseTimerDuration*1000,1000) {

            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                progressBarExercise.progress = 30 - exerciseProgress
                tvTimerExercise.text = (30-exerciseProgress).toString()
            }

            override fun onFinish() {

                if(currentExercisePosition < exerciseList?.size!! - 1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setUpRestView()

                }else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }

        }.start()
    }

    private fun setUpRestView(){

        //val soundURI = Uri.parse("android:resource://com.bhawana.a7minutesworkout/" + R.raw.press_start)
        //player = MediaPlayer.create(applicationContext,soundURI)
        try{
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player!!.isLooping = false
            player!!.start()

        }catch (e : Exception){
            e.printStackTrace()
        }
        llRestView.visibility = View.VISIBLE
        llExerciseView.visibility = View.GONE

        if(restTimer != null){
            restTimer!!.cancel()
            restProgress=0
        }

        setRestProgressBar()

        tvNextExercise.text= exerciseList!![currentExercisePosition + 1].getName()

    }


    private fun setUpExerciseView(){
        llRestView.visibility = View.GONE
        llExerciseView.visibility = View.VISIBLE
        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress=0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        setExerciseProgressBar()

        iv_image.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()

    }

    override fun onInit(status: Int) {

        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The language specified is not supported")
            }
        }else{
            Log.e("TTS","Iniatialization Failed")
        }
    }


    fun speakOut(text : String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    private fun setupExerciseStatusRecyclerView(){
        rvExerciseStatus.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        exerciseAdapter =  ExerciseStatusAdapter(exerciseList!!,this)
        rvExerciseStatus.adapter = exerciseAdapter
    }

    private fun customDialogForBackButton(){
        val customDialog =Dialog(this)

        customDialog.setContentView(R.layout.diialog_custom_back)
        customDialog.tvYes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
}

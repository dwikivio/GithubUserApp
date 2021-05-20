package com.stikubank.githubuserapp.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stikubank.githubuserapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var bind: ActivitySettingsBinding
    private lateinit var remind: Alarm

    companion object{
        private const val SET_EXTRA = "DailyAlarm"
        private const val SET_CLOCK = "09:00:00" //set time here with format (hour:minute:second)
        private const val SET_MESSAGE = "AppsReminder"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val alarmPreference = AlarmPreference(this)
        val manageAlarm = AlarmManagers()

        if (alarmPreference.getAlarm().Remind){
            bind.swAlarm.isChecked = true
        }else{
            bind.swAlarm.isChecked = false
        }

        bind.swAlarm.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                saveAlarm(true)
                manageAlarm.setAlarm(this, SET_EXTRA, SET_CLOCK, SET_MESSAGE)
            }else{
                saveAlarm(false)
                manageAlarm.shutAlarm(this)
            }
        }

        supportActionBar?.hide()
    }

    private fun saveAlarm(state: Boolean) {
        val almPreference = AlarmPreference(this)
        remind = Alarm()

        remind.Remind = state
        almPreference.setAlarm(remind)
    }
}
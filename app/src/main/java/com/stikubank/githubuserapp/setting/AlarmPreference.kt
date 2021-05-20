package com.stikubank.githubuserapp.setting

import android.content.Context

class AlarmPreference(context: Context) {

    companion object {
        const val PREFS_NAME = "AlarmPrefs"
        private const val ALARMS = "alarms"
    }

    val pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setAlarm(value: Alarm){
        val editor = pref.edit()
        editor.putBoolean(ALARMS, value.Remind)

    }

    fun getAlarm(): Alarm{
        var data = Alarm()
        data.Remind = pref.getBoolean(ALARMS, false)
        return data
    }

}
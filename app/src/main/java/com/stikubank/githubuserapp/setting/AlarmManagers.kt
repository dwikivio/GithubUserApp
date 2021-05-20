package com.stikubank.githubuserapp.setting

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.stikubank.githubuserapp.R
import com.stikubank.githubuserapp.ui.main.MainActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmManagers : BroadcastReceiver() {

    companion object {
        private const val CH_ID = "CH01"
        private const val CT_TITLE = "GithubApp"
        private const val CT_TEXT ="Let's Add More Users to Favorite!"
        private const val CH_NAME = "REMINDER"
        private const val ID_DAILY = 1
        private const val TM_FORMAT = "HH:mm:ss"
        private const val ID_ALARM = 100
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_TYPE = "extra_type"
    }

    override fun onReceive(context: Context, intent: Intent) {
        pushNotif(context)
    }
    fun pushNotif(context: Context){

        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent: PendingIntent? = PendingIntent.getActivity(context, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notifSetting = NotificationCompat.Builder(context, CH_ID)
            .setSmallIcon(R.drawable.ic_notif)
            .setContentIntent(resultPendingIntent)
            .setContentTitle(CT_TITLE)
            .setContentText(CT_TEXT)
            .setAutoCancel(true)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CH_ID, CH_NAME, NotificationManager.IMPORTANCE_DEFAULT)

            notifSetting.setChannelId(CH_ID)
            notifManager.createNotificationChannel(channel)
        }

        val notif = notifSetting.build()
        notifManager.notify(ID_DAILY, notif)
    }

    fun setAlarm(
        context: Context,
        type: String,
        clock: String,
        message: String
    ){
        if(valiDate(clock, TM_FORMAT)) return
        val manageAlarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmManagers::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val clockTime = clock.split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

        val calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clockTime[0]))
        calender.set(Calendar.MINUTE, Integer.parseInt(clockTime[1]))
        calender.set(Calendar.SECOND, Integer.parseInt(clockTime[2]))

        val pdgIntent = PendingIntent.getBroadcast(context, ID_ALARM, intent, 0)

        manageAlarm.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calender.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pdgIntent)

        Toast.makeText(context, "Alarm Ready!", Toast.LENGTH_SHORT).show()
    }


    fun shutAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmManagers::class.java)

        val reqCode = ID_ALARM
        val pdgIntent = PendingIntent.getBroadcast(context, reqCode, intent, 0)
        pdgIntent.cancel()
        alarmManager.cancel(pdgIntent)

        Toast.makeText(context, "Alarm Canceled", Toast.LENGTH_SHORT).show()
    }

    private fun valiDate(clock: String, tmFormat: String): Boolean {
        return try {
            val sdf = SimpleDateFormat(tmFormat, Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(clock)
            false
        } catch (e: ParseException){
            true
        }
    }


}
package org.dicoding.submision1dicoding

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_pengaturan_notifikasi.*
import org.dicoding.submision1dicoding.fragment.MoviewFragment
import org.dicoding.submision1dicoding.fragment.TvFragment
import org.dicoding.submision1dicoding.model.ModelViewRilis
import java.text.SimpleDateFormat
import java.util.*

class PengaturanNotifikasi : AppCompatActivity() {

    private var idNotification = 0
    private val stackNotif = ArrayList<String>()
    private lateinit var notifikasiViewModel: ModelViewRilis

    companion object {
        val NOTIFICATION_ID = 1
        val CHANNEL_ID = "channel_id"
        val CHANNEL_NAME: CharSequence = "channel_name"

        private const val GROUP_KEY_EMAILS = "group_key_emails"
        private const val NOTIFICATION_REQUEST_CODE = 200
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan_notifikasi)

        switch_btn_rilis.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Remainder()
                Toast.makeText(this, "Remainder di Aktifkan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Remainder di NonAktifkan", Toast.LENGTH_SHORT).show()
            }
        }


        switch_btn_rilistoday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rilisToday()
            } else {
                Toast.makeText(this, "Remainder di NonAktifkan", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun rilisToday() {
        val calendar = Calendar.getInstance()
        val currentDate = java.text.DateFormat.getDateInstance().format(calendar.time)
        val dateFormatInput = SimpleDateFormat("dd MMM yyyy")
        val hasil = dateFormatInput.parse(currentDate)
        val dateFormatOutput = SimpleDateFormat("yyyy-MM-dd")
        val date = dateFormatOutput.format(hasil)

        val calendarTime = Calendar.getInstance()
        val currentTime = java.text.DateFormat.getTimeInstance().format(calendarTime.time)
        val rilisTime = getString(R.string.rilis_time)

        Log.d("timerillis","$currentTime curret")
        Log.d("timerillis","$rilisTime rilis")
        Log.d("timerillis","$date date")

        if (currentTime == rilisTime) {
            notifikasiViewModel = ViewModelProvider(
                this@PengaturanNotifikasi,
                ViewModelProvider.NewInstanceFactory()
            ).get(ModelViewRilis::class.java)
            notifikasiViewModel.setMovie(date)

            notifikasiViewModel.geMoview().observe(this@PengaturanNotifikasi, Observer {
                if (it != null) {
                    val count = it.size
                    for (x in 0 until count) {
                        val judul = it[x].title
                        Log.d("jududls", judul)
                        stackNotif.add(judul)
                        setNotifrilis()
                        idNotification++
                    }
                }
            })
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        stackNotif.clear()
        idNotification = 0
    }

    private fun setNotifrilis() {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_notifications_black_24dp)
        val intent = Intent(this, TvFragment::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent =
            PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mBuilder: NotificationCompat.Builder
        val CHANNEL_ID = "channel_01"


        mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("New Movie Today")
            .setContentText(stackNotif[idNotification])
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setLargeIcon(largeIcon)
            .setGroup(GROUP_KEY_EMAILS)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        Log.d("jududl", stackNotif[idNotification])

        /*
        Untuk android Oreo ke atas perlu menambahkan notification channel
        Materi ini akan dibahas lebih lanjut di modul extended
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            mBuilder.setChannelId(CHANNEL_ID)

            mNotificationManager.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()

        mNotificationManager.notify(idNotification, notification)
    }

    fun Remainder() {
        val calendar = Calendar.getInstance()
        val currentTime = java.text.DateFormat.getTimeInstance().format(calendar.time)
        val remainderTime = getString(R.string.remainder_time)

        if (currentTime == remainderTime) {
            SetNotif()
        }
    }

    fun SetNotif() {
        val intent = Intent(this, MoviewFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.notification_icon).setColor(resources.getColor(R.color.colorPrimary))
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.notification_icon))
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.remainder))
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }
        val notificarion = mBuilder.build()
        mNotificationManager.notify(NOTIFICATION_ID, notificarion)
    }
}



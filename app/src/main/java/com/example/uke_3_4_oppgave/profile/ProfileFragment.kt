package com.example.uke_3_4_oppgave.profile

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.uke_3_4_oppgave.R
import com.example.uke_3_4_oppgave.tabbar.MainActivity
import com.example.uke_3_4_oppgave.UserManager

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    private lateinit var circleTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var firstNameTextView: TextView
    private lateinit var logOutButton: Button

   private lateinit var notificationButton: Button

    private lateinit var notificationManager: NotificationManager

    private val channelId = "com.jorfald.smalltalk.important.notifications"
    private var counter = 111

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)

        circleTextView = view.findViewById(R.id.circle_image_text_view)
        usernameTextView = view.findViewById(R.id.user_name_text_view)
        firstNameTextView = view.findViewById(R.id.first_name_text_view)
        logOutButton = view.findViewById(R.id.log_out_button)

        notificationButton = view.findViewById(R.id.notification_button)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()

        val user = UserManager.loggedInUser

        circleTextView.text = user.firstName.firstOrNull()?.toString() ?: "X"
        usernameTextView.text = user.userName
        firstNameTextView.text = user.firstName

        logOutButton.setOnClickListener {
            (activity as? MainActivity)?.logOutUser()
        }
        notificationButton.setOnClickListener{
            createAndSendNotification()

        }
    }

   @RequiresApi(Build.VERSION_CODES.O)
   private fun createNotificationChannel() {
       val channel = NotificationChannel(
           channelId,
           "Important messages",
           NotificationManager.IMPORTANCE_HIGH)

       channel.description = "Only highly important messages"
       channel.enableLights(true)
       channel.lightColor = Color.RED
       channel.enableVibration(true)
       channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

       notificationManager.createNotificationChannel(channel)
   }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAndSendNotification() {
        val notificationId = counter

        val notification = Notification.Builder(activity, channelId)
            .setContentTitle("Very important news!")
            .setContentText("This is extremely important: Hello World!.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            //.setChannelId(channelId)
            // .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
        counter++
    }
}
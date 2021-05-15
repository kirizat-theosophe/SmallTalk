package com.example.uke_3_4_oppgave.chat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uke_3_4_oppgave.R
import com.example.uke_3_4_oppgave.database.AppDatabase
import com.example.uke_3_4_oppgave.UserManager
import com.example.uke_3_4_oppgave.Utvidelser.hideKeyboard
import com.example.uke_3_4_oppgave.ChatDAO
import com.example.uke_3_4_oppgave.ChatObject
import java.util.*
import kotlin.concurrent.fixedRateTimer

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    //private lateinit var logOutButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatInput: EditText
    private lateinit var sendButton: Button
    private lateinit var loader: ProgressBar

    private var timer: Timer? = null
    private val user = UserManager.loggedInUser
    private lateinit var chatDAO: ChatDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //logOutButton = view.findViewById(R.id.log_out_button)
        recyclerView = view.findViewById(R.id.chat_recycler_view)

        chatInput = view.findViewById(R.id.chat_input)
        sendButton = view.findViewById(R.id.send_message_button)

        //activity?.hideKeyboard()

        loader = view.findViewById(R.id.loader)
        loader.isVisible = false

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatDAO = AppDatabase.getDatabase(requireContext()).chatDAO()

        bindObservers()
        setButtonListeners()
        initRecyclerView()
        viewModel.getChatMessagesFromDatabase()
    }

    override fun onResume() {
        super.onResume()

        startChatFetchTimer()
    }

    private  fun bindObservers() {
        viewModel.chatMessagesLiveData.observe(viewLifecycleOwner, { newList ->
            //viewModel.saveChat(chatDAO, newList)

            activity?.runOnUiThread {
                chatAdapter.updateData(newList)
                scrollToBottom()
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            loader.isVisible = isLoading

        })
    }

    private fun setButtonListeners() {
        sendButton.setOnClickListener{
            val text = chatInput.text.toString()

            val chatObject = ChatObject(0, user.userId, user.userName, text, Date().time)


                if (text.isNotEmpty()) {
                    viewModel.sendChatMessage(
                        chatObject
                    ) { success ->
                        if (success) {
                            activity?.hideKeyboard()
                            chatInput.setText("")
                            chatAdapter.addInstance(chatObject)
                            scrollToBottom()
                        } else {
                            Toast.makeText(
                                context,
                                "Noe gikk galt. Kunne ikke sende melding",
                                Toast.LENGTH_LONG
                            ).show()

                     }
                }
            }
        }
    }

    private fun initRecyclerView(){
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager

        //val userId= sharedPreferences.getString(SHARED_PREFS_ID_KEY,null)

        //if (userId != null) {
            chatAdapter = ChatAdapter(
                listOf(),
                user.userId
            )
            recyclerView.adapter = chatAdapter
        //}else{
          //  logOutUser()
        //}
    }

    private fun getChatMessages(){
         viewModel.getChatMessages(
                user.userId
         ) {
            Toast.makeText(
                        context,
                        "Noe gikk galt. Kunne ikke hente meldinger",
                        Toast.LENGTH_LONG
                ).show()
                }
    }

    private fun scrollToBottom() {
        recyclerView.scrollToPosition((recyclerView.adapter?.itemCount ?: 1) -1)
    }

    private fun startChatFetchTimer(){
        if (timer != null){
            return
        }

        timer = fixedRateTimer("chatFetchTimer", false, 0L, 15 * 1000) {
            getChatMessages()
        }
    }
    override fun onPause() {
        super.onPause()

        timer?.cancel()
        timer = null
    }
}
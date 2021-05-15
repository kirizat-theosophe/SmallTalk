package com.example.uke_3_4_oppgave.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uke_3_4_oppgave.ChatObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val chatRepository = ChatRepository()

    val isLoading = MutableLiveData(false)
    val chatMessagesLiveData: MutableLiveData<List<ChatObject>> = MutableLiveData()

    fun getChatMessages(
            userId: String,
            errorCallback: () -> Unit
    ) {
        //if (userId != null) {
            chatRepository.fetchChatMessages(userId) { chatMessagesList ->
                if (chatMessagesList != null){
                    if (chatMessagesLiveData.value != chatMessagesList) {
                        chatMessagesLiveData.postValue(chatMessagesList)
                    }
                } else {
                    errorCallback()
                }
                isLoading.postValue(false)
            }
        }
    //}

    fun sendChatMessage(
            chatObject: ChatObject,
            callback: (Boolean) -> Unit
    ) {
        chatRepository.sendMessage(chatObject, callback)
    }

    fun saveChat(newList: List<ChatObject>) {
        CoroutineScope(Dispatchers.IO).launch {
           chatRepository.saveChatMessagesToDB(newList)
        }
    }

    fun getChatMessagesFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val messages = chatRepository.getChatMessagesFromDB()

            chatMessagesLiveData.postValue(messages)

            if (messages.isEmpty()) {
                isLoading.postValue(true)
            }
        }
    }
}
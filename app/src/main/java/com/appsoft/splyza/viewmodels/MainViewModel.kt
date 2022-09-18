package com.appsoft.splyza.viewmodels

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.appsoft.splyza.BuildConfig
import com.appsoft.splyza.data.Members
import com.appsoft.splyza.data.Plan
import com.appsoft.splyza.interactor.MainInteractor
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = MainViewModel::class.java.simpleName
    private val app = application
    val members: MutableLiveData<Members> by lazy {
        MutableLiveData<Members>()
    }
    val plan:MutableLiveData<Plan> by lazy {
        MutableLiveData<Plan>()
    }
    val newInviteUrl:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val copyToClipboard:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private val mainInteractor = MainInteractor()

    fun getTeamInfo() {
        viewModelScope.launch {
            val team = mainInteractor.getTeamInfo()
            members.postValue(team.members)
            plan.postValue(team.plan)
        }
    }

    fun getTeamInvite(selectedPermissionLevel:String) {
        val role:String = when(selectedPermissionLevel) {
            "Coach" -> "manager"
            "Player Coach" -> "editor"
            "Player" -> "member"
            "Supporter" -> "readonly"
            else -> {"Invalid Role"}
        }
        viewModelScope.launch {
            val invites = mainInteractor.getTeamInvite(role)
            newInviteUrl.postValue(invites.url)
        }
    }

    fun copyInviteLinkToClipBoard() {
        val clipboardManager = app.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Splyza_InviteLink", newInviteUrl.value)
        clipboardManager.setPrimaryClip(clipData)
        copyToClipboard.postValue(true)
        if (BuildConfig.DEBUG) {
            Log.d(TAG,"Copied Text: ${clipboardManager.primaryClip?.getItemAt(0)?.text}")
        }
    }
}
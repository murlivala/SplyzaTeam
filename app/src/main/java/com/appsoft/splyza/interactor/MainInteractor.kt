package com.appsoft.splyza.interactor

import com.appsoft.splyza.data.Invites
import com.appsoft.splyza.data.TeamInfo
import com.appsoft.splyza.repository.MainRepository

class MainInteractor {
    val mainRepository = MainRepository()
    suspend fun getTeamInfo():TeamInfo {
        return mainRepository.getTeamInfo()
    }

    suspend fun getTeamInvite(role:String):Invites {
        return mainRepository.getTeamInvite(role)
    }
}
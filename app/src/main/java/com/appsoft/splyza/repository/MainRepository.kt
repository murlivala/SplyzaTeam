package com.appsoft.splyza.repository

import com.appsoft.splyza.data.Invites
import com.appsoft.splyza.data.TeamInfo

class MainRepository {
    val mockCloud = MockCloud()
    suspend fun getTeamInfo():TeamInfo {
        return mockCloud.getTeamInfo()
    }

    suspend fun getTeamInvite(role:String):Invites {
        return mockCloud.getTeamInvite(role)
    }
}
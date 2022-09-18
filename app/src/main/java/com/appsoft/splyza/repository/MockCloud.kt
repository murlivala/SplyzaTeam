package com.appsoft.splyza.repository

import android.util.Log
import com.appsoft.splyza.BuildConfig
import com.appsoft.splyza.data.Invites
import com.appsoft.splyza.data.TeamInfo
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.random.Random

class MockCloud (val dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    private val TAG:String = MockCloud::class.java.simpleName
    val gSon = Gson()
    suspend fun getTeamInfo():TeamInfo = withContext(dispatcher) {

        /**
         * No real api is available so response will be mocked with random values
         */

        var admins = Random.nextInt(0, 200)
        var managers = Random.nextInt(0, 200)
        var editors = Random.nextInt(0, 200)
        var members = Random.nextInt(0, 200)
        val supporters = Random.nextInt(0, 200)
        var total = admins + managers + editors + members + supporters

        var membersLimit = Random.nextInt(0, 200)
        if (membersLimit < total - supporters) {
            membersLimit = total + 10
        }
        var supportersLimit = Random.nextInt(0, 200)

        if (supportersLimit < supporters) {
            supportersLimit = supporters
        }

        if (total%3 == 0) { // simulate use-case #1
            admins = 5
            managers = 10
            editors = 20
            members = 30

            total = admins + managers + editors + members + supporters
            membersLimit = admins + managers + editors + members
        } else if (total%5 == 0) { // simulate use-case #2
            supportersLimit = 0
        } else if (total%7 == 0 && supporters > 0) { // simulate use-case #3
            supportersLimit = supporters
        }

        val teamInfoMockResponse = "{\n" +
                "\"id\": \"${UUID.randomUUID()}\",\n" +
                "\"members\": {\n" +
                "\"total\": ${total},\n" +
                "\"administrators\": ${admins},\n" +
                "\"managers\": ${managers},\n" +
                "\"editors\": ${editors},\n" +
                "\"members\": ${members},\n" +
                "\"supporters\": ${supporters}\n" +
                "},\n" +
                "\"plan\": {\n" +
                "\"memberLimit\": ${membersLimit},\n" +
                "\"supporterLimit\": ${supportersLimit}\n" +
                "}\n" +
                "}\n"

        val teamInfo = gSon.fromJson(teamInfoMockResponse, TeamInfo::class.java)
        return@withContext teamInfo
    }

    suspend fun getTeamInvite(role:String):Invites = withContext(dispatcher) {

        /**
         * No real api is available so response will be mocked with random values
         */

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getTeamInvite : role ${role}")
        }
        val inviteMockResponse = "{\n" +
                "\"url\": \"https://example.com/ti/${UUID.randomUUID()}\"\n" +
                "}"

        val inviteUrl = gSon.fromJson(inviteMockResponse, Invites::class.java)
        return@withContext inviteUrl
    }
}
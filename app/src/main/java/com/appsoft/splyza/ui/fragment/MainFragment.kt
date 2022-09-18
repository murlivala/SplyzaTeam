package com.appsoft.splyza.ui.fragment

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.appsoft.splyza.BuildConfig
import com.appsoft.splyza.R
import com.appsoft.splyza.viewmodels.MainViewModel


class MainFragment : Fragment() {

    private val TAG:String = MainFragment::class.java.simpleName
    private lateinit var lblBack: TextView
    private lateinit var lblTitle: TextView
    private lateinit var btnShareQrCode:Button
    private lateinit var btnCopyLink:Button
    private lateinit var rlPermLevels:RelativeLayout
    private lateinit var tvCurrentPermLevel:TextView
    private lateinit var tvMembers:TextView
    private lateinit var tvMembersLimit:TextView
    private lateinit var tvSupporters:TextView
    private lateinit var tvSupportersLimit:TextView

    private var members = 0
    private var supporters = 0
    private var membersLimit = 0
    private var supportersLimit = 0

    private var newInviteUrl:String? = null

    private lateinit var mainViewModel:MainViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        mainViewModel.getTeamInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        lblBack = view.findViewById(R.id.lblBack)
        lblTitle = view.findViewById(R.id.lblTitle)
        btnCopyLink = view.findViewById(R.id.btnCopyLink)
        btnShareQrCode = view.findViewById(R.id.btnShareQrCode)
        rlPermLevels = view.findViewById(R.id.rlPermLevels)
        tvCurrentPermLevel = view.findViewById(R.id.tvCurrentPermLevel)
        tvMembers = view.findViewById(R.id.tvMembers)
        tvMembersLimit = view.findViewById(R.id.tvMembersLimit)
        tvSupporters = view.findViewById(R.id.tvSupporters)
        tvSupportersLimit = view.findViewById(R.id.tvSupportersLimit)

        lblBack.text = getString(R.string.text_back_button)
        lblTitle.text = getString(R.string.text_invite_members)

        lblBack.setOnClickListener {
            processBackPress()
        }

        rlPermLevels.setOnClickListener {
            showPermissionDialog()
        }

        btnCopyLink.setOnClickListener {
            mainViewModel.copyInviteLinkToClipBoard()
        }

        btnShareQrCode.setOnClickListener {
            newInviteUrl?.let {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, ShareQrCodeFragment.newInstance(it))
                    ?.addToBackStack(null)
                    ?.commit()
            } ?: run {
                Toast.makeText(requireActivity().applicationContext, "No Invite URL available, Please get one by clicking on drop down", Toast.LENGTH_LONG).show()
            }
        }


        mainViewModel.members.observe(viewLifecycleOwner) {
            members = it.total - it.supporters
            supporters = it.supporters
            tvMembers.text = getString(R.string.current_members, members)
            tvSupporters.text = getString(R.string.current_supporters, it.supporters)
        }

        mainViewModel.plan.observe(viewLifecycleOwner) {
            membersLimit = it.memberLimit
            supportersLimit = it.supporterLimit
            tvMembersLimit.text = getString(R.string.current_members_limit, it.memberLimit)
            tvSupportersLimit.text = getString(R.string.current_supporters_limit, it.supporterLimit)

            if (supportersLimit == 0) {
                tvSupporters.visibility = View.GONE
                tvSupportersLimit.visibility = View.GONE
            } else {
                tvSupporters.visibility = View.VISIBLE
                tvSupportersLimit.visibility = View.VISIBLE
            }
        }

        mainViewModel.newInviteUrl.observe(viewLifecycleOwner) {
            newInviteUrl = it
        }

        mainViewModel.copyToClipboard.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity().applicationContext, "Copied!", Toast.LENGTH_SHORT).show()
        }

        mainViewModel.getTeamInvite(tvCurrentPermLevel.text.toString())
        return view
    }

    fun showPermissionDialog() {
        val dialog = Dialog(requireActivity(), R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.permissions_levels)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val permLevelCoach = dialog.findViewById<TextView>(R.id.tvPerm1)
        permLevelCoach.setOnClickListener {
            tvCurrentPermLevel.text = getString(R.string.text_permission_level_coach)
            mainViewModel.getTeamInvite(tvCurrentPermLevel.text.toString())
            dialog.dismiss()
        }

        val permLevelPlayerCoach = dialog.findViewById<TextView>(R.id.tvPerm2)
        permLevelPlayerCoach.setOnClickListener {
            tvCurrentPermLevel.text = getString(R.string.text_permission_level_player_coach)
            mainViewModel.getTeamInvite(tvCurrentPermLevel.text.toString())
            dialog.dismiss()
        }

        val permLevelPlayer = dialog.findViewById<TextView>(R.id.tvPerm3)
        permLevelPlayer.setOnClickListener {
            tvCurrentPermLevel.text = getString(R.string.text_permission_level_player)
            mainViewModel.getTeamInvite(tvCurrentPermLevel.text.toString())
            dialog.dismiss()
        }

        val permLevelSupporters = dialog.findViewById<TextView>(R.id.tvPerm4)
        permLevelSupporters.setOnClickListener {
            tvCurrentPermLevel.text = getString(R.string.text_permission_level_supporter)
            mainViewModel.getTeamInvite(tvCurrentPermLevel.text.toString())
            dialog.dismiss()
        }

        if (members == membersLimit) { // use-case 1 i.e., team is full
            permLevelCoach.isEnabled = false
            permLevelPlayer.isEnabled = false
            permLevelPlayerCoach.isEnabled = false
            permLevelCoach.setTextColor(resources.getColor(R.color.light_gray, null))
            permLevelPlayer.setTextColor(resources.getColor(R.color.light_gray, null))
            permLevelPlayerCoach.setTextColor(resources.getColor(R.color.light_gray, null))
        } else {
            permLevelCoach.isEnabled = true
            permLevelPlayer.isEnabled = true
            permLevelPlayerCoach.isEnabled = true
            permLevelCoach.setTextColor(resources.getColor(android.R.color.holo_blue_dark, null))
            permLevelPlayer.setTextColor(resources.getColor(android.R.color.holo_blue_dark, null))
            permLevelPlayerCoach.setTextColor(resources.getColor(android.R.color.holo_blue_dark, null))
        }

        if (supportersLimit == 0) { // use-case #2 i.e., no supporters
            permLevelSupporters.visibility = View.GONE
        } else {
            if (supporters > 0 && supporters == supportersLimit) { // use-case #3 i.e., no open slots for supporters
                permLevelSupporters.isEnabled = false
                permLevelSupporters.setTextColor(resources.getColor(R.color.light_gray, null))
            } else {
                permLevelSupporters.visibility = View.VISIBLE
                permLevelSupporters.setTextColor(resources.getColor(android.R.color.holo_blue_dark, null))
            }
        }

        dialog.show()
    }


    private fun processBackPress() {
        Log.d(TAG,"onBackPressed ${activity?.supportFragmentManager?.backStackEntryCount}")
        activity?.supportFragmentManager?.backStackEntryCount?.let {
            if (it > 0) {
                activity?.supportFragmentManager?.popBackStack()
                lblBack.visibility = View.VISIBLE
                return
            } else {
                lblBack.visibility = View.GONE
            }
        }
    }
}
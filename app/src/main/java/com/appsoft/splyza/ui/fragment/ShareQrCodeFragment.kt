package com.appsoft.splyza.ui.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.appsoft.splyza.R
import io.github.g0dkar.qrcode.QRCode

private const val ARG_PARAM_QR_CODE = "qrCodeString"

/**
 * A simple [Fragment] subclass.
 * Use the [ShareQrCodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShareQrCodeFragment : Fragment() {
    private val TAG = ShareQrCodeFragment::class.java.simpleName
    private var qrCodeStr: String? = null
    private lateinit var imgQrCode:ImageView
    private lateinit var lblBack: TextView
    private lateinit var lblTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            qrCodeStr = it.getString(ARG_PARAM_QR_CODE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_share_qr_code, container, false)
        lblBack = view.findViewById(R.id.lblBack)
        lblTitle = view.findViewById(R.id.lblTitle)
        imgQrCode = view.findViewById(R.id.imgQrCode)

        qrCodeStr?.let {
            val qrCodeBitmap = QRCode(it).render().nativeImage() as Bitmap
            imgQrCode.setImageBitmap(qrCodeBitmap)
        }

        lblBack.text = getString(R.string.text_back_button)
        lblTitle.text = getString(R.string.text_my_qr_code)

        lblBack.setOnClickListener {
            processBackPress()
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param ARG_PARAM_QR_CODE QR Code string.
         * @return A new instance of fragment ShareQrCodeFragment.
         */
        @JvmStatic
        fun newInstance(qrCodeStr: String) =
            ShareQrCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_QR_CODE, qrCodeStr)
                }
            }
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
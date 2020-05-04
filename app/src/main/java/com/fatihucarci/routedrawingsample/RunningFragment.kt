package com.fatihucarci.routedrawingsample


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_running.view.*

/**
 * A simple [Fragment] subclass.
 */
class RunningFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_running, container, false)
        view.btnStartRunning.setOnClickListener {
            val intent=Intent(activity,MapsActivity::class.java)
            startActivity(intent)
        }
        return view
    }


}

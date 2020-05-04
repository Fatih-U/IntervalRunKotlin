package com.fatihucarci.routedrawingsample


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatihucarci.routedrawingsample.room.RunDatabase
import com.fatihucarci.routedrawingsample.view.RunItemAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)


        return view


    }

    @InternalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rv_run_list.setHasFixedSize(true)
        rv_run_list.layoutManager = LinearLayoutManager(context)



        // Inflate the layout for this fragment
        launch {
            this@HomeFragment.let {
                var runList = RunDatabase(context!!).getRunActivityDAO().getRunActivities()
                rv_run_list.adapter = RunItemAdapter(runList)
            }
        }

    }


}

package com.fatihucarci.routedrawingsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.fatihucarci.routedrawingsample.room.RunActivity
import com.fatihucarci.routedrawingsample.room.RunDatabase
import kotlinx.android.synthetic.main.activity_run_detail.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

class RunDetailActivity : BaseActivity() {

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) //display back button
        supportActionBar?.setDisplayShowHomeEnabled(true) //display back button

        val runId = intent.getIntExtra("RunID",0)


        var runItem: RunActivity? = null

        launch {
            this@RunDetailActivity.let {
                runItem = RunDatabase(this@RunDetailActivity).getRunActivityDAO().getRun(runId)

                setupMap(runItem)

            }

        }


    }

    private fun setupMap(runItem: RunActivity?) {

        Toast.makeText(this@RunDetailActivity,"${runItem?.startTimeMilli}",Toast.LENGTH_SHORT).show()
        //Toast.makeText(this,"${runItem.endTimeMilli}",Toast.LENGTH_SHORT).show()
        tvRunId.text = runItem?.id.toString()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

}

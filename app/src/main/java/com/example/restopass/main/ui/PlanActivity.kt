package com.example.restopass.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.restopass.R
import com.example.restopass.main.ui.home.PlanData

class PlanActivity : AppCompatActivity() {

    private var plan: PlanData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)
        plan = intent.extras?.getParcelable("plan")
        Toast.makeText(this, "Se seleccionó: ${plan?.title}", Toast.LENGTH_LONG).show()
    }
}

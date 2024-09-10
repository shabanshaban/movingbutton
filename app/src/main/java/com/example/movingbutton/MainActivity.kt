package com.example.movingbutton

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movingbutton.enums.ButtonPosition

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


      val movingButton=  findViewById<MovingButton>(R.id.moving_button)
        movingButton.onPositionChangedListener=object :MovingButton.OnPositionChangedListener{
            override fun onPositionChanged(action: Int, position: ButtonPosition?) {
               val positionBtn = position.toString()
            }
        }
    }
}
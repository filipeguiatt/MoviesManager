package ipt.pt.sd.moviesmanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ipt.pt.sd.moviesmanager.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val background = object : Thread(){
            override fun run() {
                try{
                    sleep(5000)

                    val intent= Intent(baseContext,LoginActivity::class.java)
                    startActivity(intent)

                }catch(e: Exception){
                    e.printStackTrace()
                }
            }

        }
        background.start()
    }
}
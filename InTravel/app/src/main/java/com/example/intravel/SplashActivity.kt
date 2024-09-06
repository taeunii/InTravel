package com.example.intravel

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // 타이머가 끝나면 내부 실행
        Handler().postDelayed(Runnable {
            // 앱의 MainActivity로 넘어가기
            val i = Intent(this@SplashActivity,MainActivity::class.java)
            startActivity(i)

            // overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN/CLOSE, enterAnim, exitAnim)
            overridePendingTransition(R.anim.rightin_activity,R.anim.not_move_activity)
            // 현재 액티비티 닫기
            finish()
        }, 3000) // 3초
    }
}
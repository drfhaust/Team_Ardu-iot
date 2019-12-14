package ng.arduiot.ardu_iot

import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val auth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            if (auth.currentUser == null){
                launchClass(LoginActivity::class.java, newTask = true)
            }
            else{
                launchClass(DashBoardActivity::class.java, newTask = true)
            }
        }, 2000)

    }
}

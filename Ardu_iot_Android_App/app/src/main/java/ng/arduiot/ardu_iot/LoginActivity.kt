package ng.arduiot.ardu_iot

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import ng.arduiot.ardu_iot.R

class LoginActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        login_btn.setOnClickListener {
            if (email_et.isEmailValid() && password_et.isValid(6)) login()
        }

        create_account_tv.setOnClickListener {
            launchClass(CreateAccountActivity::class.java)
        }
    }

    private fun login() {
        val email = email_et.trimmedText()
        val password = password_et.trimmedText()

        showProgress()

        auth.signInWithEmailAndPassword(  email, password)
            .addOnCompleteListener(this) { task ->
                hideProgress()
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    launchClass(DashBoardActivity::class.java)
                } else {
                    showToast("Authentication failed.")
                }
            }
    }
}

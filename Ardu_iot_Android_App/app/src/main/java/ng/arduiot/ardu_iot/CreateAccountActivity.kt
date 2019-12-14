package ng.arduiot.ardu_iot

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        auth = FirebaseAuth.getInstance()

        create_account_btn.setOnClickListener {
            if (email_et.isEmailValid() && password_et.isValid(6)
                && doPasswordsMatch()) createAccount()
        }

        login_tv.setOnClickListener {
            launchClass(CreateAccountActivity::class.java)
        }
    }

    private fun doPasswordsMatch(): Boolean {
        val valid = password_et.trimmedText() == confirm_password_et.trimmedText()
        if (!valid){
            password_et.error = "Passwords don't mathc"
            confirm_password_et.error = "Passwords don't mathc"
        }
        return valid
    }

    private fun createAccount() {
        val email = email_et.trimmedText()
        val password = password_et.trimmedText()

        showProgress()

        auth.createUserWithEmailAndPassword(email, password)
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

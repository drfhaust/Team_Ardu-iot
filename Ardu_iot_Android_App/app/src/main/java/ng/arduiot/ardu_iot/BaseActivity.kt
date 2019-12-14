package ng.arduiot.ardu_iot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import java.util.regex.Pattern

/**
 * Created by Akano Kola on 2019-09-02.
 */

open class BaseActivity : AppCompatActivity() {

//    val context = this@BaseActivity

    fun mContext(): BaseActivity {
        return this@BaseActivity
    }


    override fun setContentView(layout: Int){
        super.setContentView(layout)
//        val backImg = findViewById<ImageView>(R.id.back_img)
//        backImg?.setOnClickListener {
//            onBackPressed()
//        }
    }

    fun showProgress(text: String = "Please wait ...") {
        log("showProgress")
        hideSoftKeyboard()
        val viewGroup = window.decorView as ViewGroup
        val progressView = LayoutInflater.from(this).inflate(R.layout.progress_view, null, true)
        progressView.tag = 780
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        viewGroup.addView(progressView, params)
        progressView.visibility = View.VISIBLE
        val textView = progressView.findViewById<TextView>(R.id.message)
        textView.visibility = View.VISIBLE
        textView.text = text

    }

    fun hideProgress() {
        log("hideProgress")
        val viewGroup1 = window.decorView as ViewGroup
        val progressView1 = viewGroup1.findViewWithTag<View>(780)
        if (progressView1 != null) {
            progressView1.visibility = View.GONE
            viewGroup1.removeView(progressView1)
        }
    }

    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    fun EditText.requestFocusAndShowSoftKeyboard() {
        this.requestFocus()
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun EditText.showPickerDialog(title:String, array: Array<String>) {
//        this.isFocusableInTouchMode = false
//
//        val pickerDialogFragment = PickerDialogFragment.newInstance(title, array)
//        pickerDialogFragment.attachListener(object : Listener<Int> {
//            override fun onClick(t: Int) {
//                setText(array[t])
//            }
//        })
//        pickerDialogFragment.show(supportFragmentManager, null)
//    }

    fun EditText.trimmedText() : String{
        return this.text.toString().trim()
    }

    fun EditText.isValid(no : Int = 1, errorText : String = "Field cannot be empty", showToast:Boolean = false, toastText : String = "Field cannot be empty") : Boolean{
        val valid =  this.trimmedText().length >= no

        if (!valid) {
            this.error = errorText
            if (showToast)
                showToast(toastText)
        }

        return valid
    }

    fun isValid(editTexts: List<EditText>, showToast:Boolean = false):Boolean{
        var valid = true
        for (editText in editTexts){
            valid = valid && editText.isValid(showToast = showToast, toastText = "Kindly Complete the form")
        }
        return valid
    }

    fun showToast(string : String, length:Int = Toast.LENGTH_SHORT){
        Toast.makeText(mContext(), string, length).show()
    }

    fun showDebugToast(text: String) {
        if (BuildConfig.DEBUG)
            showToast(text)
    }

    fun EditText.isEmailValid(): Boolean {
        val email = this.trimmedText()

        val expression = "^[\\w\\.]+@([\\w]+\\.)+[A-Z]{2,7}$"
        val valid = Pattern.compile(expression, Pattern.CASE_INSENSITIVE).matcher(email).matches()

        if (!valid) this.error = "Invalid email"

        return valid
    }

    fun launchClass(aClass: Class<*>, bundle: Bundle? = null, newTask: Boolean = false) {
        val intent = Intent(mContext(), aClass)
        if (bundle != null)
            intent.putExtras(bundle)
        if (newTask)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        mContext().startActivity(intent, bundle)
    }

    fun getFragmentTransaction(): FragmentTransaction {
        return supportFragmentManager.beginTransaction()
    }

//    private val fragmentTransaction = supportFragmentManager.beginTransaction()

    fun changeFragment(frame: Int, fragment: Fragment, replace:Boolean = false) {
        getFragmentTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)

        if (replace) {
            getFragmentTransaction().replace(frame, fragment)
            getFragmentTransaction().addToBackStack(null)
        }
        else  getFragmentTransaction().add(frame, fragment)

        getFragmentTransaction().commit()
    }

    fun log(string : String, preFix : String = ""){
        Log.e("myTag", "$preFix $string")
    }

//    fun isSuccessFull(response: Response<*>): Boolean {
//        hideProgress()
//
//        log(response.toString())
//
//        var body = false
//        val success = response.isSuccessful
//
//        if (!success) {
//            try {
//                val err = response.errorBody()!!.string()
//                log(err)
//                val errorResponse = Gson().fromJson(err, ErrorResponse::class.java)
//                showToast(errorResponse.message ?: "Authentication error")
//
//            }
//            catch (e: Exception) {
//                showToast(e.message!!)
//                e.printStackTrace()
//            }
//
//        } else {
//            body = response.body() != null
//
////            response.body()?.let {
////                log(Gson().toJson(it.toString()))
////            }?.let {
////                showToast("An Error Occurred")
////                log("No body")
////            }
//        }
//        return success && body
//    }

    fun showNetworkError(t: Throwable) {
        hideProgress()
        t.message?.let { log(it) }
        log(t.localizedMessage)

        showToast("Check your internet connection")
    }

    fun isNotEmpty(editTexts: Array<EditText>): Boolean {
        var valid = true
        for (editText in editTexts) {
            valid = valid && editText.isValid()
        }
        return valid
    }
}
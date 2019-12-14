package ng.arduiot.ardu_iot

import android.widget.EditText
import java.util.regex.Pattern

/**
 * Created by Akano Kola on 2019-11-24.
 */
fun EditText.trimmedText() : String{
    return this.text.toString().trim()
}

fun EditText.isValid(no : Int = 1, errorText : String = "Field cannot be empty") : Boolean{
    val valid =  this.trimmedText().length >= no

    if (!valid) {
        this.error = errorText
    }

    return valid
}

fun isValid(editTexts: List<EditText>, showToast:Boolean = false):Boolean{
    var valid = true
    for (editText in editTexts){
        valid = valid && editText.isValid()
    }
    return valid
}

fun EditText.isEmailValid(): Boolean {
    val email = this.trimmedText()

    val expression = "^[\\w\\.]+@([\\w]+\\.)+[A-Z]{2,7}$"
    val valid = Pattern.compile(expression, Pattern.CASE_INSENSITIVE).matcher(email).matches()

    if (!valid) this.error = "Invalid email"

    return valid
}
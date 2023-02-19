package us.carleton.onetouchhelp.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import us.carleton.onetouchhelp.R
import us.carleton.onetouchhelp.databinding.ContactsDialogBinding
import java.util.*

lateinit var dialogViewBinding: ContactsDialogBinding
var phoneNumber: String = "6505556789"
class ContactsDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle(resources.getString(R.string.dialog_title))

        dialogViewBinding = ContactsDialogBinding.inflate(
            requireActivity().layoutInflater)
        dialogBuilder.setView(dialogViewBinding.root)

        dialogBuilder.setPositiveButton("Ok") {
                dialog, which ->
            if (dialogViewBinding.etText.text.toString()!=""){
                phoneNumber = dialogViewBinding.etText.text.toString()
                dialogViewBinding.tvNumber.setText(phoneNumber)
            }
        }
        dialogBuilder.setNegativeButton("Cancel") {
                dialog, which ->
        }
        return dialogBuilder.create()
    }

    fun getPhoneNumber(): String {
        return phoneNumber
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        dialogViewBinding.tvNumber.setText(phoneNumber)
        (dialog as AlertDialog?)!!.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#9e3f3f"))
        (dialog as AlertDialog?)!!.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#9e3f3f"))
    }
}
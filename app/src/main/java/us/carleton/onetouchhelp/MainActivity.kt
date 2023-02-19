package us.carleton.onetouchhelp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import us.carleton.onetouchhelp.data.Contacts
import us.carleton.onetouchhelp.databinding.ActivityMainBinding
import us.carleton.onetouchhelp.databinding.ContactsDialogBinding
import us.carleton.onetouchhelp.dialog.ContactsDialog
import java.io.IOException
import java.util.*

lateinit var addresses: List<Address>
lateinit var binding: ActivityMainBinding
lateinit var bindingDialog: ContactsDialogBinding
lateinit var currentLocation: String
lateinit var countryCode: String


class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingDialog = ContactsDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        countryCode = ""
        getLocation()

        binding.btnPolice.setOnClickListener {
            if (countryCode!= ""){
                var phoneNumber = setPoliceNumber(countryCode)
                if (phoneNumber != null) {
                    intentCall(phoneNumber)
                }
                else{
                    intentCall("911")
                }
            }
        }

        binding.btnAmbulance.setOnClickListener {
            if (countryCode!= ""){
                var phoneNumber = setAmbulanceNumber(countryCode)
                if (phoneNumber != null) {
                    intentCall(phoneNumber)
                }
                else{
                    intentCall("911")
                }
            }
        }
        binding.btnFire.setOnClickListener {
            if (countryCode!= ""){
                var phoneNumber = setFireNumber(countryCode)
                if (phoneNumber != null) {
                    intentCall(phoneNumber)
                }
                else{
                    intentCall("911")
                }
            }
        }

        binding.btnSendText.setOnClickListener{
            intentSend()
        }

        binding.btnSend.setOnClickListener {
            intentSend()
        }

        binding.btnAdd.setOnClickListener { view ->
            val todoDialog = ContactsDialog()
            todoDialog.show(supportFragmentManager, "ContactsDialog")
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        currentLocation = "Latitude: " + location.latitude + " , Longitude: " + location.longitude

        val gcd = Geocoder(this, Locale.getDefault())

        try {
            addresses = gcd.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses.size > 0) System.out.println(addresses[0].locality)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        countryCode = addresses[0].countryCode.toString()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setPoliceNumber(countryCode: String): String? {
        return (Contacts.EmergencyNumber[countryCode]?.get("Police"))
    }
    private fun setAmbulanceNumber(countryCode: String): String? {
        return (Contacts.EmergencyNumber[countryCode]?.get("Ambulance"))
    }
    private fun setFireNumber(countryCode: String): String? {
        return (Contacts.EmergencyNumber[countryCode]?.get("Fire"))
    }

    private fun intentCall(phoneNumber: String) {
        val intentCall = Intent(
            Intent.ACTION_DIAL,
            Uri.parse("tel:{$phoneNumber}")
        )
        startActivity(intentCall)
    }

    private fun intentSend() {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        val todoDialog = ContactsDialog()
        Log.d("degbug", todoDialog.getPhoneNumber())
        sendIntent.data = Uri.parse("sms:${todoDialog.getPhoneNumber()}")
        var body = binding.tvHelp.text.toString() + "\n"
        body += currentLocation
        sendIntent.putExtra("sms_body", body);
        startActivity(sendIntent);
    }
}









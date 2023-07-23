package ipt.pt.sd.moviesmanager

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ipt.pt.sd.moviesmanager.models.User

import kotlinx.android.synthetic.main.register_login.*
import java.io.ByteArrayOutputStream

class RegisterActivity : AppCompatActivity() {
    private var selectedPhotoUri: Uri? = null
    lateinit var imgPhoto: ImageView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_login)
        FirebaseApp.initializeApp(this)

        imgPhoto = findViewById(R.id.imgPhoto)

        findViewById<Button>(R.id.btnChoosePhoto).setOnClickListener{
            val options = arrayOf<CharSequence>("Escolher da Galeria", "Tirar Foto")
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Escolher Foto")
            builder.setItems(options) { _, item ->
                when (item) {
                    0 -> choosePhotoFromGallery()
                    1 -> takePhoto()
                }
            }
            builder.show()
        }

        btRegister.setOnClickListener {
            preformRegister()
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
        }


        btToLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
    private fun preformRegister(){
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Por favor insira um email ou password",Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener
                txtEmail.setText("")
                txtPassword.setText("")
                Log.w("Main","Criado com sucesso ${it.result.user?.uid}")
                saveUserToFirebase()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.w("LoginActivity","Falhou a criação do user ${it.message}")
                Toast.makeText(applicationContext, "${it.message}",Toast.LENGTH_LONG).show()
            }
    }


    private fun saveUserToFirebase(){

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance("https://moviesmanager-99a06-default-rtdb.europe-west1.firebasedatabase.app").getReference("/users/$uid")
        val user = User(uid, txtNome.text.toString())

        ref.setValue(user).addOnSuccessListener {
            Log.d("RegisterActivity","O utilizador foi salvo")
        }
    }

    private fun choosePhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun takePhoto() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            } else {
                Toast.makeText(this, "Permissão de câmera não concedida", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST_CODE -> {
                    data?.data?.let { uri ->
                        selectedPhotoUri = uri
                        try {
                            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                            imgPhoto.setImageBitmap(bitmap)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                CAMERA_REQUEST_CODE -> {
                    data?.extras?.get("data")?.let { imageBitmap ->
                        selectedPhotoUri = getImageUriFromBitmap(imageBitmap as Bitmap)
                        imgPhoto.setImageBitmap(imageBitmap)
                    }
                }
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }



    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 100
        private const val CAMERA_REQUEST_CODE = 101
        private const val CAMERA_PERMISSION_CODE = 102
    }
}




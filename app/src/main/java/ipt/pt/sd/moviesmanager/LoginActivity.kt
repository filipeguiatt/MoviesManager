package ipt.pt.sd.moviesmanager

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.btLogin
import kotlinx.android.synthetic.main.activity_login.btToRegister
import kotlinx.android.synthetic.main.activity_login.txtEmailLogin
import kotlinx.android.synthetic.main.activity_login.txtPasswordLogin


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        if(!isConnected){
            Toast.makeText(this,"Sem conexão à Internet!",Toast.LENGTH_SHORT).show()
        }


        btLogin.setOnClickListener {
            verifyIfIsLogged()
        }

        btToRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun verifyIfIsLogged(){
        val author = Firebase.auth
        val email = txtEmailLogin.text.toString()
        val password = txtPasswordLogin.text.toString()
        val uid = FirebaseAuth.getInstance().uid

        author.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    Log.d("LoginActivity", "Login efetuado com sucesso")
                    val user = author.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Log.w("LoginActivity", "Login Incorreto!", task.exception)
                    Toast.makeText(baseContext, "Email ou password incorretos",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}
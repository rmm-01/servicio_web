package com.mendoza.servicio_web

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mendoza.servicio_web.servicio.RetrofitClient
import com.mendoza.servicio_web.servicio.LoginRequest
import com.mendoza.servicio_web.servicio.LoginResponse
import com.mendoza.servicio_web.servicio.UserIdRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val txtUsername = findViewById<EditText>(R.id.txtUsername)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)


        btnLogin.setOnClickListener {
            val username = txtUsername.text.toString()
            val password = txtPassword.text.toString()

            val request = LoginRequest(username, password)


            // Use coroutines to perform the network request
            CoroutineScope(Dispatchers.IO).launch {
                val usuario_id = RetrofitClient.webService.obtenerIdPorUsername(username)
                val response: Response<LoginResponse> = RetrofitClient.webService.login(request)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()

                        val userId = usuario_id.body()?.id ?: -1  // Obtener el ID del usuario


                        // Establecer el usuario_id en la sesión
                        val userIdRequest = UserIdRequest(userId)
                        val setUserIdResponse = RetrofitClient.webService.setUserId(userIdRequest)

                        if (loginResponse != null && loginResponse.success) {

                            Toast.makeText(this@LoginActivity, "Login exitoso", Toast.LENGTH_SHORT).show()
                            // Redirect to MainActivity
                            val intent = Intent(this@LoginActivity, MenuActivity::class.java).apply {
                                putExtra("EXTRA_TEXT", username)
                            }

                            startActivity(intent)

                            //finish()  // Finish LoginActivity so the user cannot go back to it
                        } else {
                            Toast.makeText(this@LoginActivity, "Credenciales invalidas", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Ocurrió un error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

package com.mendoza.servicio_web

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mendoza.servicio_web.entidad.Item
import com.mendoza.servicio_web.servicio.IdCallback
import com.mendoza.servicio_web.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var btnNuevo: FloatingActionButton
    private lateinit var rvItems: RecyclerView
    private lateinit var btnBuscar: Button
    private lateinit var btnStock: Button

    private lateinit var btnRegresar:FloatingActionButton
    private var adaptador:AdaptadorPersonalizado = AdaptadorPersonalizado()
    private var listaItems: ArrayList<Item> = ArrayList()
    //Guardar el id del usuario
    private var loginActivity:LoginActivity=LoginActivity()


    var userId:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        asignarReferencias()
        cargarDatos()
        val usuarioRecibido = intent.getStringExtra("EXTRA_TEXT")
        lifecycleScope.launch {
            val response = RetrofitClient.webService.obtenerIdPorUsername("user1")
            if (response.isSuccessful && response.body()?.success == true) {
                userId = response.body()?.id
                Toast.makeText(this@MainActivity, "ID del usuario "+userId, Toast.LENGTH_LONG).show()
                // Aquí puedes usar la variable userId
            } else {
                // Manejar el error
                Toast.makeText(this@MainActivity, "Error al obtener el ID del usuario "+usuarioRecibido, Toast.LENGTH_LONG).show()
            }
            /*
            if (userId != null) {
                Toast.makeText(this@MainActivity, "User ID: $userId", Toast.LENGTH_LONG).show()
                // Aquí puedes usar el userId como necesites
            } else {
                Toast.makeText(this@MainActivity, "Error al obtener el ID del usuario", Toast.LENGTH_LONG).show()
            }*/
        }
    }



    private fun mostrarmensaje(msg:String){
        val ventana=AlertDialog.Builder(this)
        ventana.setTitle("Informacion")
        ventana.setMessage(msg)
        ventana.setPositiveButton( "Aceptar",DialogInterface.OnClickListener{dialog, which ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }


    private fun mostrarDatos(){
        rvItems.adapter = adaptador
    }

    private fun eliminar(id:Int,producto:String){
        val ventana= AlertDialog.Builder(this)
        ventana.setTitle("Eliminar")
        ventana.setMessage("¿Desea Eliminar el Item?")
        ventana.setPositiveButton( "Aceptar", DialogInterface.OnClickListener{ dialog, which ->
            CoroutineScope(Dispatchers.IO).launch {
                val rpta=RetrofitClient.webService.eliminarItem(id)
                runOnUiThread{
                    if(rpta.isSuccessful){
                        mostrarmensaje(rpta.body().toString())
                    }
                }
            }
        })
        ventana.setNegativeButton("Cancelar",null)
        ventana.create().show()
    }

    private fun cargarDatos() {
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.obtenerItems()
            runOnUiThread {
                if(rpta.isSuccessful){
                    val listaObtenida = rpta.body()?.listaItems ?: ArrayList()
                    adaptador.agregarDatos(listaObtenida)
                    mostrarDatos()
                    listaItems = rpta.body()!!.listaItems
                    adaptador.agregarDatos(listaItems)
                    mostrarDatos()
                } else {
                    mostrarmensaje("Error al cargar los datos")
                }
            }
        }
    }

    private fun asignarReferencias() {
        btnNuevo = findViewById(R.id.btnNuevo)
        btnBuscar = findViewById(R.id.btnMenuBuscar)
        btnStock = findViewById(R.id.btnStock)
        rvItems = findViewById(R.id.rvItems)
        rvItems.layoutManager = LinearLayoutManager(this)

        btnNuevo.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }

        btnBuscar.setOnClickListener {
            val intent = Intent(this, BuscarActivity::class.java)
            startActivity(intent)
        }

        btnStock.setOnClickListener {
            val intent = Intent(this, StockActivity::class.java)
            startActivity(intent)
        }
        adaptador.setContext(this)
        adaptador.setEliminarItem {
            eliminar(it.item_id,it.item_nombre)
        }
        btnRegresar = findViewById(R.id.btnMainRegresar)
        btnRegresar.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}

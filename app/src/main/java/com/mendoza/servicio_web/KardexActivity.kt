package com.mendoza.servicio_web

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mendoza.servicio_web.entidad.Item
import com.mendoza.servicio_web.entidad.Kardex
import com.mendoza.servicio_web.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KardexActivity : AppCompatActivity() {

    private lateinit var rvKardex: RecyclerView
    private lateinit var btnRegresar:ImageButton
    private var adaptador:AdaptadorKardex =AdaptadorKardex()
    private var listaKardex: ArrayList<Kardex> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kardex)
        asignarReferencias()
        cargarDatos()
    }

    private fun mostrarDatos(){
        rvKardex.adapter = adaptador
    }

    private fun cargarDatos() {
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.obtenerKardex()
            runOnUiThread {
                if(rpta.isSuccessful){
                    val listaObtenida = rpta.body()?.listaKardex ?: ArrayList()
                    adaptador.agregarDatos(listaObtenida)
                    mostrarDatos()
                    listaKardex = rpta.body()!!.listaKardex
                    adaptador.agregarDatos(listaKardex)
                    mostrarDatos()
                } else {
                    mostrarmensaje("Error al cargar los datos")
                }
            }
        }
    }

    private fun mostrarmensaje(msg:String){
        val ventana= AlertDialog.Builder(this)
        ventana.setTitle("Informacion")
        ventana.setMessage(msg)
        ventana.setPositiveButton( "Aceptar", DialogInterface.OnClickListener{ dialog, which ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }

    private fun asignarReferencias() {

        rvKardex = findViewById(R.id.rvKardex)
        rvKardex.layoutManager = LinearLayoutManager(this)

        adaptador.setContext(this)

        btnRegresar = findViewById(R.id.btnkardexRegresar)
        btnRegresar.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}
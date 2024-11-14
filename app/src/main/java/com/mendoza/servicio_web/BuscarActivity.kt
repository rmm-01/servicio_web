package com.mendoza.servicio_web

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mendoza.servicio_web.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BuscarActivity : AppCompatActivity() {

    private lateinit var txtID:EditText
    private lateinit var btnBuscarID:Button
    private lateinit var lblProducto:TextView
    private lateinit var lblPrecio: TextView
    private lateinit var lblOrigen:TextView
    private lateinit var lblDestino:TextView
    private lateinit var lblStock:TextView
    private lateinit var btnRegresar: FloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar)
        asignarReferencias()
    }

    private fun asignarReferencias(){
        txtID= findViewById(R.id.txtID)
        btnBuscarID= findViewById(R.id.btnBuscarID)
        lblProducto= findViewById(R.id.lblProducto)
        lblPrecio= findViewById(R.id.lblPrecio)
        lblOrigen=findViewById(R.id.lblOrigen)
        lblDestino=findViewById(R.id.lblDestino)
        lblStock=findViewById(R.id.lblStock)


        btnBuscarID.setOnClickListener{
            Buscar()
        }

        btnRegresar = findViewById(R.id.btnRegresar)
        btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun Buscar(){
        val id = txtID.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
           // val objUsuario = Usuario(0,"","")
            val respuesta  = RetrofitClient.webService.buscarItem(id.toInt())
            runOnUiThread{
                if(respuesta.isSuccessful){
                    val item = respuesta.body()
                    if(item!=null){
                        lblProducto.text=item.item_nombre
                        lblPrecio.text= item.item_precio.toString()
                        lblOrigen.text= item.item_origen
                        lblDestino.text=item.item_destino
                        lblStock.text=item.item_stock.toString()
                    }
                }
            }
        }
    }
}
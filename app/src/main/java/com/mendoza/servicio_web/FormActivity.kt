package com.mendoza.servicio_web

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mendoza.servicio_web.entidad.Item
import com.mendoza.servicio_web.servicio.RetrofitClient
import com.mendoza.servicio_web.servicio.UserIdRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormActivity : AppCompatActivity() {

    private lateinit var txtProducto: EditText
    private lateinit var txtPrecio: EditText
    private lateinit var spnOrigen: Spinner
    private lateinit var spnDestino: Spinner
    private lateinit var txtStock: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnRegresar: FloatingActionButton

    private var modificar:Boolean=false
    private var id:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        asignarReferencias()
        configurarSpinners()
        recuperararDatos()
    }

    private fun asignarReferencias() {
        txtProducto = findViewById(R.id.txtProducto)
        txtPrecio = findViewById(R.id.txtPrecio)
        spnOrigen = findViewById(R.id.spnOrigen)
        spnDestino = findViewById(R.id.spnDestino)
        txtStock = findViewById(R.id.txtStock)

        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener {
            if(modificar){
                modificar()
            }else{
                agregar()
            }

        }

        btnRegresar = findViewById(R.id.btnRegresar)
        btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun modificar(){
        val item=Item(id,"",0.0,"","",0)
        item.item_nombre=txtProducto.text.toString()
        item.item_precio = txtPrecio.text.toString().toDouble()
        item.item_origen = spnOrigen.selectedItem.toString()
        item.item_destino=spnDestino.selectedItem.toString()
        item.item_stock= txtStock.text.toString().toInt()

        CoroutineScope(Dispatchers.IO).launch{
            val rpta =RetrofitClient.webService.actualizarItem(id,item)
            runOnUiThread{
                if(rpta.isSuccessful){
                    mostrarmensaje(rpta.body().toString())
                }
            }
        }
    }
    private fun configurarSpinners() {
        val opcionesAlmacen = listOf("alm 1", "alm 2", "alm 3", "alm 4")
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesAlmacen)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnOrigen.adapter = adaptador
        spnDestino.adapter = adaptador
    }

    private fun agregar() {
        val nombre = txtProducto.text.toString()
        val precio = txtPrecio.text.toString().toDouble()
        val origen = spnOrigen.selectedItem.toString()
        val destino = spnDestino.selectedItem.toString()
        val stock = txtStock.text.toString().toInt()


        if (nombre.isEmpty() || precio == null || origen.isEmpty() || destino.isEmpty() || stock == null) {
            Toast.makeText(this, "Por favor, complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        val item = Item(
            item_id = 0,  // Se asume que el ID será autogenerado en el servidor
            item_nombre = nombre,
            item_precio = precio,
            item_origen = origen,
            item_destino = destino,
            item_stock = stock
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarItem(item)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarmensaje("Registro exitoso")
                    } else {
                        mostrarmensaje("Error al registrar: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarmensaje("Error de red: ${e.message}")
                }
            }
        }
    }

    private fun recuperararDatos(){
        if(intent.hasExtra("id")){
            modificar=true
            btnRegistrar.setText("Guardar Cambios")
            id=intent.getIntExtra("id",0)
            txtProducto.setText(intent.getStringExtra("producto"))
            txtStock.setText(intent.getIntExtra("stock", 0).toString())
            txtPrecio.setText(intent.getDoubleExtra("precio", 0.0).toString())
            seleccionarValorSpinner((intent.getStringExtra("origen").toString()),spnOrigen)
            seleccionarValorSpinner((intent.getStringExtra("destino").toString()),spnDestino)
        }
    }
    private fun seleccionarValorSpinner(valor: String, spinner: Spinner) {
        val posicion = (spinner.adapter as ArrayAdapter<String>).getPosition(valor)
        spinner.setSelection(posicion)
    }
    private fun mostrarmensaje(mensaje: String) {
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Información")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar") { dialog, _ ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        ventana.create().show()
    }
}

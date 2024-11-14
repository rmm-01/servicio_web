package com.mendoza.servicio_web

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.mendoza.servicio_web.entidad.Item
import com.mendoza.servicio_web.servicio.RetrofitClient
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class StockActivity : AppCompatActivity() {

    private lateinit var txtCapacidad: TextView
    private lateinit var txtStock: TextView
    private lateinit var txtRestante: TextView
    private lateinit var spnAlmacen: Spinner
    private lateinit var btnRegresar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stock)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        asignarReferencias()
        configurarSpinner()

        spnAlmacen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int,id: Long) {
                val origenSeleccionado = parent?.getItemAtPosition(position) as String
                CoroutineScope(Dispatchers.Main).launch {
                    val stockTotal = obtenerStockPorOrigen(origenSeleccionado).await()
                    if (stockTotal >= 0) {
                        txtStock.text = "$stockTotal"
                        val capacidad = txtCapacidad.text.toString().toIntOrNull() ?: 0
                        val restante = capacidad -stockTotal
                        txtRestante.text = "$restante"
                    } else {
                        txtStock.text = when (stockTotal) {
                            -2 -> "Error al obtener el stock"
                            else -> "Error desconocido"
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun asignarReferencias() {
        txtCapacidad = findViewById(R.id.txtCapacidad)
        txtStock = findViewById(R.id.txtStock)
        txtRestante = findViewById(R.id.txtRestante)
        spnAlmacen = findViewById(R.id.spnAlmacen)
        btnRegresar = findViewById(R.id.btnRegresar)

        btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configurarSpinner() {
        CoroutineScope(Dispatchers.Main).launch {val respuesta = RetrofitClient.webService.obtenerItems()
            if (respuesta.isSuccessful) {
                val items = respuesta.body()?.listaItems ?: emptyList<Item>()
                val origenes = items.map { it.item_origen }.toSet()

                val adaptador = ArrayAdapter(this@StockActivity, android.R.layout.simple_spinner_item, origenes.toList())
                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spnAlmacen.adapter = adaptador
            } else {
                AlertDialog.Builder(this@StockActivity)
                    .setTitle("Error")
                    .setMessage("Ocurrió un error al obtener los datos.")
                    .setPositiveButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(this@StockActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                .show()
            }
        }
    }

    private fun obtenerStockPorOrigen(origen: String): Deferred<Int> {
        return CoroutineScope(Dispatchers.Main).async {
            val respuesta = RetrofitClient.webService.obtenerItems()
            if (respuesta.isSuccessful) {
                val items = respuesta.body()?.listaItems ?: emptyList<Item>()
                val stockTotal = items.filter { it.item_origen == origen }
                    .sumOf { it.item_stock }
                stockTotal
            } else {
                -2 // Retornamos -2 si ocurre un error en la petición
            }
        }
    }
}
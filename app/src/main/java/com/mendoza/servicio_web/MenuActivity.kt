package com.mendoza.servicio_web

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btnOption1: Button = findViewById(R.id.btnMenuListar)
        val btnOption2: Button = findViewById(R.id.btnMenuBuscar)
        val btnOption3: Button = findViewById(R.id.btnMenuListarStock)

        val btnKardex: Button = findViewById(R.id.btnKardex)

        btnOption1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnOption2.setOnClickListener {
            val intent = Intent(this, BuscarActivity::class.java)
            startActivity(intent)
        }

        btnOption3.setOnClickListener {
            val intent = Intent(this, StockActivity::class.java)
            startActivity(intent)
        }

        btnKardex.setOnClickListener {
            val intent = Intent(this, KardexActivity::class.java)
            startActivity(intent)
        }
    }
}
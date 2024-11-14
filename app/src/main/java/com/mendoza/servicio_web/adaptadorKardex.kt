package com.mendoza.servicio_web

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mendoza.servicio_web.entidad.Item
import com.mendoza.servicio_web.entidad.Kardex

class AdaptadorKardex:RecyclerView.Adapter<AdaptadorKardex.MiViewHolder>() {

    private var listaKardex: ArrayList<Kardex> = ArrayList()
    private lateinit var context: Context

    fun agregarDatos(kardex: ArrayList<Kardex>) {
        this.listaKardex = kardex
        notifyDataSetChanged()
    }

    fun setContext(context: Context) {
        this.context = context
    }

    inner class MiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val kardexID: TextView = view.findViewById(R.id.kardexID)
        private val kardexInfoItem: TextView = view.findViewById(R.id.kardexProducto)
        private val kardexUsuario: TextView = view.findViewById(R.id.kardexUsuario)
        private val kardexFecha: TextView = view.findViewById(R.id.kardexFecha)
        private val kardexCantidad: TextView = view.findViewById(R.id.kardexCantidad)
        private val kardexCantidadAnterior: TextView = view.findViewById(R.id.kardexAnt)
        private val kardexTipo: TextView = view.findViewById(R.id.kardexTipo)


        fun setValores(kardex: Kardex) {
            kardexID.text = kardex.id.toString()
            kardexInfoItem.text = kardex.info_item
            kardexUsuario.text = kardex.usuario
            kardexFecha.text = kardex.fecha
            kardexCantidad.text = kardex.cantidad.toString()
            kardexCantidadAnterior.text = kardex.cantidad_anterior
            kardexTipo.text = kardex.tipo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filakardex, parent, false)
        return MiViewHolder(view)
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val kardex = listaKardex[position]
        holder.setValores(kardex)
        /*
        holder.filaEditar.setOnClickListener {
            val intent = Intent(context, FormActivity::class.java).apply {
                putExtra("id", kardex.id)
                putExtra("info_item", kardex.info_item)
                putExtra("usuario", kardex.usuario)
                putExtra("fecha", kardex.fecha)
                putExtra("cantidad", kardex.cantidad)
                putExtra("cantidad_anterior", kardex.cantidad_anterior)
                putExtra("tipo", kardex.tipo)
            }
            context.startActivity(intent)
        }

        holder.filaEliminar.setOnClickListener {
            // Implementa la lógica de eliminación si es necesario
        }*/
    }

    override fun getItemCount(): Int {
        return listaKardex.size
    }
}
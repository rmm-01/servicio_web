package com.mendoza.servicio_web

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mendoza.servicio_web.entidad.Item
import com.mendoza.servicio_web.entidad.Kardex

class AdaptadorPersonalizado: RecyclerView.Adapter<AdaptadorPersonalizado.MiViewHolder>() {

    private var listaItems: ArrayList<Item> = ArrayList()

    private lateinit var context: Context

    fun agregarDatos(items: ArrayList<Item>) {
        this.listaItems = items
    }


    private var eliminarItem:((Item)->Unit)?=null

    fun setEliminarItem(callback:(Item)->Unit){
        this.eliminarItem=callback
    }

    fun setContext(context: Context){
        this.context=context
    }

    inner class MiViewHolder(var view: View,) : RecyclerView.ViewHolder(view) {
        private var filaID = view.findViewById<TextView>(R.id.filaID)
        private var filaProducto = view.findViewById<TextView>(R.id.filaProducto)
        private var filaPrecio = view.findViewById<TextView>(R.id.filaPrecio)
        private var filaOrigen = view.findViewById<TextView>(R.id.filaOrigen)
        private var filaDestino = view.findViewById<TextView>(R.id.filaDestino)
        private var filaStock = view.findViewById<TextView>(R.id.filaStock)


        val filaEliminar: Button = itemView.findViewById(R.id.filaEliminar)
        val filaEditar: Button = itemView.findViewById(R.id.filaEditar)

        fun setValores(item: Item) {
            filaID.text = item.item_id.toString()
            filaProducto.text = item.item_nombre
            filaPrecio.text = item.item_precio.toString()
            filaOrigen.text = item.item_origen
            filaDestino.text = item.item_destino
            filaStock.text = item.item_stock.toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int) = MiViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.fila,parent,false)
    )


    override fun onBindViewHolder(holder: AdaptadorPersonalizado.MiViewHolder, position: Int) {
        val usuarioItem = listaItems[position]
        holder.setValores(usuarioItem)
        holder.filaEditar.setOnClickListener{
            val intent= Intent(context,FormActivity::class.java)
            intent.putExtra("id",usuarioItem.item_id)
            intent.putExtra("precio",usuarioItem.item_precio)
            intent.putExtra("producto",usuarioItem.item_nombre)
            intent.putExtra("stock",usuarioItem.item_stock)
            intent.putExtra("origen",usuarioItem.item_origen)
            intent.putExtra("destino",usuarioItem.item_destino)


            context.startActivity(intent)
        }

        holder.filaEliminar.setOnClickListener{
            eliminarItem?.invoke(usuarioItem)
        }

    }

    override fun getItemCount(): Int {
        return listaItems.size
    }
}

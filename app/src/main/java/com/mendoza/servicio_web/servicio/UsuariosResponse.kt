package com.mendoza.servicio_web.servicio

import com.google.gson.annotations.SerializedName
import com.mendoza.servicio_web.entidad.Item
import com.mendoza.servicio_web.entidad.Kardex

data class UsuariosResponse (
    @SerializedName("listaItems") var listaItems:ArrayList<Item>
)
package com.mendoza.servicio_web.servicio

import com.google.gson.annotations.SerializedName
import com.mendoza.servicio_web.entidad.Kardex

data class kardexResponse (
    @SerializedName("listaKardex") var listaKardex:ArrayList<Kardex>
)
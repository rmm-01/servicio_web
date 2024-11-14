package com.mendoza.servicio_web.entidad

import java.io.Serializable

data class Kardex (
    var id: Int,
    var info_item: String,
    var usuario: String,
    var fecha: String,
    var cantidad: Int,
    var cantidad_anterior: String,
    var tipo: String
): Serializable
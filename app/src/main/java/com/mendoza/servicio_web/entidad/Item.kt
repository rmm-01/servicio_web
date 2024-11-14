package com.mendoza.servicio_web.entidad

import java.io.Serializable

data class Item (
    var item_id:Int,
    var item_nombre:String,
    var item_precio:Double,
    var item_origen:String,
    var item_destino:String,
    var item_stock:Int,
): Serializable
package com.mendoza.servicio_web.servicio

interface IdCallback {
    fun onSuccess(userId: Int)
    fun onError(message: String)
}
const express = require('express')
const mysql = require('mysql')
const bodyParser = require('body-parser')

const app = express()
app.use(bodyParser.json())

const PUERTO = 3000

const conexion = mysql.createConnection(
    {
        host:'localhost',
        database:'servicios',
        user:'root',
        password:''
    }
)

app.listen(PUERTO, ()=>{
    console.log("Servidor corriendo en el puerto "+PUERTO)
})

conexion.connect(error => {
    if(error) throw error
    console.log("Conexión a la base de datos fue exitosa")
})

app.get("/", (req, res) =>{
    res.send('Web Service Funcionando...')
})

app.get("/usuarios", (req, res) =>{
    const query = "SELECT *  FROM usuarios;"
    conexion.query(query, (error, resultado) =>{
        if(error) return console.error(error.message)

        const objeto = {}
        if(resultado.length > 0){
            objeto.listaUsuarios = resultado
            res.json(objeto)
        }else{
            res.json("No hay registros")
        }
    })
})
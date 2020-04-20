package mx.edu.ittepic.ladm_u3_practica1_emmanuelhuizar

import android.database.sqlite.SQLiteException
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main3.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Main3Activity : AppCompatActivity() {
    val nombreBaseDatos = "ejemplo1"


    @RequiresApi(Build.VERSION_CODES.O)
    var fecha1=fechaActual()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        button2.setOnClickListener {
            insertarPersona()
            finish()
        }

        button3.setOnClickListener {
            finish()
        }
    }

    fun insertarPersona(){
        try{
            var basedatos=Basedatos(this,nombreBaseDatos,null,1)
            var insertar=basedatos.writableDatabase
            var SQL="INSERT INTO ACTIVIDAD VALUES(NULL,'${editText.text.toString()}','${editText2.text.toString()}','${fecha1}')"

            insertar.execSQL(SQL)
            mensaje("se inserto correctamente")
            insertar.close()
            basedatos.close()
            editText2.setText("")
            editText.setText("")
        }catch (error: SQLiteException){
            mensaje(error.message.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fechaActual():String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var fechaA = current.format(formatter)
        return fechaA
    }

    fun mensaje(mensaje:String){
        AlertDialog.Builder(this).setMessage(mensaje).show()
    }
}

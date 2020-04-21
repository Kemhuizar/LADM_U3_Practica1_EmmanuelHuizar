package mx.edu.ittepic.ladm_u3_practica1_emmanuelhuizar

import android.app.Dialog
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    val nombreBaseDatos = "ejemplo1"
    var listaID=ArrayList<String>()
    @RequiresApi(Build.VERSION_CODES.O)
    var fecha1=fechaActual()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CargarData()
        button5.setOnClickListener {
            var dialogo= Dialog(this)
            dialogo.setContentView(R.layout.agregar)

            var descripcion = dialogo.findViewById<EditText>(R.id.editText5)
            var fecha = dialogo.findViewById<EditText>(R.id.editText6)
            var enviar = dialogo.findViewById<Button>(R.id.button9)
            var cerrar = dialogo.findViewById<Button>(R.id.button10)

            enviar.setOnClickListener {
                try{
                    var basedatos=Basedatos(this,nombreBaseDatos,null,1)
                    var insertar=basedatos.writableDatabase
                    var SQL="INSERT INTO ACTIVIDAD VALUES(NULL,'${descripcion.text.toString()}','${fecha.text.toString()}','${fecha1}')"

                    insertar.execSQL(SQL)
                    mensaje("se inserto correctamente")
                    insertar.close()
                    basedatos.close()
                    descripcion.setText("")
                    fecha.setText("")
                }catch (error: SQLiteException){
                    mensaje(error.message.toString())
                }
                CargarData()
                dialogo.dismiss()
            }

            cerrar.setOnClickListener {
                dialogo.dismiss()
            }
            dialogo.show()
        }
    }
    private fun CargarData(){
        try{
            var baseDatos=Basedatos(this,nombreBaseDatos,null,1)
            var select=baseDatos.readableDatabase
            var SQL="SELECT * FROM ACTIVIDAD"

            var cursor=select.rawQuery(SQL,null)

        if(cursor.count>=0){
                var arreglo = ArrayList<String>()
                this.listaID=ArrayList<String>()
                cursor.moveToFirst()
                var cantidad=cursor.count-1
                (0..cantidad).forEach{
                    var data="Descripcion: ${cursor.getString(1)}\nFecha entrega:${cursor.getString(2)}"
                    arreglo.add(data)
                    listaID.add(cursor.getString(0))
                    cursor.moveToNext()
                }
                Lista.adapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arreglo)
                Lista.setOnItemClickListener { parent, view, position, id ->
                    AlertDialog.Builder(this).setTitle("ATENCION").setMessage("¿Que desea hacer con este ITEM?")
                        .setPositiveButton("Eliminar"){d, i->
                            EliminarID(listaID[position])
                        }
                        .setNeutralButton("Ver actividad"){d,i->
                            var otroActivity= Intent(this,Main2Activity::class.java)
                            otroActivity.putExtra("id",listaID[position])
                            startActivity(otroActivity)
                        }
                        .setNegativeButton("Cancelar"){d,i->}.show()
                }
            }
            select.close()
            baseDatos.close()
        }catch (error: SQLiteException){
            mensaje(error.message.toString())
        }
    }

    private fun EliminarID(id: String){
        try{
            var basedatos=Basedatos(this,nombreBaseDatos,null,1)
            var insertar=basedatos.writableDatabase
            var SQL="DELETE FROM ACTIVIDAD WHERE IDactividad=?"
            var parametros= arrayOf(id)
            insertar.execSQL(SQL,parametros)
            insertar.close()
            basedatos.close()
            CargarData()
        }catch (error:SQLiteException){
            mensaje(error.message.toString())
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun fechaActual():String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var fechaA = current.format(formatter)
        return fechaA
    }

    private fun mensaje(mensaje:String){
        AlertDialog.Builder(this).setMessage(mensaje).show()
    }
}

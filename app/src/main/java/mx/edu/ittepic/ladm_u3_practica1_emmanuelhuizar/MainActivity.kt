package mx.edu.ittepic.ladm_u3_practica1_emmanuelhuizar

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val nombreBaseDatos = "ejemplo1"
    var listaID=ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CargarData()
        floatingActionButton.setOnClickListener {
            var otroActivity= Intent(this,Main3Activity::class.java)
            startActivity(otroActivity)
        }
    }

    fun CargarData(){
        try{
            var baseDatos=Basedatos(this,nombreBaseDatos,null,1)
            var select=baseDatos.readableDatabase
            var SQL="SELECT * FROM ACTIVIDAD"

            var cursor=select.rawQuery(SQL,null)

            if(cursor.count>0){
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
                            EliminarPorID(listaID[position])
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

    fun mensaje(mensaje:String){
        AlertDialog.Builder(this).setMessage(mensaje).show()
    }

    fun EliminarPorID(id: String){
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
}

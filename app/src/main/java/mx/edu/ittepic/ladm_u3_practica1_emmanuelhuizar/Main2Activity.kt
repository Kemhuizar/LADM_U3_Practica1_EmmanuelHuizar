package mx.edu.ittepic.ladm_u3_practica1_emmanuelhuizar

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main3.*

class Main2Activity : AppCompatActivity() {
    var idActualizar =""
    val nombreBaseDatos = "ejemplo1"
    var listaID=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        var extras=intent.extras
        idActualizar=extras!!.getString("id").toString()

        try{
            var baseDatos=Basedatos(this,nombreBaseDatos,null,1)
            var select=baseDatos.readableDatabase
            var SQL="SELECT * FROM ACTIVIDAD WHERE IDactividad=?"
            var parametro= arrayOf(idActualizar)

            var cursor=select.rawQuery(SQL,parametro)
            if(cursor.moveToFirst()){
                //si hay resulatdp
                textView2.setText(cursor.getString(1))
                textView3.setText(cursor.getString(2))
                textView.setText(cursor.getString(3))
            }else{
                mensaje("No se encontro considendcia")
            }
            select.close()
            baseDatos.close()
        }catch (error: SQLiteException){
            mensaje(error.message.toString())
        }

        button.setOnClickListener {
            finish()
        }

        button4.setOnClickListener {

        }
    }

    fun Eliminar(id: String){
        try{
            var basedatos=Basedatos(this,nombreBaseDatos,null,1)
            var insertar=basedatos.writableDatabase
            var SQL="DELETE FROM ACTIVIDAD WHERE IDactividad=?"
            var parametros= arrayOf(id)

            insertar.execSQL(SQL,parametros)
            mensaje("se Elimino correctamente")
            insertar.close()
            basedatos.close()
        }catch (error:SQLiteException){
            mensaje(error.message.toString())
        }
    }


    fun mensaje(mensaje:String){
        AlertDialog.Builder(this).setMessage(mensaje).show()
    }


}

package mx.edu.ittepic.ladm_u3_practica1_emmanuelhuizar

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class Main2Activity : AppCompatActivity() {
    var idActualizar =""
    var bitmap : Bitmap?= null
    val nombreBaseDatos = "ejemplo1"
    var listaID=ArrayList<String>()
    val PHOTO = 1

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

        try{
            var data = ArrayList<ByteArray>()
            var baseDatos=Basedatos(this,nombreBaseDatos,null,1)

            var select=baseDatos.readableDatabase
            var SQL="SELECT * FROM EVIDENCIA WHERE IDactividad=?"
            var parametro= arrayOf(idActualizar)

            var cursor=select.rawQuery(SQL,parametro)
            /*var select=baseDatos.readableDatabase
            var columnas = arrayOf("Foto")
            var ID = arrayOf(idActualizar)
            var cursor = select.query("EVIDENCIA",columnas,"IDactividad=?",ID,null,null,null)*/

            if(cursor.moveToFirst()){
                do{
                    data.add(cursor.getBlob(2))
                }while (cursor.moveToNext())
            }else{
                mensaje("No se encontro considendcia")
            }
            select.close()
            baseDatos.close()
            imageView5.setImageBitmap(ByteArrayToBitmap(data[0]))
        }catch (error: SQLiteException){
            mensaje(error.message.toString())
        }

        button.setOnClickListener {
            finish()
        }

        button6.setOnClickListener {
            cargarImagen()
        }

        button4.setOnClickListener {

        }
    }

    private fun cargarImagen() {
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent,PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHOTO) {
            val selectedImage: Uri? = data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selectedImage)
            /*imageView5.setImageURI(selectedImage)
            insertarImagen(selectedImage)*/
            imageView5.setImageBitmap(bitmap)
            val bos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG, 2, bos)
            val bArray: ByteArray = bos.toByteArray()
            insertarImagen(bArray)
        }


    }

    private fun insertarImagen(Imagen:ByteArray){
        try{
            var bien:ByteArray=Imagen
            var basedatos=Basedatos(this,nombreBaseDatos,null,1)

            var insertar = basedatos.writableDatabase
            var datos= ContentValues()

            datos.put("IDactividad",idActualizar)
            datos.put("Foto",bien)


            insertar.insert("EVIDENCIA","IDevidencia",datos)

            /*var insertar=basedatos.writableDatabase
            var SQL="INSERT INTO EVIDENCIA VALUES(NULL,'${idActualizar}','${bien}')"
            insertar.execSQL(SQL)
            mensaje("se inserto correctamente")
            insertar.close()
            basedatos.close()*/
        }catch (error: SQLiteException){
            mensaje(error.message.toString())
        }
    }


    private fun mensaje(mensaje:String){
        AlertDialog.Builder(this).setMessage(mensaje).show()
    }
    private fun ByteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        val arrayInputStream = ByteArrayInputStream(byteArray)
        return BitmapFactory.decodeStream(arrayInputStream)
    }

}

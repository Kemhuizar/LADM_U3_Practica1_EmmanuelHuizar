package mx.edu.ittepic.ladm_u3_practica1_emmanuelhuizar

import android.content.Context
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class Basedatos(context:Context,nombreBaseDatos:String,cursor:SQLiteDatabase.CursorFactory?,versionBaseDatos:Int) : SQLiteOpenHelper(context,nombreBaseDatos,cursor,versionBaseDatos ){
    override fun onCreate(db: SQLiteDatabase?) {
        //se utiliza para contruir la estructura de tablas de SQLite
        try{
            db?.execSQL("CREATE TABLE PERSONA(ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR(200),DOMICILIO VARCHAR(500))")
        }catch (error:SQLiteException){
        }
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Upgrate = actualiza mayor = eSTRUCTURA
        //Update = Actualiza menos = data
    }
}
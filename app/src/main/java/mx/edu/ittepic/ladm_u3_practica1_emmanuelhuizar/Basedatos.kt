package mx.edu.ittepic.ladm_u3_practica1_emmanuelhuizar

import android.content.Context
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class Basedatos(context:Context,nombreBaseDatos:String,cursor:SQLiteDatabase.CursorFactory?,versionBaseDatos:Int) : SQLiteOpenHelper(context,nombreBaseDatos,cursor,versionBaseDatos ){
    override fun onCreate(db: SQLiteDatabase?) {
        try{
            db?.execSQL("CREATE TABLE ACTIVIDAD(IDactividad INTEGER PRIMARY KEY AUTOINCREMENT, Descripcion VARCHAR(500), FechaEntrega DATE,FechaCaptura DATE)")
            db?.execSQL("CREATE TABLE EVIDENCIA(IDevidencia INTEGER PRIMARY KEY AUTOINCREMENT,IDactividad INTEGER NOT NULL,Foto BLOB,FOREIGN KEY (ID_ACTIVIDAD) REFERENCES ACTIVIDAD(ID_ACTIVIDAD))")
        }catch (error:SQLiteException){
        }
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}
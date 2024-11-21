package com.example.easyway.cadastros;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Definições das colunas
    public static final String DATABASE_NAME = "easyway.db";
    public static final int DATABASE_VERSION = 3;

    public static final String TABLE_SERVICOS = "servicos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_ORIGEM = "origem";
    public static final String COLUMN_DESTINO = "destino";
    public static final String COLUMN_ORIGEM_LAT = "origem_lat";
    public static final String COLUMN_ORIGEM_LNG = "origem_lng";
    public static final String COLUMN_DESTINO_LAT = "destino_lat";
    public static final String COLUMN_DESTINO_LNG = "destino_lng";
    public static final String COLUMN_VALOR = "valor";

    // Definição de criação da tabela
    private static final String CREATE_TABLE_SERVICOS = "CREATE TABLE " + TABLE_SERVICOS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOME + " TEXT, " +
            COLUMN_ORIGEM + " TEXT, " +
            COLUMN_DESTINO + " TEXT, " +
            COLUMN_ORIGEM_LAT + " DOUBLE, " +
            COLUMN_ORIGEM_LNG + " DOUBLE, " +
            COLUMN_DESTINO_LAT + " DOUBLE, " +
            COLUMN_DESTINO_LNG + " DOUBLE, " +
            COLUMN_VALOR + " TEXT" + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SERVICOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_SERVICOS + " ADD COLUMN " + COLUMN_VALOR + " TEXT;");
        }
    }

    public void addServico(String nome, String origem, String destino,
                           double origemLat, double origemLng,
                           double destinoLat, double destinoLng,
                           String valor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, nome);
        values.put(COLUMN_ORIGEM, origem);
        values.put(COLUMN_DESTINO, destino);
        values.put(COLUMN_ORIGEM_LAT, origemLat);
        values.put(COLUMN_ORIGEM_LNG, origemLng);
        values.put(COLUMN_DESTINO_LAT, destinoLat);
        values.put(COLUMN_DESTINO_LNG, destinoLng);
        values.put(COLUMN_VALOR, valor);

        db.insert(TABLE_SERVICOS, null, values);
        db.close();
    }

    public Cursor getAllServicos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SERVICOS, null);
    }
}

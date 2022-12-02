package com.pronque.snake;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SnakeSQLiteOpenHelper extends SQLiteOpenHelper {
    // Nom de la base de données
    private static final String DATABASE_NAME = "snake.db";
    // Version de la base de données
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructeur de la classe SnakeSQLiteOpenHelper
     *
     * @param context Contexte de l'application
     */
    public SnakeSQLiteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Création de la table des scores
        String sqlCreateDatabaseScore = "CREATE TABLE leaderboard (idScore INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, score INTEGER)";
        sqLiteDatabase.execSQL(sqlCreateDatabaseScore);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Suppression de la table des scores
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "leaderboard");
        // Recréation de la table des scores
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Appelle la méthode onUpgrade
        onUpgrade(db, oldVersion, newVersion);
    }
}

package com.pronque.snake;

import android.database.Cursor;

/**
 * Classe qui représente un score
 */
public class Score {
    // Variables pour le nom du joueur et son score
    private int score;
    private String name;

    /**
     * Constructeur de la classe Score
     *
     * @param cursor curseur de la base de données
     */
    public Score(Cursor cursor) {
        score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
        name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
    }

    /**
     * @return le score du joueur
     */
    public int getScore() {
        return score;
    }

    /**
     * @return le nom du joueur
     */
    public String getName() {
        return name;
    }
}

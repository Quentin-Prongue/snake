package com.pronque.snake;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Classe qui gère le classement
 */
public class LeaderboardManager {
    // Variable pour la liste de scores
    private ArrayList<Score> scoresList;

    /**
     * Constructeur de la classe LeaderboardManager
     *
     * @param context Contexte de l'application
     */
    public LeaderboardManager(Context context) {
        scoresList = initScoresList(context);
    }

    /**
     * Méthode qui initialise la liste de scores
     *
     * @param context Contexte de l'application
     * @return La liste de scores
     */
    private ArrayList<Score> initScoresList(Context context) {
        // Création de la liste de scores
        ArrayList<Score> listScores = new ArrayList<>();
        // Variable pour gérer la base de données
        SnakeSQLiteOpenHelper helper = new SnakeSQLiteOpenHelper(context);
        // Récupère la base de données
        SQLiteDatabase db = helper.getReadableDatabase();

        // Requête pour récupérer les scores
        Cursor cursor = db.rawQuery("SELECT * FROM leaderboard", null);

        // Si la requête renvoie des résultats
        while (cursor.moveToNext()) {
            // Ajoute le score à la liste
            listScores.add(new Score(cursor));
        }

        // Ferme le curseur
        cursor.close();
        // Ferme la base de données
        db.close();
        return listScores;
    }

    /**
     * @return la taille de la liste de scores
     */
    public int getSize() {
        return scoresList.size();
    }

    /**
     * Méthode qui récupère la liste de scores triée
     */
    public ArrayList<Score> getSortedScoresList() {
        // Trie la liste de scores
        scoresList.sort((o1, o2) -> o2.getScore() - o1.getScore());
        return scoresList;
    }
}

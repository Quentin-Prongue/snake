package com.pronque.snake;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

/**
 * Classe de l'activité de fin de partie
 */
public class EndActivity extends AppCompatActivity {
    // Initialisation des éléments de l'interface
    private TableLayout tv_leaderboard;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        // Récupération des éléments de l'interface
        tv_leaderboard = findViewById(R.id.tb_leaderboard);
        toolbar = findViewById(R.id.toolbar);

        // Barre d'outils
        setSupportActionBar(toolbar);

        // Permet de récupérer les extras de l'intent
        Bundle extras = getIntent().getExtras();

        // Récupère le nom du joueur et le score
        String playerName = extras.getString("playerName");
        int score = extras.getInt("score");

        // Récupère la base de données
        SnakeSQLiteOpenHelper snakeSQLiteOpenHelper = new SnakeSQLiteOpenHelper(getApplicationContext());
        SQLiteDatabase db = snakeSQLiteOpenHelper.getWritableDatabase();

        // Création d'un objet ContentValues pour insérer les données
        ContentValues values = new ContentValues();
        values.put("name", playerName);
        values.put("score", score);
        // Insertion des données dans la base de données
        db.insert("leaderboard", null, values);

        // Ferme la base de données
        db.close();

        // Ce qui permet de gérer le classement
        LeaderboardManager leaderboardManager = new LeaderboardManager(getApplicationContext());

        // Récupère la liste de scores du classement triée
        ArrayList<Score> scoresList = leaderboardManager.getSortedScoresList();

        // Affiche le classement dans le tableau
        for (int i = 0; i < leaderboardManager.getSize(); i++) {
            // Créer un ligne de tableau
            TableRow tableRow = new TableRow(this);
            // Créer un TextView pour la position du joueur
            TextView tv_position = new TextView(this);
            // Créer un TextView pour le nom du joueur
            TextView tv_playerName = new TextView(this);
            // Créer un TextView pour le score du joueur
            TextView tv_score = new TextView(this);

            // Ajoute la position du joueur dans la TextView et centre le texte
            tv_position.setText(String.valueOf(i + 1));
            tv_position.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            tv_position.setTextSize(18);
            tv_position.setTextColor(Color.WHITE);

            // Ajoute le nom du joueur dans la TextView et centre le texte
            tv_playerName.setText(scoresList.get(i).getName());
            tv_playerName.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            tv_playerName.setTextSize(18);
            tv_playerName.setTextColor(Color.WHITE);

            // Ajoute le score du joueur dans la TextView et centre le texte
            tv_score.setText(String.valueOf(scoresList.get(i).getScore()));
            tv_score.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            tv_score.setTextSize(18);
            tv_score.setTextColor(Color.WHITE);

            // Teste les positions du joueur et change la couleur du texte
            if (i == 0) {
                // Le premier joueur est en or
                tv_position.setTextColor(Color.rgb(255, 215, 0));
                tv_playerName.setTextColor(Color.rgb(255, 215, 0));
                tv_score.setTextColor(Color.rgb(255, 215, 0));
                // Ajoute une couronne à côté du nom du joueur
                tv_playerName.setText(scoresList.get(i).getName() + " 👑");
            } else if (i == 1) {
                // Le deuxième joueur est en argent
                tv_position.setTextColor(Color.GRAY);
                tv_playerName.setTextColor(Color.GRAY);
                tv_score.setTextColor(Color.GRAY);
            } else if (i == 2) {
                // Le troisième joueur est en bronze
                tv_position.setTextColor(Color.rgb(205, 127, 50));
                tv_playerName.setTextColor(Color.rgb(205, 127, 50));
                tv_score.setTextColor(Color.rgb(205, 127, 50));
            }

            // Ajoute les TextView dans la ligne de tableau
            tableRow.addView(tv_position);
            tableRow.addView(tv_playerName);
            tableRow.addView(tv_score);

            // Ajoute la ligne de tableau dans le tableau
            tv_leaderboard.addView(tableRow);
        }
    }

    /**
     * Création du menu
     *
     * @param menu Menu
     * @return Booléen
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.end_menu, menu);
        return true;
    }

    /**
     * Gestion des clics sur les éléments du menu
     *
     * @param item Elément du menu
     * @return Booléen
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Rejouer
            case R.id.action_restart:
                // Redémarre le jeu
                Intent intent = new Intent(EndActivity.this, StartActivity.class);
                startActivity(intent);
                return true;
            // Quitter
            case R.id.action_quit:
                // Quitte l'application
                finishAffinity();
                return true;
            // Par défaut
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
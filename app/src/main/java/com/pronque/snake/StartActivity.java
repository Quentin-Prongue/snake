package com.pronque.snake;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.slider.Slider;

import java.util.Locale;

/**
 * Classe de l'activité de démarrage
 */
public class StartActivity extends AppCompatActivity {
    // Initialisation des variables
    private Toolbar TOOLBAR_main;
    private FrameLayout FL_settings;
    private FrameLayout FL_themes;
    private Button BT_start;
    private Button BT_settings_cancel;
    private Button BT_settings_apply;
    private Button BT_themes_cancel;
    private Button BT_themes_apply;
    private EditText ET_player_name;
    private Slider SL_apple_number;
    private Slider SL_snake_speed;
    private RadioGroup RG_themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Barre d'outils
        TOOLBAR_main = findViewById(R.id.toolbar_main);
        setSupportActionBar(TOOLBAR_main);

        // Récupération des éléments de l'interface
        FL_settings = findViewById(R.id.fl_settings);
        FL_themes = findViewById(R.id.fl_themes);
        BT_start = findViewById(R.id.bt_start);
        BT_settings_cancel = findViewById(R.id.bt_settings_cancel);
        BT_settings_apply = findViewById(R.id.bt_settings_apply);
        BT_themes_cancel = findViewById(R.id.bt_themes_cancel);
        BT_themes_apply = findViewById(R.id.bt_themes_apply);
        ET_player_name = findViewById(R.id.et_player_name);
        SL_apple_number = findViewById(R.id.sl_apple_number);
        SL_snake_speed = findViewById(R.id.sl_snake_speed);
        RG_themes = findViewById(R.id.rg_theme_choice);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Désactive le bouton au démarrage
        BT_start.setEnabled(false);

        // Quand le texte est modifié dans le champ de texte du nom du joueur
        ET_player_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Active le bouton si le champ de texte n'est pas vide
                BT_start.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Au clic sur le bouton annuler, ferme la fenêtre de paramètres
        BT_settings_cancel.setOnClickListener(v -> {
            // Réinitialise les paramètres
            SL_apple_number.setValue(2);
            SL_snake_speed.setValue(12);
            // Ferme la fenêtre de paramètres
            FL_settings.setVisibility(View.GONE);
            // Affiche le bouton
            BT_start.setVisibility(View.VISIBLE);
            // Affiche le champ pour le nom du joueur
            ET_player_name.setVisibility(View.VISIBLE);
            // Change le titre de la barre d'outils
            TOOLBAR_main.setTitle(R.string.app_name);
        });

        // Au clic sur le bouton appliquer, ferme la fenêtre de paramètres
        BT_settings_apply.setOnClickListener(v -> {
            // Affiche un message de confirmation
            Toast.makeText(StartActivity.this, "Paramètres appliqués", Toast.LENGTH_SHORT).show();
            // Ferme la fenêtre de paramètres
            FL_settings.setVisibility(View.GONE);
            // Affiche le bouton
            BT_start.setVisibility(View.VISIBLE);
            // Affiche le champ pour le nom du joueur
            ET_player_name.setVisibility(View.VISIBLE);
            // Change le titre de la barre d'outils
            TOOLBAR_main.setTitle(R.string.app_name);
        });

        // Au clic sur le bouton annuler, ferme la fenêtre de thèmes
        BT_themes_cancel.setOnClickListener(v -> {
            // Réinitialise le thème
            RG_themes.check(R.id.rb_theme_vert);
            // Ferme la fenêtre de thèmes
            FL_themes.setVisibility(View.GONE);
            // Affiche le bouton
            BT_start.setVisibility(View.VISIBLE);
            // Affiche le champ pour le nom du joueur
            ET_player_name.setVisibility(View.VISIBLE);
            // Change le titre de la barre d'outils
            TOOLBAR_main.setTitle(R.string.app_name);
        });

        // Au clic sur le bouton appliquer, ferme la fenêtre de thèmes
        BT_themes_apply.setOnClickListener(v -> {
            // Affiche un message de confirmation
            Toast.makeText(StartActivity.this, "Thème appliqué", Toast.LENGTH_SHORT).show();
            // Ferme la fenêtre de thèmes
            FL_themes.setVisibility(View.GONE);
            // Affiche le bouton
            BT_start.setVisibility(View.VISIBLE);
            // Affiche le champ pour le nom du joueur
            ET_player_name.setVisibility(View.VISIBLE);
            // Change le titre de la barre d'outils
            TOOLBAR_main.setTitle(R.string.app_name);
        });

        // Au clic sur le bouton démarrer, lance l'activité de jeu
        BT_start.setOnClickListener(v -> {
            // Récupère le nom du joueur
            String playerName = ET_player_name.getText().toString();
            // Récupère le nombre de pommes
            int appleNumber = (int) SL_apple_number.getValue();
            // Récupère la vitesse du serpent
            int snakeSpeed = (int) SL_snake_speed.getValue();
            // Récupère le thème
            String theme = ((RadioButton) findViewById(RG_themes.getCheckedRadioButtonId())).getText().toString().toLowerCase(Locale.ROOT);

            // Création de l'intent
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            intent.putExtra("playerName", playerName);
            intent.putExtra("appleNumber", appleNumber);
            intent.putExtra("snakeSpeed", snakeSpeed);
            intent.putExtra("theme", theme);
            startActivity(intent);

        });
    }

    /**
     * Création du menu
     *
     * @param menu Menu
     * @return Booléen
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Au clic sur un élément du menu
     *
     * @param item Elément du menu
     * @return Booléen
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Paramètres
            case R.id.action_settings:
                // Change le titre de la barre d'outils
                TOOLBAR_main.setTitle("Paramètres");
                // Affiche la fenêtre de paramètres
                FL_settings.setVisibility(View.VISIBLE);
                // Cache le layout des thèmes
                FL_themes.setVisibility(View.GONE);
                // Cache le bouton
                BT_start.setVisibility(View.GONE);
                // Cache le champ pour le nom du joueur
                ET_player_name.setVisibility(View.GONE);
                return true;
            // Thèmes
            case R.id.action_themes:
                // Change le titre de la barre d'outils
                TOOLBAR_main.setTitle("Thèmes");
                // Affiche la fenêtre des thèmes
                FL_themes.setVisibility(View.VISIBLE);
                // Cache le layout des paramètres
                FL_settings.setVisibility(View.GONE);
                // Cache le bouton
                BT_start.setVisibility(View.GONE);
                // Cache le champ pour le nom du joueur
                ET_player_name.setVisibility(View.GONE);
                return true;
            // Par défaut
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
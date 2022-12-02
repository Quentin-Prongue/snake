package com.pronque.snake;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Classe qui gère l'affichage du jeu et la direction du serpent
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // Variable pour le handler
    private final Handler handler = new Handler();
    // Variable pour l'état du jeu
    public GameState gameStatus = GameState.START;
    // Variables pour les éléments du jeu
    private SnakeEngineView snakeEngineView;
    private TextView statusText;
    private TextView scoreText;

    // Variable pour le capteur
    private SensorManager sensorManager;

    // Variable pour le nom du joueur
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des éléments du jeu
        snakeEngineView = findViewById(R.id.snake_engine_view);
        statusText = findViewById(R.id.game_status);
        scoreText = findViewById(R.id.game_score);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Récupère le capteur gravity du téléphone
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_GAME);

        // Permet de récupérer les extras de l'intent
        Bundle extras = getIntent().getExtras();

        // Récupère le nom du joueur
        playerName = extras.getString("playerName");

        // Change le nombre de pommes
        snakeEngineView.apple_number = extras.getInt("appleNumber");

        // Change la vitesse du serpent
        snakeEngineView.snakeSpeed = extras.getInt("snakeSpeed");

        // Change le thème du jeu
        snakeEngineView.theme = extras.getString("theme");

        // Initialisation du jeu
        snakeEngineView.initGame();

        // Affichage du score
        scoreText.setText("Score : " + snakeEngineView.getScore());

        // Met le status du jeu à début
        changeGameStatus(GameState.START);

        // Au clic sur l'écran, on lance le jeu
        snakeEngineView.setOnClickListener(v -> {
            if (gameStatus == GameState.PLAYING) {
                changeGameStatus(GameState.PAUSE);
            } else {
                changeGameStatus(GameState.PLAYING);
            }
        });
    }

    /**
     * Permet de changer l'état du jeu
     *
     * @param status nouvel état du jeu
     */
    private void changeGameStatus(GameState status) {
        // Ancien état du jeu
        GameState prevStatus = gameStatus;

        // Affiche le nouveau status
        statusText.setVisibility(View.VISIBLE);
        gameStatus = status;

        // Switch sur l'état du jeu
        switch (gameStatus) {
            // Si le jeu au début
            case START:
                snakeEngineView.newGame();
                statusText.setText(R.string.game_status_start);
                break;
            // Si le jeu est terminé
            case END:
                // Stoppe le jeu
                handler.removeCallbacksAndMessages(null);
                // Stoppe le listener du capteur
                statusText.setText(R.string.game_status_end);
                // Attend 1 seconde
                handler.postDelayed(() -> {
                    // Appelle l'activité de fin de jeu
                    Intent intent = new Intent(MainActivity.this, EndActivity.class);
                    intent.putExtra("playerName", playerName);
                    intent.putExtra("score", snakeEngineView.getScore());
                    startActivity(intent);
                }, 500);
                break;
            // Si le jeu est en pause
            case PAUSE:
                statusText.setText(R.string.game_status_pause);
                break;
            // Si le jeu est en cours
            case PLAYING:
                // Si le jeu était terminé, on le relance
                if (prevStatus == GameState.END) {
                    snakeEngineView.newGame();
                }
                // Sinon on lance une partie
                startGame();
                statusText.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * Permet de lancer le jeu
     */
    private void startGame() {
        // Initialise le délai entre chaque frame
        final int delay = 1000 / SnakeEngineView.FPS;
        // Thread pour le jeu
        new Thread(() -> {
            int count = 0;
            // Tant que le jeu est en cours
            while (!snakeEngineView.isGameOver() && gameStatus != GameState.PAUSE) {
                try {
                    // On attend le délai
                    Thread.sleep(delay);
                    // Si le compteur modulo la vitesse est égal à 0, on met à jour le jeu
                    if (count % snakeEngineView.snakeSpeed == 0) {
                        // On teste le prochain point
                        snakeEngineView.testNextPoint();
                        // Redessine le jeu
                        handler.post(() -> snakeEngineView.invalidate());
                    }
                    // On incrémente le compteur
                    count++;
                } catch (InterruptedException ignored) {
                }
            }
            // Si le jeu est terminé, on met le status à END
            if (snakeEngineView.isGameOver()) {
                handler.post(() -> changeGameStatus(GameState.END));
            }
        }).start();
    }

    /**
     * Au moment où le capteur change
     *
     * @param sensorEvent Événement du capteur
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Récupère les valeurs du capteur (x = axe horizontal, y = axe vertical)
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];

        // Transforme les valeurs de direction en pourcentage
        int xPercent = (int) (x * 10);
        int yPercent = (int) (y * 10);

        // Met à jour le score du jeu
        scoreText.setText("Score : " + snakeEngineView.getScore());

        // Direction du serpent en fonction des valeurs du capteur en format landscape
        // Si x est positif, on va à en haut
        if (xPercent > 20) {
            snakeEngineView.setDirection(Direction.UP);
            // Sinon on va en bas
        } else if (xPercent < -20) {
            snakeEngineView.setDirection(Direction.DOWN);
        }

        // Si y est positif, on va à gauche
        if (yPercent > 20) {
            snakeEngineView.setDirection(Direction.LEFT);
            // Sinon on va à droite
        } else if (yPercent < -20) {
            snakeEngineView.setDirection(Direction.RIGHT);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
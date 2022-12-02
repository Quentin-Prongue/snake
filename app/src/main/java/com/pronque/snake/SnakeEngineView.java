package com.pronque.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Classe qui gère le fonctionnement et le dessin du jeu
 */
public class SnakeEngineView extends View {
    // Constante pour les FPS et la vitesse du serpent (plus c'est petit, plus c'est rapide)
    public static final int FPS = 60;
    public static final int SNAKE_SPEED = 12;
    // Variables pour les dimensions de la map
    private static final int MAP_SIZE_X = 45;
    private static final int MAP_SIZE_Y = 32;
    // Variables pour la position de départ du serpent
    private static final int SNAKE_START_X = 20;
    private static final int SNAKE_START_Y = 16;
    // Variables pour le serpent
    private final Point[][] points = new Point[MAP_SIZE_X][MAP_SIZE_Y];
    private final ArrayList<Point> snake = new ArrayList<>();
    // Variable pour le dessin
    private final Paint paint = new Paint();
    // Variable pour le nombre de pommes
    public int apple_number = 2;

    // Variable pour la vitesse du serpent
    public int snakeSpeed = SNAKE_SPEED;

    // Variable pour le theme
    public String theme = "vert";
    public Direction currentSnakeDirection;
    // Variable pour le score
    private int score;
    // Variable pour la fin de partie
    private boolean gameOver;

    /**
     * Constructeur de la classe SnakeEngineView
     *
     * @param context Contexte de l'application
     */
    public SnakeEngineView(Context context) {
        super(context);
    }

    /**
     * Constructeur de la classe SnakeEngineView
     * @param context Contexte de l'application
     * @param attrs Attributs de l'application
     */
    public SnakeEngineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructeur de la classe SnakeEngineView
     * @param context Contexte de l'application
     * @param attrs Attributs de l'application
     * @param defStyleAttr Style de l'application
     */
    public SnakeEngineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Initialise le jeu
     * Met le game over à false, le score à 0 et la direction du serpent en bas
     */
    public void initGame() {
        gameOver = false;
        score = 0;
        currentSnakeDirection = Direction.DOWN;
    }

    /**
     * Nouvelle partie
     * Initialise le jeu, la map et met à jour le score
     */
    public void newGame() {
        initGame();
        initMap();
        updateScore();
    }

    /**
     * Initialisation de la map
     */
    private void initMap() {
        // Initialise la map
        for (int x = 0; x < MAP_SIZE_X; x++) {
            for (int y = 0; y < MAP_SIZE_Y; y++) {
                points[x][y] = new Point(x, y);
            }
        }

        // Détermine les murs de la map horizontaux
        for (int x = 0; x < MAP_SIZE_X; x++) {
            // Les murs du haut
            points[x][0].type = PointType.WALL;
            // Les murs du bas
            points[x][MAP_SIZE_Y - 1].type = PointType.WALL;
        }
        // Détermine les murs de la map verticaux
        for (int y = 0; y < MAP_SIZE_Y; y++) {
            // Les murs de gauche
            points[0][y].type = PointType.WALL;
            // Les murs de droite
            points[MAP_SIZE_X - 1][y].type = PointType.WALL;
        }

        // Efface le serpent
        snake.clear();

        // Détermine les points du serpent
        for (int i = 0; i < 3; i++) {
            Point point = getPoint(SNAKE_START_X + i, SNAKE_START_Y);
            point.type = PointType.SNAKE;
            snake.add(point);
        }

        // Génère le nombre de pomme souhaité aléatoirement sur la map
        for (int i = 0; i < apple_number; i++) {
            randomApple();
        }
    }

    /**
     * Génère une pomme aléatoirement
     */
    private void randomApple() {
        // Génère un nombre aléatoire
        Random random = new Random();
        int x = random.nextInt(MAP_SIZE_X - 2) + 1;
        int y = random.nextInt(MAP_SIZE_Y - 2) + 1;

        // Vérifie si le point est un mur ou un serpent
        Point point = getPoint(x, y);
        if (point.type == PointType.WALL || point.type == PointType.SNAKE) {
            // Si c'est le cas, on relance la fonction
            randomApple();
        } else {
            // Sinon, on met le point en pomme
            point.type = PointType.APPLE;
        }
    }

    /**
     * Obtenir un point de la map
     */
    private Point getPoint(int x, int y) {
        return points[x][y];
    }

    /**
     * Teste le prochain point
     */
    public void testNextPoint() {
        // Récupère le premier point du serpent (tête) et le prochain point
        Point snakeHead = snake.get(0);
        Point nextPoint = getNextPoint(snakeHead);

        // Switch sur le type du point
        switch (nextPoint.type) {
            // Si le prochain point est vide on déplace le serpent et on supprime le dernier point
            case EMPTY:
                nextPoint.type = PointType.SNAKE;
                snake.add(0, nextPoint);
                snake.get(snake.size() - 1).type = PointType.EMPTY;
                snake.remove(snake.size() - 1);
                break;
            // Si le prochain point est une pomme on déplace le serpent et on génère une nouvelle pomme et on met à jour le score
            case APPLE:
                nextPoint.type = PointType.SNAKE;
                snake.add(0, nextPoint);
                randomApple();
                updateScore();
                // Augmente la vitesse du serpent tous les 3 points
                if (score % 3 == 0) {
                    snakeSpeed--;
                }
                break;
            // Si le prochain point est le serpent on arrête le jeu
            case SNAKE:
                // Si le prochain point est un mur on arrête le jeu
            case WALL:
                gameOver = true;
                break;
        }
    }

    /**
     * Met à jour le score
     */
    public void updateScore() {
        score = snake.size() - 3;
    }

    /**
     * @return le score actuel
     */
    public int getScore() {
        return score;
    }

    /**
     * Met à jour la direction du serpent
     *
     * @param dir Direction
     */
    public void setDirection(Direction dir) {
        // Si la direction est à gauche ou à droite et que le serpent va vers la gauche ou la droite on ne fait rien
        if ((dir == Direction.LEFT || dir == Direction.RIGHT) &&
                (currentSnakeDirection == Direction.LEFT || currentSnakeDirection == Direction.RIGHT)) {
            return;
        }
        // Si la direction est en haut ou en bas et que le serpent va en haut ou en bas on ne fait rien
        if ((dir == Direction.UP || dir == Direction.DOWN) &&
                (currentSnakeDirection == Direction.UP || currentSnakeDirection == Direction.DOWN)) {
            return;
        }

        // Sinon on met à jour la direction du serpent
        currentSnakeDirection = dir;
    }

    /**
     * Obtenir le prochain point
     *
     * @param point Point
     * @return Point
     */
    private Point getNextPoint(Point point) {
        // Récupère les coordonnées du point (x = colonnes, y = lignes)
        int x = point.x;
        int y = point.y;

        // Switch sur la direction en format paysage
        switch (currentSnakeDirection) {
            // Si la direction est en haut on décrémente y
            case UP:
                y = (y == 0) ? (MAP_SIZE_Y - 1) : (y - 1);
                break;
            // Si la direction est en bas on incrémente y
            case DOWN:
                y = (y == (MAP_SIZE_Y - 1)) ? 0 : (y + 1);
                break;
            // Si la direction est à gauche on décrémente x
            case LEFT:
                x = (x == 0) ? (MAP_SIZE_X - 1) : (x - 1);
                break;
            // Si la direction est à droite on incrémente x
            case RIGHT:
                x = (x == (MAP_SIZE_X - 1)) ? 0 : (x + 1);
                break;
        }
        // Retourne le point
        return getPoint(x, y);
    }

    /**
     * Teste si la partie est terminée
     *
     * @return boolean true si la partie est terminée
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Dessine le jeu
     *
     * @param canvas Canvas sur lequel dessiner
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Boucle sur les points de la map
        for (int y = 0; y < MAP_SIZE_Y; y++) {
            for (int x = 0; x < MAP_SIZE_X; x++) {
                // Dessine le point sur le canvas
                int left = x * MAP_SIZE_X;
                int right = left + MAP_SIZE_X;
                int top = y * MAP_SIZE_Y;
                int bottom = top + MAP_SIZE_Y;

                // Switch sur le type du point
                switch (getPoint(x, y).type) {
                    // Si le point est une pomme on dessine un carré rouge
                    case APPLE:
                        paint.setColor(Color.RED);
                        break;
                    // Si le point est le serpent on dessine un carré bleu
                    case SNAKE:
                        canvas.drawRect(left, top, right, bottom, paint);
                        paint.setColor(Color.rgb(66, 111, 227));
                        left += MAP_SIZE_X;
                        right -= MAP_SIZE_X;
                        top += MAP_SIZE_Y;
                        bottom -= MAP_SIZE_Y;
                        break;
                    // Si le point est vide
                    case EMPTY:
                        switch (theme) {
                            // Thème vert
                            case "vert":
                                paint.setColor(Color.rgb(162, 209, 73));
                                break;
                            // Thème bleu
                            case "bleu":
                                paint.setColor(Color.rgb(163, 197, 245));
                                break;
                            // Thème volcan
                            case "volcan":
                                paint.setColor(Color.rgb(110, 53, 53));
                                break;
                            // Thème neige
                            case "neige":
                                paint.setColor(Color.rgb(226, 236, 237));
                                break;
                            case "nuit":
                                paint.setColor(Color.rgb(68, 62, 76));
                                break;
                        }
                        break;
                    // Si le point est un mur
                    case WALL:
                        switch (theme) {
                            // Thème vert
                            case "vert":
                                paint.setColor(Color.rgb(87, 138, 52));
                                break;
                            // Thème bleu
                            case "bleu":
                                paint.setColor(Color.rgb(39, 91, 165));
                                break;
                            // Thème volcan
                            case "volcan":
                                paint.setColor(Color.rgb(163, 62, 62));
                                break;
                            // Thème neige
                            case "neige":
                                paint.setColor(Color.rgb(135, 159, 161));
                                break;
                            // Thème nuit
                            case "nuit":
                                paint.setColor(Color.rgb(44, 39, 48));
                                break;
                        }
                        break;
                }
                // Dessine le point
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
    }
}

package com.pronque.snake;

/**
 * Classe représentant un point de la carte
 */
public class Point {
    // Coordonnées du point
    public final int x, y;

    // Type du point
    public PointType type;

    /**
     * Constructeur de la classe Point
     *
     * @param x Coordonnée x du point
     * @param y Coordonnée y du point
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = PointType.EMPTY;
    }
}

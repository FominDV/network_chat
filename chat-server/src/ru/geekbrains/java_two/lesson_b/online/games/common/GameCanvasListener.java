package ru.geekbrains.java_two.lesson_b.online.games.common;

import java.awt.*;

public interface GameCanvasListener {
    void onDrawFrame(GameCanvas canvas, Graphics g, float deltaTime);
}

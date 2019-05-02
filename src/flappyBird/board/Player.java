package flappyBird.board;


import atariCore.BaseObject;
import atariCore.FileManager;
import flappyBird.FlappyBird;
import flappyBird.menu.Splash;

import javax.swing.*;
import java.awt.*;

import static atariCore.Helper.*;
import static flappyBird.FlappyHelper.*;

public class Player extends BaseObject {

    private int score;
    private String name;
    public int lastScore;
    public boolean start;
    private JPanel panel;
    FlappyBird flappyBird;

    public Player(String Name, JPanel panel, FlappyBird flappyBird) {
        super(10, 10, null);
        this.name = Name;
        this.panel = panel;
        this.flappyBird = flappyBird;
        lastScore = 0;
        score = 0;
        start = true;
    }

    public String getName() {
        return name;
    }

    public void die() {
        FileManager.addNewScoreToLeaderboards(pathFile + "Leaderboards.txt", name, score, 0);
        running = false;

        if (!AIMode) {

            new Splash();
        } else {

            lastScore = 0;
            setScore(0);

            flappyBird.myGeneration.generateNewGeneration();
            flappyBird.initialize();

        }
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void increaseScore(int add) {
        score += add;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void tick() {
    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.setFont(font);
        String strScore = String.valueOf(score);
        g.drawString(name, 10, 40);
        g.drawString(strScore, 10, 80);
    }
}


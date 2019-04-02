

package arkanoid.board;

import arkanoid.arkHelper;
import arkanoid.capsule.Capsule;
import atariCore.BaseObject;
import atariCore.Handler;
import atariCore.Sounds;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static arkanoid.arkHelper.*;

public class Paddle extends BaseObject {

    public CopyOnWriteArrayList<Capsule> capsules;
    public Handler handler;
    public boolean sticky, laser = true;
    public boolean expand = false;
    int normalImageIdx = 0;
    Player player;

    public Paddle(int xPosition, int yPosition, Image image, float velX, float velY, Handler handler , Player player) {

        super(xPosition, yPosition, image, velX, 0);
        this.handler = handler;
        this.player = player;
        capsules = new CopyOnWriteArrayList<>();
    }

    public void tick() {

        if(laser) {
            if(!expand)
            updateLaserImage();

        }
        else if(!expand) {
            updateNormalImage();
        }



        x += velX;

        collision();
        clamp();
    }

    private void updateLaserImage() {
        img = paddleWeapon[normalImageIdx++];
        normalImageIdx %= 3;
    }

    public void splitBall() {

        for (BaseObject o : handler.getObject()) {

            if (o instanceof Ball) {

                Ball ball = (Ball)o;
                ball.setVelX(0);

                Ball newBallL = new Ball(ball.getX() , ball.getY() , ball.getImg() , xSpeed , ball.getVelY() , handler , player);
                Ball newBallR = new Ball(ball.getX() , ball.getY() , ball.getImg() , -xSpeed , ball.getVelY(), handler , player);

                handler.addObject(newBallL);
                handler.addObject(newBallR);

                break;
            }
        }
    }

    public void updateLaser() {

        laser = true;
        setImageWidth(paddleWeapon[0].getWidth(null));
        setImageHeight(paddleWeapon[0].getHeight(null));
    }

    public void expand() {
        expand = true;
        if(!laser)
          this.img =  paddleExpanded;
        else
            this.img = paddleExpandedWeapon;
        this.setImageHeight(paddleExpanded.getHeight(null));
        this.setImageWidth(paddleExpanded.getWidth(null));
    }

    private void updateCapsules() {

        for (Capsule c : capsules) {

            c.effect(this);
            if (!c.hit()) {
                capsules.remove(c);
            }
        }
    }

    private void updateNormalImage() {

        img = paddle[normalImageIdx++];
        normalImageIdx %= 3;
    }

    public void hitLaser() {

        updateCapsules();
        Bullet bulletL = new Bullet(x , y , bullet , handler);
        Bullet bulletR = new Bullet(x + getImageWidth() - getImageWidth() * 3 / 100 , y , bullet , handler);

        handler.addObject(bulletL);
        handler.addObject(bulletR);


    }

    public void speedUp() {

        xSpeed = Math.min(4 , xSpeed + 0.5f);
        ySpeed = Math.min(4 , ySpeed + 0.5f);
    }

    public void speedDown() {

        xSpeed = Math.max(1 , xSpeed - 0.5f);
        ySpeed = Math.max(1 , xSpeed - 0.5f);
    }

    private void collision() {
        boolean checkIfBricksHeight1 = false , checkIfBricksHeight2 = false;
        for (BaseObject o : handler.getObject()) {

            if (o instanceof Capsule) {

                if (o.getRectangle().intersects(getRectangle())) {

                    capsules.add(((Capsule) o));
                    updateCapsules();
                    handler.removeObject(o);
                }
            }

            if (o instanceof Ball) {
                if (o.getRectangle().intersects(getRectangle()) && o.getY() + o.getImageHeight() / 2 < y) {

                    if (sticky) {

                        o.setVelX(0);
                        o.setVelY(0);
                        continue;
                    }

                    o.setVelY(ySpeed * -1);

                    int dir = (o.getVelX() >= 0) ? 1 : -1;

                    if ((o.getVelX() > 0 && getVelX() >= 0) || (o.getVelX() < 0 && getVelX() <= 0)) {
                        o.setVelX(dir * getNewVx(o.getX() + o.getImageWidth() / 2));
                    } else {
                        o.setVelX(-dir * getNewVx(o.getX() + o.getImageWidth() / 2));
                    }

                    //System.out.println(o.getVelX());
                }
                if(o.getY() >= screenHeight)
                {
                    player.setLives(player.getLives()-1);
                    o.setX((this.x+this.getImageWidth() - this.getImageWidth()/2));
                    o.setY(INIT_BALL_Y);
                    o.setVelX(xSpeed);
                    o.setVelY(ySpeed);
                }
            }
            if( o instanceof Brick) {

                if (o.getY() >= 0) {
                    if (o.getY() >= INIT_BRICKS_HEIGHT) {
                        checkIfBricksHeight1 = true;
                    }
                }
                else
                {
                    checkIfBricksHeight2 = true;
                }
            }
        }
        if(checkIfBricksHeight1 == false && checkIfBricksHeight2 == true)
        {
            for (BaseObject o : handler.getObject())
            {
                if( o instanceof Brick)
                {
                    ((Brick)o).moveDown();
                }
            }
        }

    }

    public float getNewVx(float currX) {

        float newVX = xSpeed;

        float q1 = x + getImageWidth() / 5;
        float q2 = q1 + getImageWidth() / 5;
        float q3 = q2 + getImageWidth() / 5;
        float q4 = q3 + getImageWidth() / 5;
        float q5 = q4 + getImageWidth() / 5;

        //System.out.println(currX);
        //System.out.println(q1 + " " + q2 + " " + q3 + " " + q4 + " " + q5);

        if (currX < q1 || currX >= q5) newVX = xSpeed + xSpeed * 25 / 100 ;
        else if (currX < q2) newVX = xSpeed + xSpeed * 10 / 100;
        else if ( currX < q3) newVX = xSpeed;
        else if (currX < q4) newVX = xSpeed;
        else newVX = xSpeed + xSpeed * 10 / 100;

        return newVX;
    }

    public Player getPlayer() {
        return player;
    }

    public void render(Graphics g) {

        g.drawImage(super.img, (int)super.x, (int)super.y, null);
    }
    public void makeAcidBall() {

        for(BaseObject o : handler.getObject()) {

            if(o instanceof Ball) {

                ((Ball)o).makeAcid();
            }
        }
    }

    public void shrink() {

        img = paddle[0];
    }
}



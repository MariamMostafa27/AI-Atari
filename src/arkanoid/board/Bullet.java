package arkanoid.board;

import arkanoid.capsule.Capsule;
import atariCore.BaseObject;
import atariCore.Handler;

import java.awt.*;
import java.util.ArrayList;

import static arkanoid.arkHelper.paddleSpeed;

public class Bullet extends BaseObject {

    Handler handler;
    public Bullet(int x, int y, Image img , Handler handler) {
        super(x, y, img);
        this.handler = handler;
    }

    @Override
    public void tick() {

        y -= paddleSpeed;

        collision();
    }

    private void collision() {

        int calcScore = 0;

        ArrayList<BaseObject> object = new ArrayList<>();
        boolean dead = false;

        for (BaseObject o : handler.getObject()) {
            if ((o instanceof Brick || o instanceof Enemy)) {
                if (o.getRectangle().intersects(getRectangle())) {

                    dead = true;
                    calcScore++;
                    ((Brick) o).hit();

                    int pow = ((Brick) o).getPower();
                    if (pow != 0) {

                        object.add(o);
                    }
                }
            }

            if(o == this) continue;
            object.add(o);
        }

        if (!dead) object.add(this);

        handler.object = object;
    }

    @Override
    public void render(Graphics g) {

    }
}

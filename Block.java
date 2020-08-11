import biuoop.DrawSurface;

import java.awt.Color;

/**
 * A Block object, which has a rectangular shape a color , and a hit counter.
 *
 * @author : mohammed Elesawi.
 */

public class Block implements Collidable, Sprite {

    private Rectangle shape;
    private java.awt.Color color;
    private int hitsCounter;

    /**
     * the constructor.
     *
     * @param rectangle   : the block's shape.
     * @param color       : the blocks color.
     * @param hitsCounter : marks the number of hits needed to get an "X" mark.
     */
    public Block(Rectangle rectangle, java.awt.Color color, int hitsCounter) {
        this.shape = rectangle;
        this.color = color;
        this.hitsCounter = hitsCounter;
    }

    /**
     * @return returning the blocks shape.
     */
    public Rectangle getCollisionRectangle() {
        return this.shape;
    }

    /**
     * Notify the block that we collided with it at a certain collisionPoint with a given velocity , if collided with
     * it from below or above , then we will turn the vertical direction , if collided with it from left or right ,
     * then we will turn the horizontal direction , and if collided with it from the it's angles , then we will turn
     * the vertical and horizontal directions.
     *
     * @param collisionPoint  the point where the object has hit the ball.
     * @param currentVelocity the object's current velocity.
     * @return the new velocity depending on where the object has hit the block.
     */
    public Velocity hit(Point collisionPoint, Velocity currentVelocity) {
        // blocks lines.
        Line upLine = this.shape.getUpLine();
        Line lowLine = this.shape.getLowLine();
        Line leftLine = this.shape.getLeftLine();
        Line rightLine = this.shape.getRightLine();


        double updatedDx = 0, updatedDy = 0;
        double collisionX = collisionPoint.getX(), collisionY = collisionPoint.getY();

        // if  we have hit the block's angles.

        if (collisionX == leftLine.start().getX() && collisionY == upLine.start().getY()
                || collisionX == leftLine.start().getX() && collisionY == lowLine.start().getY()
                || collisionX == rightLine.start().getX() && collisionY == upLine.start().getY()
                || collisionX == rightLine.start().getX() && collisionY == lowLine.start().getY()) {

            updatedDx = currentVelocity.getdx() * -1;
            updatedDy = currentVelocity.getdY() * -1;

            // if we have hit the block from right or left.
        } else if (collisionY == upLine.start().getY() || collisionY == lowLine.start().getY()) {
            updatedDx = currentVelocity.getdx();
            updatedDy = -1 * (currentVelocity.getdY());
        } else if (collisionX == rightLine.start().getX() || collisionX == leftLine.start().getX()) {
            updatedDx = -1 * (currentVelocity.getdx());
            updatedDy = currentVelocity.getdY();
        }
        // if we have hit the block from above or below.

        //decrease the number on the block when collision occurs.
        if (this.hitsCounter >= 1) {
            this.hitsCounter--;
        }

        return new Velocity(updatedDx, updatedDy);

    }

    /**
     * notify the block that time has passed.
     */

    public void timePassed() {
    }

    /**
     * drawing the block on a given DrawSurface.
     *
     * @param surface : the surface to draw the block on.
     */

    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillRectangle((int) this.shape.getUpperLeft().getX(), (int) this.shape.getUpperLeft().getY(),
                (int) this.shape.getWidth(), (int) this.shape.getHeight());

        surface.setColor(Color.black);
        //drawing the frame

        surface.drawRectangle((int) this.shape.getUpperLeft().getX(), (int) this.shape.getUpperLeft().getY(),
                (int) this.shape.getWidth(), (int) this.shape.getHeight());

        //drawing the hitsCounter on the middle of the block

        Point mid = this.shape.getLeftLine().middle();
        int y = (int) mid.getY();
        int x = (int) mid.getX() + (int) this.shape.getWidth() / 2;

        //drawing the hitsCounter.
        if (this.hitsCounter != 0) {
            surface.drawText(x, y, Integer.toString(this.hitsCounter), 17);

            //if the hitsCounter decreases to 0 , we will draw an "X" .
        } else {
            surface.drawText(x, y, ("X"), 15);
        }

    }

    /**
     * adding the block to specified game , (the block is also a collidable and a sprite).
     *
     * @param g : our "Arkanoid" game.
     */

    public void addToGame(Game g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

}

/**
 * @author Mohamad Elesawi <esawi442@gmail.com>
 * @since 2019-03-14
 **/

import biuoop.DrawSurface;

import java.awt.Color;

import java.util.Random;

/**
 * class Ball.
 **/

public class Ball implements Sprite {

    private Point center;
    private int radius;
    private Color color;
    private Velocity velocity;
    private GameEnvironment environment;

    /**
     * Constructor.
     * given the x and y coordinates of the center point of the ball, radius and color.
     *
     * @param x     : ball's center x coordinate point.
     * @param y     : ball's center y coordinate point.
     * @param r     :  ball's radius.
     * @param color : ball's color.
     */
    public Ball(double x, double y, int r, Color color) {
        this.center = new Point(x, y);
        this.radius = r;
        this.color = color;
    }


    /**
     * @return x-coordinate of the ball's center point.
     */
    public double getX() {
        return this.center.getX();
    }

    /**
     * @return y-coordinate of the ball's center point.
     */
    public double getY() {
        return this.center.getY();
    }

    /**
     * setting the ball's Game environment.
     *
     * @param gameEnvironment : the game environment.
     */

    public void setGameEnvironment(GameEnvironment gameEnvironment) {
        this.environment = gameEnvironment;
    }

    /**
     * @return radius of the ball.
     */

    public int getSize() {
        return this.radius;
    }

    /**
     * @return ball's color.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * draw the ball on the given DrawSurface.
     *
     * @param surface : draws surface to draw our ball on it.
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.getColor());
        surface.fillCircle((int) this.getX(), (int) this.getY(), this.getSize());
    }

    /**
     * set the balls velocity while the velocity is given .
     *
     * @param v : the velocity.
     */

    public void setVelocity(Velocity v) {
        this.velocity = v;
    }

    /**
     * set the balls velocity while given the change in position of the `x` and the `y` coordinates.
     *
     * @param dx : change in position of the x .
     * @param dy : change in position of the y .
     */
    public void setVelocity(double dx, double dy) {
        this.velocity = new Velocity(dx, dy);
    }

    /**
     * @return ball's velocity.
     */
    public Velocity getVelocity() {
        return this.velocity;
    }


    /**
     * moving the the ball one step according to it's velocity, while taking ball's frame exit into calculation.
     */

    public void moveOneStep() {
        Point p = this.getVelocity().applyToPoint(this.center);
        Line trajectory = new Line(this.center, p);
        CollisionInfo col = this.environment.getClosestCollision(trajectory);
        if (col != null) {
            Point collisionPoint = col.collisionPoint();
            double epsilon = 0.001;

            Line upLine = col.collidable().getCollisionRectangle().getUpLine();
            Line lowLine = col.collidable().getCollisionRectangle().getLowLine();
            Line leftLine = col.collidable().getCollisionRectangle().getLeftLine();
            Line rightLine = col.collidable().getCollisionRectangle().getRightLine();

            if (upLine.ifPointOnLine(collisionPoint)) {
                this.center = new Point(collisionPoint.getX(), collisionPoint.getY() - epsilon);
            }
            if (lowLine.ifPointOnLine(collisionPoint)) {
                this.center = new Point(collisionPoint.getX(), collisionPoint.getY() + epsilon);
            }
            if (rightLine.ifPointOnLine(collisionPoint)) {
                this.center = new Point(collisionPoint.getX() + epsilon, collisionPoint.getY());
            }
            if (leftLine.ifPointOnLine(collisionPoint)) {
                this.center = new Point(collisionPoint.getX() - epsilon, collisionPoint.getY());
            }
            //updating the velocity.
            this.velocity = col.collidable().hit(collisionPoint, this.velocity);
        } else {
            this.center = this.getVelocity().applyToPoint(this.center);
        }
    }


    /**
     * function to generate a random ball according to the balls bounds , the flag is used to so wont have to create
     * another function that does almost the same thing but adds the needed dx and dy ( for example ,
     * we used it different flags in the MultipleFramesBouncingBallsAnimation class then in the
     * BouncingBallAnimation class.
     *
     * @param radius : the Ball-to-be radius.
     * @param boundX : sets the bound of the x's random point max value
     * @param boundY : sets the bound of the y's random point max value
     * @param addToX : the change in the x coordinates
     * @param addToY : the change in the y coordinate
     * @param flag   :  sets wither to update the coordinates according to the function who sent it.
     * @return a new Ball.
     */


    public static Ball generateRandomBall(int radius, int boundX, int boundY, int addToX, int addToY, int flag) {
        Random rand = new Random();
        Color color = new Color(rand.nextInt(0xFFFFFF)); // random color
        int x = rand.nextInt(boundX) + 1; // get integer in range 1 - X bound
        int y = rand.nextInt(boundY) + 1; // get integer in range 1 - Y bound
        // returns the new ball
        if (flag == 1) {
            return new Ball((double) x, (double) y, radius, color);
        } else { // returns the new ball with updated X and Y according to the velocity
            return new Ball((double) x + addToX, (double) y + addToY, radius, color);
        }
    }

    /**
     * sorting the given array of balls , using bubble sort.
     *
     * @param ballsArray : the array to sort.
     * @return the same array but sorted from the highest radius to the lowest.
     */
    public static Ball[] sortBallArray(Ball[] ballsArray) {
        for (int k = 0; k < ballsArray.length; k++) {
            for (int j = 0; j < ballsArray.length - k - 1; j++) {
                // comparing radius'es
                if (ballsArray[k].getSize() < ballsArray[k + 1].getSize()) {
                    Ball tempBall = ballsArray[k];
                    ballsArray[k] = ballsArray[k + 1];
                    ballsArray[k + 1] = tempBall;
                }
            }
        }
        return ballsArray;
    }

    /**
     * setting the velocity of each ball in the array, the first ball will always be the lragest and its already
     * set to have the slowest speed.
     *
     * @param ballsArray :  the array to set its balls velocity.
     * @param flag       :      decides which way to set the velocity,
     *                   (both ways will are working in a way that the larger the ball the slower its speed and
     *                   the smallest the ball, the faster its speed
     * @return the array with its balls velocity set.
     */
    public static Ball[] setArrayVelocity(Ball[] ballsArray, int flag) {
        Random random = new Random();
        for (int k = 1; k < ballsArray.length; k++) {
            // balls with same size will get the same velocity.
            if (ballsArray[k].getSize() == ballsArray[k - 1].getSize()) {
                ballsArray[k].setVelocity(ballsArray[k - 1].getVelocity());
                continue;
            }
            // balls with radius bigger then 50 will have the same velocity
            if (ballsArray[k].getSize() > 50) {
                ballsArray[k].setVelocity(2, 2);
                continue;
            }
            if (flag == 0) {
                int dy = random.nextInt(12) + (int) ballsArray[k - 1].getVelocity().getdx() + 1;
                //symmetrical change in y and x axes.
                ballsArray[k].setVelocity(dy, dy);
            } else if (flag == 1) {
                if (ballsArray[k].getSize() <= 50) {
                    //symmetrical change in y and x .
                    int dx = 150 / ballsArray[k].getSize();
                    ballsArray[k].setVelocity(dx, dx);
                }
            }
        }
        return ballsArray;
    }

    /**
     * notifying the ball that time has passed.
     */

    public void timePassed() {
        moveOneStep();
    }

    /**
     * adding the ball to the our game.
     *
     * @param g : our game.
     */

    public void addToGame(Game g) {
        g.addSprite(this);
    }

}
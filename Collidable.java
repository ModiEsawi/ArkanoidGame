/**
 * The Collidable interface.
 * Things that objects can collide with.
 *
 * @author : mohammed Elesawi.
 */
public interface Collidable {
    /**
     * @return returning the shape of the collidable object.
     */
    Rectangle getCollisionRectangle();

    /**
     * letting the object know that we collided with it at a certain collisionPoint with a certain velocity.
     *
     * @param collisionPoint  : the point where the collision occurs.
     * @param currentVelocity : the object's current velocity.
     * @return : the updated velocity after collision depending on which part of the collidable shape the collision had
     * happened.
     */

    Velocity hit(Point collisionPoint, Velocity currentVelocity);
}
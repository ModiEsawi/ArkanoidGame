import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.KeyboardSensor;

import java.awt.Color;
import java.util.Random;


/**
 * The Game class.
 * in charge of the game animation and contains the collection of sprites and collaidables.
 *
 * @author : mohammed Elesawi.
 */

public class Game {
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private GUI gui;

    /**
     * initializing the game environment and sprites.
     */

    public Game() {
        this.environment = new GameEnvironment();
        this.sprites = new SpriteCollection();
    }

    /**
     * adding a collidable object to the game's environment.
     *
     * @param c : the collidable that needs to be added.
     */

    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * adding a sprite object to the sprite collection.
     *
     * @param s : the sprite object that needs to be added.
     */

    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * Initializing the game by creating the blocks , the paddle and the two balls , and adding them to the game.
     */

    public void initialize() {
        Random rand = new Random();
        Velocity ballsVelocity = new Velocity(5, 5);
        Ball[] twoBallsArray = new Ball[2];
        int surroundingBlocksWidth = 30, totalBlockRows = 6, blocksInFirstRow = 12, eachBlockWidth = 50;
        int eachBlockHeight = 30, startingYlocation = 145, paddleWidth = 145, paddleHeight = 30, hitsCounter = 0;
        int surfaceHeight = 800, surfaceWidth = 800, drawX = 0, topBlocksHits = 2, otheBlockHits = 1;
        double paddleMove = 16;

        Point paddleUpperleft = new Point(205, surfaceHeight - surroundingBlocksWidth - paddleHeight);

        for (int i = 0; i < 2; i++) {
            Ball ball = new Ball(100 * (i + 1), 300 * (i + 1), 8, Color.white);
            ball.setVelocity(ballsVelocity);
            twoBallsArray[i] = ball;
        }
        for (int i = 0; i < twoBallsArray.length; i++) {
            twoBallsArray[i].setGameEnvironment(this.environment);
            twoBallsArray[i].addToGame(this);
        }
        this.gui = new GUI("Arkanoid Game", surfaceWidth, surfaceHeight);
        KeyboardSensor keyboard = this.gui.getKeyboardSensor();
        for (int i = 0; i < totalBlockRows; blocksInFirstRow--, i++) {
            Color randomColor = new Color(rand.nextInt(0xFFFFFF));
            for (int j = 0; j < blocksInFirstRow; j++) {

                Point upperLeft = new Point(surfaceWidth - (eachBlockWidth * (j + 1)) - surroundingBlocksWidth
                        , startingYlocation + (eachBlockHeight * i));


                if (i != 0) {
                    Block block = new Block(new Rectangle(upperLeft, eachBlockWidth, eachBlockHeight), randomColor,
                            otheBlockHits);

                    block.addToGame(this);

                } else {
                    Block block = new Block(new Rectangle(upperLeft, eachBlockWidth, eachBlockHeight), randomColor,
                            topBlocksHits);

                    block.addToGame(this);

                }
            }
        }
        /* creating the blocks that surround the surface according to screen lengths , adding them to the game
          and also draw an "X" mark in their middle. */

        Block topBlock = new Block(new Rectangle(new Point(0, 0), surfaceWidth, surroundingBlocksWidth),
                Color.GRAY, drawX);
        topBlock.addToGame(this);

        Block bottomBlock = new Block(new Rectangle(new Point(surroundingBlocksWidth,
                surfaceHeight - surroundingBlocksWidth), surfaceWidth - surroundingBlocksWidth * 2,
                surroundingBlocksWidth), Color.GRAY, drawX);
        bottomBlock.addToGame(this);

        Block leftBlock = new Block(new Rectangle(new Point(0, surroundingBlocksWidth), surroundingBlocksWidth
                , surfaceHeight - surroundingBlocksWidth), Color.GRAY, drawX);
        leftBlock.addToGame(this);

        Block rightBlock = new Block(new Rectangle(new Point(surfaceWidth - surroundingBlocksWidth,
                surroundingBlocksWidth), surroundingBlocksWidth, surfaceHeight - surroundingBlocksWidth),
                Color.GRAY, drawX);
        rightBlock.addToGame(this);

        Paddle paddle = new Paddle(new Rectangle(paddleUpperleft, paddleWidth, paddleHeight), Color.YELLOW, keyboard,
                paddleMove, surroundingBlocksWidth, surfaceWidth);
        paddle.addToGame(this);

    }

    /**
     * running the game and and starting the animation.
     */

    public void run() {
        biuoop.Sleeper sleeper = new biuoop.Sleeper();
        int framesPerSecond = 60;
        int millisecondsPerFrame = 1000 / framesPerSecond;

        while (true) {
            long startTime = System.currentTimeMillis();
            Color backgroundColor = new Color(0.092f, 0.092f, 0.432f);
            DrawSurface d = gui.getDrawSurface();
            d.setColor(backgroundColor);
            // drawing the background.
            d.fillRectangle(0, 0, 800, 800);
            this.sprites.drawAllOn(d);
            gui.show(d);
            this.sprites.notifyAllTimePassed();

            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
    }
}
package com.classicgames.myapplication.data.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the pure Snake mechanics. The model only touches Android via the
 * record store, which no-ops when MyApplication is absent (unit-test mode), so the
 * gameplay can be exercised on the plain JVM.
 */
public class SnakeModelTest {

    private static final int RADIUS = 10;
    private static final int STEP = RADIUS * 2;   // distance moved per tick
    private static final int SIZE = 200;          // surface width/height

    private SnakeModel game;

    @Before
    public void setUp() {
        // mapSize 2 (medium), no obstacles
        game = new SnakeModel(2, RADIUS, SIZE, SIZE, false);
        game.start();
    }

    @Test
    public void startInitialisesState() {
        assertEquals(0, game.getScore());
        assertEquals(3, game.getSnakeBodySize());
        assertEquals(600, game.getCurrentSpeed());
        assertEquals(10, game.getCurrentSpeedPercentage());
        assertFalse(game.isGoldenPointActive());
    }

    @Test
    public void moveRightAdvancesHeadByOneStep() {
        int x = game.getSnakeHead().getPositionX();
        int y = game.getSnakeHead().getPositionY();
        game.moveSnake();
        assertEquals(x + STEP, game.getSnakeHead().getPositionX());
        assertEquals(y, game.getSnakeHead().getPositionY());
    }

    @Test
    public void growIncreasesSizeAndScore() {
        game.growSnake();
        assertEquals(4, game.getSnakeBodySize());
        assertEquals(1, game.getScore());
    }

    @Test
    public void cannotReverseDirection() {
        int x = game.getSnakeHead().getPositionX();
        game.changeDirection(100, 0, 50, 50); // swipe left while moving right
        game.moveSnake();
        // Still moving right: head advanced, not retreated.
        assertEquals(x + STEP, game.getSnakeHead().getPositionX());
    }

    @Test
    public void canTurnPerpendicular() {
        int y = game.getSnakeHead().getPositionY();
        game.changeDirection(50, 50, 0, 100); // swipe down while moving right
        game.moveSnake();
        assertEquals(y + STEP, game.getSnakeHead().getPositionY());
    }

    @Test
    public void cannotChangeDirectionTwiceBeforeMoving() {
        game.changeDirection(50, 50, 0, 100); // turn down (consumes the turn)
        int y = game.getSnakeHead().getPositionY();
        game.changeDirection(0, 100, 50, 50); // try to turn right immediately -> ignored
        game.moveSnake();
        // Still going down.
        assertEquals(y + STEP, game.getSnakeHead().getPositionY());
    }

    @Test
    public void runningIntoWallEndsGame() {
        assertFalse(game.isGameOver());
        int guard = 0;
        while (!game.isGameOver() && guard++ < 100) {
            game.moveSnake(); // straight right, into the right wall
        }
        assertTrue(game.isGameOver());
    }

    @Test
    public void pointCollisionIsDetectedAtPointPosition() {
        SnakePiece onPoint = new SnakePiece(
                game.getPoint().getPositionX(), game.getPoint().getPositionY());
        assertTrue(game.pointCollision(onPoint));

        SnakePiece elsewhere = new SnakePiece(
                game.getPoint().getPositionX() + STEP, game.getPoint().getPositionY() + STEP);
        assertFalse(game.pointCollision(elsewhere));
    }

    @Test
    public void speedIncreasesAfterFivePoints() {
        for (int i = 0; i < 5; i++) game.growSnake();
        assertTrue(game.shouldSpeedIncrease());
        game.increaseSpeed();
        assertEquals(650, game.getCurrentSpeed());
        assertEquals(30, game.getCurrentSpeedPercentage());
    }

    @Test
    public void noSpeedIncreaseBeforeThreshold() {
        for (int i = 0; i < 4; i++) game.growSnake();
        assertFalse(game.shouldSpeedIncrease());
    }

    @Test
    public void goldenPointStartsInactiveAndCaughtAddsFive() {
        assertFalse(game.isGoldenPointActive());
        game.goldenPointCaught();
        assertEquals(5, game.getScore());
        assertFalse(game.isGoldenPointActive());
    }

    @Test
    public void obstaclesNeverTrapACell() {
        // Regenerate the obstacle course many times; a point must never be able to
        // spawn in an unreachable cell or a one-exit dead-end.
        for (int trial = 0; trial < 30; trial++) {
            SnakeModel obstacleGame = new SnakeModel(1, RADIUS, 400, 400, true);
            obstacleGame.start();
            assertFalse("no obstacles were placed", obstacleGame.getObstacles().isEmpty());
            assertTrue("obstacles left an unreachable cell or a one-exit dead-end",
                    boardIsPlayable(obstacleGame, RADIUS, 400, 400));
        }
    }

    /**
     * Independent verification (does not call the model's own checker): treating
     * obstacles as walls, every empty cell must have at least two open neighbours
     * and be reachable from the snake's head.
     */
    private boolean boardIsPlayable(SnakeModel game, int radius, int width, int height) {
        int cell = radius * 2;
        int cols = width / cell;
        int rows = height / cell;
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        boolean[][] blocked = new boolean[cols][rows];
        int blockedCount = 0;
        for (SnakePiece o : game.getObstacles()) {
            int c = (o.getPositionX() - radius) / cell;
            int r = (o.getPositionY() - radius) / cell;
            if (c >= 0 && c < cols && r >= 0 && r < rows && !blocked[c][r]) {
                blocked[c][r] = true;
                blockedCount++;
            }
        }

        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                if (blocked[c][r]) continue;
                int open = 0;
                for (int[] d : dirs) {
                    int nc = c + d[0], nr = r + d[1];
                    if (nc >= 0 && nc < cols && nr >= 0 && nr < rows && !blocked[nc][nr]) open++;
                }
                if (open < 2) return false;
            }
        }

        SnakePiece head = game.getSnakeHead();
        int startC = (head.getPositionX() - radius) / cell;
        int startR = (head.getPositionY() - radius) / cell;
        if (startC < 0 || startC >= cols || startR < 0 || startR >= rows || blocked[startC][startR])
            return true;

        boolean[][] seen = new boolean[cols][rows];
        java.util.Deque<int[]> stack = new java.util.ArrayDeque<>();
        stack.push(new int[]{startC, startR});
        seen[startC][startR] = true;
        int reached = 0;
        while (!stack.isEmpty()) {
            int[] cur = stack.pop();
            reached++;
            for (int[] d : dirs) {
                int nc = cur[0] + d[0], nr = cur[1] + d[1];
                if (nc >= 0 && nc < cols && nr >= 0 && nr < rows && !blocked[nc][nr] && !seen[nc][nr]) {
                    seen[nc][nr] = true;
                    stack.push(new int[]{nc, nr});
                }
            }
        }
        return reached == cols * rows - blockedCount;
    }
}

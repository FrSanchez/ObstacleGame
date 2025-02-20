package com.sanchezparralabs.obstacle.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sanchezparralabs.obstacle.assets.AssetPaths;
import com.sanchezparralabs.obstacle.config.GameConfig;
import com.sanchezparralabs.obstacle.entity.Obstacle;
import com.sanchezparralabs.obstacle.entity.Player;
import com.sanchezparralabs.obstacle.util.GdxUtils;
import com.sanchezparralabs.obstacle.util.ViewportUtils;
import com.sanchezparralabs.obstacle.util.debug.DebugCameraController;

public class GameScreen implements Screen {

    private static final Logger log = new Logger(GameScreen.class.getName(), Logger.DEBUG);

    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private Viewport hudViewport;

    private SpriteBatch batch;
    private BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();

    private Viewport viewport;
    private ShapeRenderer renderer;

    private Player player;
    private final Array<Obstacle> obstacles = new Array<>();
    private float obstacleTime;
    private float scoreTimer;

    private int score = 0;
    private int lives = GameConfig.LIVES_START;

    private DebugCameraController debugCameraController;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal(AssetPaths.UI_FONT));

        player = new Player();

        // calculate position
        float startPlayerX = GameConfig.WORLD_CENTER_X;
        float startPlayerY = 1;

        player.setPosition(startPlayerX, startPlayerY);

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
    }

    @Override
    public void render(float delta) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        // update world
        update(delta);

        GdxUtils.clearScreen();;

        //render ui/hud
        renderUI();

        // render debug graphics
        renderDebug();
    }

    private void update(float delta) {
        updatePlayer(delta);
        updateObstacles(delta);
        updateScore(delta);
        if (isPlayerCollidingWithObstacle()) {
            log.debug("Collision detected");
            lives--;
        }
    }

    private void updateScore(float delta) {
        scoreTimer += delta;

        if (scoreTimer >= GameConfig.SCORE_MAX_TIME) {
            scoreTimer = 0.0f;
            score += MathUtils.random(1, 5);
        }
    }

    private boolean isPlayerCollidingWithObstacle() {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.isPlayerColliding(player)) {
                return true;
            }
        }

        return false;
    }

    private void updateObstacles(float delta) {
        for (Obstacle obstacle : obstacles) {
            obstacle.update();
        }

        createNewObstacle(delta);
    }

    private void createNewObstacle(float delta) {
        obstacleTime += delta;

        if (obstacleTime > GameConfig.OBSTACLE_SPAWN_TIME) {
            Obstacle obstacle = new Obstacle();
            float obstacleX = MathUtils.random(0, GameConfig.WORLD_WIDTH - obstacle.getWidth());
            float obstacleY = GameConfig.WORLD_HEIGHT;
            obstacle.setPosition(obstacleX, obstacleY);
            obstacles.add(obstacle);
            obstacleTime = 0;
        }
    }

    private void updatePlayer(float delta) {
        player.update();
        blockPlayerFromLeavingTheWorld();
    }

    private void blockPlayerFromLeavingTheWorld() {
        float playerX = MathUtils.clamp(player.getX(), player.getWidth() / 2, GameConfig.WORLD_WIDTH - player.getWidth() / 2);

        player.setPosition(playerX, player.getY());
    }

    private void renderUI() {
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        String livesText = "LIVES: " + lives;
        String scoreText = "SCORE: " + score;

        layout.setText(font, livesText);
        font.draw(batch, livesText, 20, GameConfig.HUD_HEIGHT - layout.height);

        layout.setText(font, scoreText);
        font.draw(batch, scoreText, GameConfig.HUD_WIDTH - layout.width - 20, GameConfig.HUD_HEIGHT - layout.height);

        batch.end();
    }

    private void renderDebug() {
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();

        renderer.end();
        ViewportUtils.drawGrid(viewport, renderer);
    }


    private void drawDebug() {
        player.drawDebug(renderer);
        for(Obstacle obstacle : obstacles) {
            obstacle.drawDebug(renderer);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        font.dispose();
    }
}

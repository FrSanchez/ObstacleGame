package com.sanchezparralabs.obstacle.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sanchezparralabs.obstacle.config.GameConfig;
import com.sanchezparralabs.obstacle.entity.Player;
import com.sanchezparralabs.obstacle.util.GdxUtils;
import com.sanchezparralabs.obstacle.util.ViewportUtils;
import com.sanchezparralabs.obstacle.util.debug.DebugCameraController;

public class GameScreen implements Screen {

    private static final Logger log = new Logger(GameScreen.class.getName(), Logger.DEBUG);

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private Player player;
    private DebugCameraController debugCameraController;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

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
        renderDebug();
    }

    private void update(float delta) {
        updatePlayer(delta);
    }

    private void updatePlayer(float delta) {
        log.debug("Player position = " + player.getX() + ", " + player.getY());
        player.update();
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
    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h, true);
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
    }
}

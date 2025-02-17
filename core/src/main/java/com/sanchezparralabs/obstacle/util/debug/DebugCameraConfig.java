package com.sanchezparralabs.obstacle.util.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Logger;

public class DebugCameraConfig {
    private static final Logger log = new Logger(DebugCameraController.class.getName(), Logger.DEBUG);
    // constants

    private static final String MAX_ZOOM_IN = "maxZoomIn";
    private static final String MAX_ZOOM_OUT = "maxZoomOut";
    private static final String MOVE_SPEED = "moveSpeed";
    private static final String ZOOM_SPEED = "zoomSpeed";

    private static final String LEFT_KEY = "leftKey";
    private static final String RIGHT_KEY = "rightKey";
    private static final String UP_KEY = "upKey";
    private static final String DOWN_KEY = "downKey";

    private static final String ZOOM_INKEY = "downKey";



    private static final int DEFAULT_LEFT_KEY = Input.Keys.A;
    private static final int DEFAULT_RIGHT_KEY = Input.Keys.D;
    private static final int DEFAULT_UP_KEY = Input.Keys.W;
    private static final int DEFAULT_DOWN_KEY = Input.Keys.S;

    private static final int DEFAULT_ZOOM_IN_KEY = Input.Keys.COMMA;
    private static final int DEFAULT_ZOOM_OUT_KEY = Input.Keys.PERIOD;

    private static final int DEFAULT_RESET_KEY = Input.Keys.BACKSPACE;
    private static final int DEFAULT_LOG_KEY = Input.Keys.ENTER;

    private static final float DEFAULT_ZOOM_SPEED = 2.0f; // units per second
    private static final float DEFAULT_MAX_ZOOM_IN = 0.20f;
    private static final float DEFAULT_MAX_ZOOM_OUT = 30f;

    private static final float DEFAULT_MOVE_SPEED = 20.0f; // units per second

    private static final String FILE_PATH = "debug/debugCameraConfig.json";

    // attributes
    private float maxZoomIn;
    private float maxZoomOut;
    private float moveSpeed;
    private float zoomSpeed;

    private int leftKey;
    private int rightKey;
    private int upKey;
    private int downKey;

    private int zoomInKey;
    private int zoomOutKey;

    private int resetKey;
    private int logKey;

    private FileHandle fileHandle;

    public DebugCameraConfig() {
        init();
    }

    private void init() {
        fileHandle = Gdx.files.internal(FILE_PATH);

        if (fileHandle.exists()) {
            load();
        } else {
            log.info("Using defaults file does not exist " + FILE_PATH);
            setupDefaults();
        }
    }

    private void setupDefaults() {
        maxZoomIn = DEFAULT_MAX_ZOOM_IN;
        maxZoomOut = DEFAULT_MAX_ZOOM_OUT;
        moveSpeed = DEFAULT_MOVE_SPEED;
        zoomSpeed = DEFAULT_ZOOM_SPEED;

        leftKey = DEFAULT_LEFT_KEY;
        rightKey = DEFAULT_RIGHT_KEY;
        upKey = DEFAULT_UP_KEY;
        downKey = DEFAULT_DOWN_KEY;

        zoomInKey = DEFAULT_ZOOM_IN_KEY;
        zoomOutKey = DEFAULT_ZOOM_OUT_KEY;

        resetKey = DEFAULT_RESET_KEY;
        logKey = DEFAULT_LOG_KEY;
    }

    private void load() {
        try { // try with resources
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(fileHandle);
            maxZoomIn = root.getFloat("maxZoomIn", DEFAULT_MAX_ZOOM_IN);
            maxZoomOut = root.getFloat("maxZoomOut", DEFAULT_MAX_ZOOM_OUT);
        } catch (Exception e) {
            log.error("Error loading " + FILE_PATH + " " + e.getMessage());
            setupDefaults();
        }
    }
}

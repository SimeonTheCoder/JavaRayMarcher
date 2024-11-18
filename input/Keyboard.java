package input;

import math.Vec3;
import rendering.Window;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
    private static final float CAMERA_SPEED = 0.1f;

    private final Vec3 playerCamera;
    private final Window window;

    public Keyboard (Vec3 playerCamera, Window window) {
        this.playerCamera = playerCamera;
        this.window = window;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w' -> playerCamera.z += CAMERA_SPEED;
            case 's' -> playerCamera.z -= CAMERA_SPEED;
            case 'a' -> playerCamera.x -= CAMERA_SPEED;
            case 'd' -> playerCamera.x += CAMERA_SPEED;
            case 'q' -> playerCamera.y -= CAMERA_SPEED;
            case 'e' -> playerCamera.y += CAMERA_SPEED;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        window.blocked = false;
    }
}

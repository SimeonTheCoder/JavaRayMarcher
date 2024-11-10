package input;

import math.Vec3;
import rendering.RenderingSettings;
import rendering.Window;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
    private static final float CAMERA_SPEED = 0.1f;
    private static final int RESOLUTION_DROP = 32;

    private Vec3 playerCamera;
    private Window window;

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
            case 'w':
                playerCamera.z += CAMERA_SPEED;
//                window.refreshImage();

                RenderingSettings.RESOLUTION_SCALING = RESOLUTION_DROP;

                if(!window.blocked) {
                    window.refreshImage();
                }

                window.blocked = true;

                break;
            case 's':
                playerCamera.z -= CAMERA_SPEED;
//                window.refreshImage();

                RenderingSettings.RESOLUTION_SCALING = RESOLUTION_DROP;

                if(!window.blocked) {
                    window.refreshImage();
                }

                window.blocked = true;

                break;

            case 'a':
                playerCamera.x -= CAMERA_SPEED;
//                window.refreshImage();

                RenderingSettings.RESOLUTION_SCALING = RESOLUTION_DROP;

                if(!window.blocked) {
                    window.refreshImage();
                }

                window.blocked = true;

                break;
            case 'd':
                playerCamera.x += CAMERA_SPEED;
//                window.refreshImage();

                RenderingSettings.RESOLUTION_SCALING = RESOLUTION_DROP;

                if(!window.blocked) {
                    window.refreshImage();
                }

                window.blocked = true;

                break;

            case 'q':
                playerCamera.y -= CAMERA_SPEED;
//                window.refreshImage();

                RenderingSettings.RESOLUTION_SCALING = RESOLUTION_DROP;

                if(!window.blocked) {
                    window.refreshImage();
                }

                window.blocked = true;

                break;
            case 'e':
                playerCamera.y += CAMERA_SPEED;
//                window.refreshImage();

                RenderingSettings.RESOLUTION_SCALING = RESOLUTION_DROP;

                if(!window.blocked) {
                    window.refreshImage();
                }

                window.blocked = true;

                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        window.blocked = false;
    }
}

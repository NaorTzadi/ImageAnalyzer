package org.example;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.example.Main.screenShotsFiles;

public class GlobalScreen {
    public void shootAndSaveScreenShot() {
        //TODO: make an example of this method so to add to useful methods.
        System.out.println("press the 'SHIFT' button to take a screen shot.");
        System.out.println("press the 'ESCAPE' button to cancel the action.");

        final boolean[] cancelled = {false};
        Logger logger = Logger.getLogger(org.jnativehook.GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        try {
            org.jnativehook.GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            System.err.println("There was a problem registering the native hook.");
            return;
        }

        org.jnativehook.GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT) {
                    try {
                        Robot robot = new Robot();
                        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                        String fileName = "screenshot" + ((screenShotsFiles == null) ? 1 : screenShotsFiles.size() + 1);
                        File outputFile = new File(Constants.screenShotsDirectoryPath, "/" + fileName + ".png");
                        ImageIO.write(screenFullImage, "png", outputFile);
                        screenShotsFiles.add(outputFile);
                        System.out.println("screen shot was taken!\n");
                    } catch (AWTException | IOException ex) {
                        System.err.println("Error in capturing screen: " + ex.getMessage());
                    }
                } else if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                    cancelled[0] = true;
                    cleanupKeyListener(this);
                }
            }
            @Override
            public void nativeKeyReleased(NativeKeyEvent e) {
                if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT) {
                    cleanupKeyListener(this);
                }
            }
            @Override
            public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {}
        });

        while (!cancelled[0] && screenShotsFiles.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        if (cancelled[0]) {
            System.out.println("Action cancelled!\n");
        }
    }
    private void cleanupKeyListener(NativeKeyListener listener) {
        org.jnativehook.GlobalScreen.removeNativeKeyListener(listener);
        try {
            org.jnativehook.GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("Error in unregistering native hook: " + ex.getMessage());
        }
    }

}

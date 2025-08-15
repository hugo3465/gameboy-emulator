package gb_emu.ui;

import javax.swing.*;

import gb_emu.core.FrameObserver;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Screen extends JPanel implements FrameObserver {

    private BufferedImage image;

    public Screen(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(width * 2, height * 2)); // zoom opcional
    }

    @Override
    public void onFrameReady(int[] frame) {
        image.setRGB(0, 0, image.getWidth(), image.getHeight(), frame, 0, image.getWidth());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }

    public void start() {
        JFrame frame = new JFrame("Game Boy Emulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }

}

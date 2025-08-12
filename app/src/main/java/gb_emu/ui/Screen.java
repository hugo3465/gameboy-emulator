package gb_emu.ui;

import javax.swing.*;

import gb_emu.core.GameBoy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Screen extends JPanel {

    private BufferedImage image;
    private GameBoy gb;

    public Screen(int width, int height, GameBoy gb) {
        this.gb = gb;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(width * 2, height * 2)); // zoom opcional
    }

    public void updatePixels(int[] pixels) {
        image.setRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
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

        // Exemplo: preencher a imagem com branco
        // int[] pixels = new int[160 * 144];
        // for (int i = 0; i < pixels.length; i++) {
        // pixels[i] = 0xFFFFFF; // branco
        // }

        // Atualiza a imagem
        new Thread(() -> {
            while (true) {
                int[] screen = gb.getScreen();
                this.updatePixels(screen);
            }
        }).start();
    }
}

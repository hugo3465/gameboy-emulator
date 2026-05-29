package gb_emu.ui.debugScreen;

import java.awt.image.BufferedImage;
import java.awt.*;

import javax.swing.JPanel;

import gb_emu.core.FrameObserver;
import gb_emu.core.ppu.PPU;
import gb_emu.core.ppu.Palette;
import gb_emu.core.ppu.VRAM;
import gb_emu.core.utils.PPUUtils;

public class TestPannel2 extends JPanel implements FrameObserver {
    private final int RE_RENDER_DELAY = 10000;

    private VRAM vram;
    private BufferedImage image;
    private PPU ppu;
    private Palette palette;

    // Delay between background rerenders to avoid slowdowns
    private int currentDelayState;

    public TestPannel2(VRAM vram, Palette bgPalette, PPU ppu, int width, int height) {
        this.vram = vram;
        this.ppu = ppu;
        this.palette = bgPalette;
        this.currentDelayState = 0;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(width * 2, height * 2)); // zoom opcional
    }

    @Override
    public void onFrameReady() {
        // Testar os dois mapas de background
        boolean useSecondMap = false; // Trocar entre false (0x9800) e true (0x9C00)
        int[] bgMapDebug = PPUUtils.debugBGMap(vram, useSecondMap);

        // Definir o tamanho do image buffer se necessário
        if (image.getWidth() != 256 || image.getHeight() != 256) {
            image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        }

        // Mostrar o BGMap usando cores de debug
        image.setRGB(0, 0, 256, 256, bgMapDebug, 0, 256);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }
}

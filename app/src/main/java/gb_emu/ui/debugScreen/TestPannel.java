package gb_emu.ui.debugScreen;

import java.awt.image.BufferedImage;
import java.awt.*;

import javax.swing.JPanel;

import gb_emu.core.FrameObserver;
import gb_emu.core.ppu.Palette;
import gb_emu.core.ppu.VRAM;
import gb_emu.core.utils.PPUUtils;

public class TestPannel extends JPanel implements FrameObserver {
    private VRAM vram;
    private BufferedImage tile1;
    private Palette palette;

    public TestPannel(VRAM vram, Palette bgPalette) {
        this.vram = vram;
        this.palette = bgPalette;

        this.tile1 = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(8 * 2, 8 * 2)); // zoom opcional
    }

    @Override
    public void onFrameReady() {
        int[] tile1Data = vram.getTile(0x8000);
        int[] tile1Frame = PPUUtils.processTileData(tile1Data);

        // Convert to RGB
        int[] rgbFrame = new int[64];
        for (int i = 0; i < 64; i++) {
            rgbFrame[i] = palette.getColor(tile1Frame[i]);
        }

        tile1.setRGB(0, 0, 8, 8, rgbFrame, 0, 8);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(tile1, 0, 0, getWidth(), getHeight(), null);
    }
}

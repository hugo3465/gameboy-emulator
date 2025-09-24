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
        if (currentDelayState == RE_RENDER_DELAY) {
            currentDelayState = 0;

            int[] bgMap = PPUUtils.renderBackgroundTileMap(vram, palette);
            image.setRGB(0, 0, image.getWidth(), image.getHeight(), bgMap, 0, image.getWidth());
            repaint();
        } else {
            currentDelayState++;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }
}

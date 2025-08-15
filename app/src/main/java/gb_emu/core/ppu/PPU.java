package gb_emu.core.ppu;

import gb_emu.core.ppu.modes.OAMSearchHandler;
import gb_emu.core.ppu.modes.PixelTransferHandler;
import gb_emu.core.mem.MMU;

public class PPU {
    private static final int LCDC_INITIAL_VALUE = 0x91;
    private static final int SCROLL_Y_INITIAL_VALUE = 0;
    private static final int SCROLL_X_INITIAL_VALUE = 0;
    private static final int BGP_INITIAL_VALUE = 0xFC;

    private static final int OAM_SEARCH_CYCLES = 80;
    private static final int PIXEL_TRANSFER_CYCLES = 172;
    private static final int HBLANK_CYCLES = 204;
    private static final int LINE_CYCLES = 456;
    private static final int VBLANK_LINES_START = 144;
    private static final int VBLANK_LINES_END = 153;
    private static final int TOTAL_LINES = 154;

    private enum Mode {
        OAM_SEARCH, // 2
        PIXEL_TRANSFER, // 3
        HBLANK, // 0
        VBLANK // 1
    }

    private MMU mmu;
    private VRAM vRam;
    private OAM oam;
    private Screen screen;
    private PPURegisters registers;
    private Palette bgPalette;

    private Mode currentMode;
    private int modeClock;

    private OAMSearchHandler oamSearchHandler;
    private PixelTransferHandler pixelTransferHandler;
    private boolean frameReady = false;

    public PPU(PPURegisters ppuRegisters, VRAM vram, OAM oam, MMU mmu) {
        this.vRam = vram;
        this.registers = ppuRegisters;
        this.oam = oam;
        this.mmu = mmu;
        this.screen = new Screen();
        this.bgPalette = new Palette();

        this.currentMode = Mode.OAM_SEARCH;
        this.modeClock = 0;

        this.oamSearchHandler = new OAMSearchHandler(oam, registers);
        this.pixelTransferHandler = new PixelTransferHandler(vRam, oam, registers, screen, bgPalette);

        // Initialize registers essential for LCD operation
        registers.setLCDC(LCDC_INITIAL_VALUE); // LCDC on
        registers.setSCY(SCROLL_Y_INITIAL_VALUE); // Scroll Y
        registers.setSCX(SCROLL_X_INITIAL_VALUE); // Scroll X
        registers.setBGP(BGP_INITIAL_VALUE); // Default Pallete fot he background
    }

    public void step(int cycles) {
        Mode oldMode = currentMode;
        modeClock += cycles;

        switch (oldMode) {
            case OAM_SEARCH:
                oamSearchHandler.tick();
                if (modeClock >= OAM_SEARCH_CYCLES) {
                    modeClock -= OAM_SEARCH_CYCLES;
                    currentMode = Mode.PIXEL_TRANSFER;

                }
                break;

            case PIXEL_TRANSFER:
                pixelTransferHandler.tick();
                if (modeClock >= PIXEL_TRANSFER_CYCLES) {
                    modeClock -= PIXEL_TRANSFER_CYCLES;
                    currentMode = Mode.HBLANK;
                }
                break;

            case HBLANK:
                // Acho que falta alguma coisa antes

                if (modeClock >= HBLANK_CYCLES) {
                    modeClock -= HBLANK_CYCLES;
                    registers.incrementLY();

                    if (registers.getLY() < VBLANK_LINES_START) {
                        currentMode = Mode.OAM_SEARCH;
                    } else if (registers.getLY() == VBLANK_LINES_START) {
                        currentMode = Mode.VBLANK;
                        triggerVBlankInterrupt();
                        frameReady = true;
                    }
                }
                break;

            case VBLANK:
                // if (registers.getLY() == 144) {
                // // set IF register to 0 (interupt VBlank)
                // mmu.write(0xFF0F, 0);
                // }

                if (modeClock >= LINE_CYCLES) {
                    modeClock -= LINE_CYCLES;
                    registers.incrementLY();

                    if (registers.getLY() > VBLANK_LINES_END) { // resets the frame
                        registers.setLY(0);
                        currentMode = Mode.OAM_SEARCH;
                        frameReady = false;
                    }
                }
                break;
        }
    }

    private void triggerVBlankInterrupt() {
        // Set bit 0 of the IF register (0xFF0F) to signal the interrupt of VBlank
        int interruptFlags = mmu.read(0xFF0F);
        mmu.write(0xFF0F, interruptFlags | 0x01);
    }

    public int[] getFrame() {
        return screen.getPixels();
    }

    public VRAM getVram() {
        return vRam;
    }

    public OAM getOAM() {
        return oam;
    }

    public OAM getOam() {
        return oam;
    }

    public PPURegisters getRegisters() {
        return registers;
    }

    public boolean isFrameReady() {
        return frameReady;
    }

    public void resetFrameReady() {
        frameReady = false;
    }
}
package gb_emu.core.ppu;

import gb_emu.core.ppu.modes.OAMSearchHandler;
import gb_emu.core.ppu.modes.PixelTransferHandler;
import gb_emu.core.mem.MMU;

public class PPU {
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
        registers.setLCDC(0x91); // LCDC on
        registers.setSCY(0); // Scroll Y
        registers.setSCX(0); // Scroll X
        registers.setBGP(0xFC); // Default Pallete fot he background
    }

    public void step(int cycles) {
        Mode oldMode = currentMode;
        modeClock += cycles;

        switch (oldMode) {
            case OAM_SEARCH:
                oamSearchHandler.tick();
                currentMode = Mode.PIXEL_TRANSFER;
                break;

            case PIXEL_TRANSFER:
                pixelTransferHandler.tick();
                currentMode = Mode.HBLANK;
                break;

            case HBLANK:
                // Acho que falta alguma coisa antes

                registers.incrementLY();
                if (registers.getLY() < 144) {
                    currentMode = Mode.OAM_SEARCH;
                } else if (registers.getLY() == 144) {
                    currentMode = Mode.VBLANK;
                }
                break;

            case VBLANK:
                if (registers.getLY() == 144) {
                    // set IF register to 0 (interupt VBlank)
                    mmu.write(0xFF0F, 0);
                }

                if (modeClock >= 456) {
                    modeClock = 0;
                    registers.incrementLY();

                    if (registers.getLY() == 153) {
                        registers.setLY(0);
                        currentMode = Mode.OAM_SEARCH;
                    }
                }
                break;
        }
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
}
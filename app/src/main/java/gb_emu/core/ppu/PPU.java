package gb_emu.core.ppu;

public class PPU {
    private enum Mode {
        OAM_SEARCH, // 2
        PIXEL_TRANSFER, // 3
        HBLANK, // 0
        VBLANK // 1
    }

    private VRAM vRam;
    private OAM oam;
    private Screen screen;
    private PPURegisters registers;
    private Palette bgPalette;

    private Mode currentMode;
    private int modeClock;


    public PPU() {
        this.vRam = new VRAM();
        this.oam = new OAM();
        this.screen = new Screen();
        this.registers = new PPURegisters();
        this.bgPalette = new Palette();

        this.currentMode = Mode.OAM_SEARCH;
        this.modeClock = 0;

        // Inicializar os registos essenciais para o LCD funcionar
        registers.setLCDC(0x91); // LCDC ligado, background habilitado
        registers.setSCY(0); // Scroll Y
        registers.setSCX(0); // Scroll X
        registers.setBGP(0xFC); // Palette padrão para background
    }

    public void step(int cycles) {
        Mode oldMode = currentMode;
        modeClock += cycles;


        switch (oldMode) {
            case OAM_SEARCH:
                currentMode = Mode.PIXEL_TRANSFER;
                break;

            case PIXEL_TRANSFER:
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
                if (registers.getLY() == 144) { // first time in this mode
                    // TODO set IF register to 0 (interupt VBlank)
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

    private void renderScanline() {
        final int LCDC = registers.getLCDC();
        final int SCX = registers.getSCX();
        final int LY = registers.getLY();
        final int SCY = registers.getSCY();

        if ((LCDC & 0x01) == 0)
            return; // Background display is disabled

        int tileMapBase = ((LCDC & 0x08) != 0) ? 0x9C00 : 0x9800;
        int tileDataBase = ((LCDC & 0x10) != 0) ? 0x8000 : 0x8800;

        for (int x = 0; x < Screen.SCREEN_WIDTH; x++) {
            int scrolledX = (x + SCX) & 0xFF;
            int scrolledY = (LY + SCY) & 0xFF;

            int tileX = scrolledX / 8;
            int tileY = scrolledY / 8;

            int tileIndexAddr = tileMapBase + tileY * 32 + tileX;
            int tileIndex = vRam.read(tileIndexAddr);

            if ((LCDC & 0x10) == 0) {
                tileIndex = (byte) tileIndex; // Interpret tile index as signed (range -128 to 127)
            }

            // Fetch tile pixel row
            int tileLine = scrolledY % 8;
            int tileAddr = tileDataBase + tileIndex * 16 + tileLine * 2;
            // int indexLow = tileAddr;
            // int indexHigh = indexLow + 1;

            int low = vRam.read(tileAddr);
            int high = vRam.read(tileAddr + 1);

            int bitIndex = 7 - (scrolledX % 8);
            int bit0 = (low >> bitIndex) & 1;
            int bit1 = (high >> bitIndex) & 1;
            int colorId = (bit1 << 1) | bit0;

            int color = bgPalette.getColor(colorId);

            int screenIndex = LY * Screen.SCREEN_WIDTH + x;
            screen.writeOnScreen(screenIndex, color);
        }
    }

    public int[] getFrame() {
        return screen.getPixels();
    }

    public int readVRAM(int address) {
        return vRam.read(address);
    }

    public void writeVRAM(int address, int value) {
        vRam.write(address, value);
    }

    public int readOAM(int address) {
        return oam.read(address);
    }

    public void writeOAM(int address, int value) {
        oam.write(address, value);
    }

}

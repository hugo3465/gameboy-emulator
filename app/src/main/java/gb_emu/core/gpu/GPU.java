package gb_emu.core.gpu;

import gb_emu.core.mem.RAM;

public class GPU {
    private static final int VRAM_CAPACITY = 0x2000; // 8KB
    private static final int VRAM_OFFSET = 0x8000;
    private static final int OAM_CAPACITY = 0xA0; // 160 bytes
    private static final int OAM_OFFSET = 0xFE00;

    private enum Mode {
        OAM_SEARCH, // 2
        PIXEL_TRANSFER, // 3
        HBLANK, // 0
        VBLANK // 1
    }

    private RAM vRam;
    private RAM oam;
    private Screen screen;
    private GPURegisters registers;
    private Palette bgPalette;

    private Mode mode = Mode.OAM_SEARCH;
    private int modeClock = 0;
    private int line = 0;

    public GPU() {
        this.vRam = new RAM(VRAM_CAPACITY, VRAM_OFFSET);
        this.oam = new RAM(OAM_CAPACITY, OAM_OFFSET);
        this.screen = new Screen();
        this.registers = new GPURegisters();
        this.bgPalette = new Palette();
    }

    public void step(int cycles) {
        modeClock += cycles;

        switch (mode) {
            case OAM_SEARCH:
                if (modeClock >= 80) {
                    modeClock -= 80;
                    mode = Mode.PIXEL_TRANSFER;
                }
                break;

            case PIXEL_TRANSFER:
                if (modeClock >= 172) {
                    modeClock -= 172;
                    mode = Mode.HBLANK;
                    renderScanline();
                }
                break;

            case HBLANK:
                if (modeClock >= 204) {
                    modeClock -= 204;
                    line++;
                    if (line == 144) {
                        mode = Mode.VBLANK;
                        // Trigger VBlank interrupt here
                    } else {
                        mode = Mode.OAM_SEARCH;
                    }
                    registers.setLY(line);
                }
                break;

            case VBLANK:
                if (modeClock >= 456) {
                    modeClock -= 456;
                    line++;
                    if (line > 153) {
                        line = 0;
                        mode = Mode.OAM_SEARCH;
                    }
                    registers.setLY(line);
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
            int indexLow = tileAddr;
            int indexHigh = indexLow + 1;

            if (indexLow < 0 || indexHigh >= VRAM_CAPACITY) {
                continue; // Skip this pixel if the address is invalid
            }

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

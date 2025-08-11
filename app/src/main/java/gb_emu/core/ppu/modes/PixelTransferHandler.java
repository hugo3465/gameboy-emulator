package gb_emu.core.ppu.modes;

import gb_emu.core.ppu.OAM;
import gb_emu.core.ppu.PPURegisters;
import gb_emu.core.ppu.Palette;
import gb_emu.core.ppu.Screen;
import gb_emu.core.ppu.VRAM;

public class PixelTransferHandler implements PPUMode {
    public static int NUMBER_OF_TILES = 32;
    public static int TILE_SIZE = 8; // px

    private VRAM vRam;
    private OAM oam;
    private PPURegisters registers;
    private Screen screen;
    private Palette bgPalette;

    public PixelTransferHandler(VRAM vRam, OAM oam, PPURegisters registers, Screen screen, Palette bgPalette) {
        this.vRam = vRam;
        this.oam = oam;
        this.registers = registers;
        this.screen = screen;
        this.bgPalette = bgPalette;
    }

    @Override
    public void tick() {
        int ly = registers.getLY();
        int scx = registers.getSCX();
        int scy = registers.getSCY();

        for (int x = 0; x < Screen.SCREEN_WIDTH; x++) {
            // Calculate bgX/bgY
            int bgX = (scx + x) & 0xFF; // X coordenate on the BG map
            int bgY = (scy + ly) & 0xFF;

            // find tile
            int tileCol = bgX / TILE_SIZE;
            int tileRow = bgY / TILE_SIZE;
            int tileMapIndex = tileRow * NUMBER_OF_TILES + tileCol;

            // Read tile index on the VRAM
            int tileMapBase = registers.getBgTileMapDisplay(); // 0x9800 or 0x9C00
            int tileIndex = vRam.read(tileMapBase + tileMapIndex - 0x8000);

            // Convert indexes to tile data addresses
            boolean signedIndex = registers.isBgWindowTileDataSigned();
            if (signedIndex) {
                tileIndex = (byte) tileIndex; // signed
                tileIndex += 128; // convert to 0–255 int the memmory
            }
            int tileDataBase = registers.getBgWindowTileData();

            // read the tile line that contains the pixel
            int lineInTile = bgY % TILE_SIZE;
            int tileAddr = tileDataBase + (tileIndex * 16) + (lineInTile * 2);

            int lowByte = vRam.read(tileAddr);
            int highByte = vRam.read(tileAddr + 1);

            // extract right pixel
            int bitIndex = 7 - (bgX % TILE_SIZE);
            int bit0 = (lowByte >> bitIndex) & 1;
            int bit1 = (highByte >> bitIndex) & 1;
            int colorId = (bit1 << 1) | bit0; // 0–3

            // Apply pallet
            int color = bgPalette.getColor(colorId);

            // escrever no framebuffer
            int index = ly * Screen.SCREEN_WIDTH + x;
            screen.writeOnScreen(index, color);
        }
    }

    // private void renderScanline() {
    //     final int LCDC = registers.getLCDC();
    //     final int SCX = registers.getSCX();
    //     final int LY = registers.getLY();
    //     final int SCY = registers.getSCY();

    //     if ((LCDC & 0x01) == 0)
    //         return; // Background display is disabled

    //     int tileMapBase = ((LCDC & 0x08) != 0) ? 0x9C00 : 0x9800;
    //     int tileDataBase = ((LCDC & 0x10) != 0) ? 0x8000 : 0x8800;

    //     for (int x = 0; x < Screen.SCREEN_WIDTH; x++) {
    //         int scrolledX = (x + SCX) & 0xFF;
    //         int scrolledY = (LY + SCY) & 0xFF;

    //         int tileX = scrolledX / 8;
    //         int tileY = scrolledY / 8;

    //         int tileIndexAddr = tileMapBase + tileY * 32 + tileX;
    //         int tileIndex = vRam.read(tileIndexAddr);

    //         if ((LCDC & 0x10) == 0) {
    //             tileIndex = (byte) tileIndex; // Interpret tile index as signed (range -128 to 127)
    //         }

    //         // Fetch tile pixel row
    //         int tileLine = scrolledY % 8;
    //         int tileAddr = tileDataBase + tileIndex * 16 + tileLine * 2;
    //         // int indexLow = tileAddr;
    //         // int indexHigh = indexLow + 1;

    //         int low = vRam.read(tileAddr);
    //         int high = vRam.read(tileAddr + 1);

    //         int bitIndex = 7 - (scrolledX % 8);
    //         int bit0 = (low >> bitIndex) & 1;
    //         int bit1 = (high >> bitIndex) & 1;
    //         int colorId = (bit1 << 1) | bit0;

    //         int color = bgPalette.getColor(colorId);

    //         int screenIndex = LY * Screen.SCREEN_WIDTH + x;
    //         screen.writeOnScreen(screenIndex, color);
    //     }
    // }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }
}

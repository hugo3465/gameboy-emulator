package gb_emu.core.utils;

import gb_emu.core.ppu.Palette;
import gb_emu.core.ppu.Screen;
import gb_emu.core.ppu.VRAM;

public class PPUUtils {
    private PPUUtils() {
    } // prevent initialization

    /**
     * Processes the raw tile data to return 64 2-bit values for the tile's pixels.
     * Each pixel is represented by 2 bits.
     * 
     * @param values The 16-byte tile data (2 bits per pixel).
     * @return An array of 64 2-bit values, each representing a pixel's color (0-3).
     */
    public static int[] processTileData(int[] values, Palette palette) {
        int[] tileColors = new int[64]; // 8x8 pixels

        for (int row = 0; row < 8; row++) {
            int low = values[row * 2];
            int high = values[row * 2 + 1];
            for (int col = 0; col < 8; col++) {
                int bit = 7 - col; // Bits are processed from left to right
                int colorId = ((high >> bit) & 1) << 1 | ((low >> bit) & 1);
                tileColors[row * 8 + col] = palette.getColor(colorId);
            }
        }

        return tileColors;
    }

    public static int[] renderBackgroundTileMap(VRAM vRam, Palette bgPalette) {
        final int TILES_PER_ROW = VRAM.BACKGROUND_TILE_MAP_SIDE_LENGHT; // 32
        final int TILES_PER_COL = VRAM.BACKGROUND_TILE_MAP_SIDE_LENGHT; // 32
        final int TILE_SIZE = 8;

        int[] screenBuffer = new int[Screen.SCREEN_WIDTH * Screen.SCREEN_HEIGHT];

        for (int tileMapIndex = 0; tileMapIndex < VRAM.BACKGROUND_TILE_MAP_SIZE; tileMapIndex++) {
            int tileRow = tileMapIndex / TILES_PER_ROW;
            int tileCol = tileMapIndex % TILES_PER_COL;

            int tileIndex = vRam.readBackgroundTileMap(tileMapIndex);
            int tileDataAddr = BGMapAttributes.getTileDataAddress(tileIndex, true, 0);
            int[] tilePixels = PPUUtils.processTileData(vRam.getTile(tileDataAddr), bgPalette); // Pass palette here

            for (int y = 0; y < TILE_SIZE; y++) {
                for (int x = 0; x < TILE_SIZE; x++) {
                    int color = tilePixels[y * TILE_SIZE + x];
                    int screenX = tileCol * TILE_SIZE + x;
                    int screenY = tileRow * TILE_SIZE + y;
                    if (screenX < Screen.SCREEN_WIDTH && screenY < Screen.SCREEN_HEIGHT) {
                        int screenIndex = screenY * Screen.SCREEN_WIDTH + screenX;
                        screenBuffer[screenIndex] = color;
                    }
                }
            }
        }

        return screenBuffer;
    }
}

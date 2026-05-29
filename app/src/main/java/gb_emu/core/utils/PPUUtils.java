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

    /**
     * Debug method to render raw BGMap contents directly from VRAM
     * 
     * @param vRam         VRAM instance
     * @param useSecondMap true to use map at 0x9C00, false for map at 0x9800
     * @return RGB array for direct display
     */
    public static int[] debugBGMap(VRAM vRam, boolean useSecondMap) {
        int mapBase = useSecondMap ? 0x9C00 : 0x9800;
        int[] result = new int[32 * 32 * 8 * 8]; // 256x256 pixels

        // For visualization - different colors for different values
        int[] debugColors = new int[256]; // One color per possible tile index
        for (int i = 0; i < 256; i++) {
            // Generate a unique color for each tile index
            int r = (i & 0x07) * 32;
            int g = ((i >> 3) & 0x07) * 32;
            int b = ((i >> 6) & 0x03) * 64;
            debugColors[i] = (r << 16) | (g << 8) | b;
        }

        // Loop through the entire BGMap
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                int mapIndex = y * 32 + x;
                int tileIndex = vRam.read(mapBase + mapIndex) & 0xFF; // Get tile index from map

                // Fill this 8x8 area with the debug color for this tile index
                for (int py = 0; py < 8; py++) {
                    for (int px = 0; px < 8; px++) {
                        int screenX = x * 8 + px;
                        int screenY = y * 8 + py;
                        int screenIndex = screenY * 256 + screenX;

                        if (screenIndex < result.length) {
                            result[screenIndex] = debugColors[tileIndex];
                        }
                    }
                }

                // Opcional: Desenhar número do tile no centro da área
                // Isso exigiria mais código para renderizar texto, então omitimos por
                // simplicidade
            }
        }

        return result;
    }
}

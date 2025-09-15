package gb_emu.core.utils;

public class PPUUtils {
    /**
     * Processes the raw tile data to return 64 2-bit values for the tile's pixels.
     * Each pixel is represented by 2 bits.
     * 
     * @param values The 16-byte tile data (2 bits per pixel).
     * @return An array of 64 2-bit values, each representing a pixel's color (0-3).
     */
    public static int[] processTileData(int[] values) {
        int[] tileColors = new int[64]; // 8x8 pixels

        for (int row = 0; row < 8; row++) {
            int low = values[row * 2];
            int high = values[row * 2 + 1];
            for (int col = 0; col < 8; col++) {
                int bit = 7 - col; // Bits são processados da esquerda para a direita
                int color = ((high >> bit) & 1) << 1 | ((low >> bit) & 1);
                tileColors[row * 8 + col] = color;
            }
        }

        return tileColors;
    }

}

package gb_emu.core.utils;

public class PPUUtils {
    /**
     * Processes the raw tile data to return 64 2-bit values for the tile's pixels.
     * Each pixel is represented by 2 bits.
     * @param values The 16-byte tile data (2 bits per pixel).
     * @return An array of 64 2-bit values, each representing a pixel's color (0-3).
     */
    public static int[] processTileData(int[] values) {
        int[] tileColors = new int[64]; // We need 64 pixel colors for an 8x8 tile

        // Iterate over the 16 bytes of tile data
        for (int i = 0; i < 16; i++) {
            int byteData = values[i]; // Get each byte of the tile data

            // Extract the 2-bit values for the 4 pixels in this byte
            for (int j = 0; j < 4; j++) {
                int twoBitValue = (byteData >> (6 - 2 * j)) & 0x03; // Extract 2 bits at a time

                // Store the 2-bit value in the tileColors array
                tileColors[i * 4 + j] = twoBitValue;
            }
        }

        return tileColors; // Return the array of 64 2-bit color values
    }

}

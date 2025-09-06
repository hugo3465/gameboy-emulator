package gb_emu.core.utils;

public class BitwiseOperation {
    /**
     * Sums the bits by position of the elements in the array.
     * 
     * @param values Array of integers between 0 and 255.
     * @return An array of 8 integers containing the sum of the bits by position
     *         (bit 7 to bit 0).
     */
    public static int[] sumBitsPerPosition(int[] values) {
        int[] bitSums = new int[8]; // position 0 = most significant bit (bit 7)

        for (int value : values) {
            for (int i = 0; i < 8; i++) {
                int bit = (value >> (7 - i)) & 1; // Get the bit at position i
                bitSums[i] += bit; // Sum the bits at each position
            }
        }

        return bitSums;
    }

}

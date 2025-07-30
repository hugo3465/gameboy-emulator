package gb_emu.core.gpu;

public class Palette {
    private static int PALETTE_SIZE = 4;

    private int[] colors = new int[PALETTE_SIZE];

    private static final int[] GRAYSCALE = {
        0xFFFFFFFF, // white
        0xFFAAAAAA, // light gray
        0xFF555555, // dark gray
        0xFF000000  // black
    };

    public Palette() {
        for (int i = 0; i < PALETTE_SIZE; i++) {
            colors[i] = GRAYSCALE[i];
        }
    }

    /**
     * Configure the palette based on the value of the BGP/OBP0/OBP1 record
     * @param paletteByte
     */
    public void loadFromByte(int paletteByte) {
        for (int i = 0; i < PALETTE_SIZE; i++) {
            int bits = (paletteByte >> (i * 2)) & 0b11;
            colors[i] = GRAYSCALE[bits];
        }
    }

    public int getColor(int colorId) {
        return colors[colorId & 0b11];
    }
}

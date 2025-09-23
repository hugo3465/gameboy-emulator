package gb_emu.core.utils;

public class BGMapAttributes {

    /**
     * Extracts the priority bit (bit 7).
     * @param attrByte The BG Map attribute byte.
     * @return true if the tile has priority over OBJ.
     */
    public static boolean getPriority(int attrByte) {
        return ((attrByte >> 7) & 0x1) != 0;
    }

    /**
     * Extracts the vertical flip bit (Y flip, bit 6).
     * @param attrByte The BG Map attribute byte.
     * @return true if the tile should be drawn vertically flipped.
     */
    public static boolean getYFlip(int attrByte) {
        return ((attrByte >> 6) & 0x1) != 0;
    }

    /**
     * Extracts the horizontal flip bit (X flip, bit 5).
     * @param attrByte The BG Map attribute byte.
     * @return true if the tile should be drawn horizontally flipped.
     */
    public static boolean getXFlip(int attrByte) {
        return ((attrByte >> 5) & 0x1) != 0;
    }

    /**
     * Extracts the VRAM bank bit (bit 3).
     * @param attrByte The BG Map attribute byte.
     * @return 0 for bank 0, 1 for bank 1.
     */
    public static int getVRAMBank(int attrByte) {
        return (attrByte >> 3) & 0x1;
    }

    /**
     * Extracts the color palette index (bits 0-2).
     * @param attrByte The BG Map attribute byte.
     * @return Value from 0 to 7 indicating which palette to use.
     */
    public static int getPaletteIndex(int attrByte) {
        return attrByte & 0x7;
    }
}
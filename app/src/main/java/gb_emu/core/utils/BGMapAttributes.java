package gb_emu.core.utils;

public class BGMapAttributes {
    /**
     * Extracts the priority bit (bit 7).
     * 
     * @param attrByte The BG Map attribute byte.
     * @return true if the tile has priority over OBJ.
     */
    public static boolean getPriority(int attrByte) {
        return ((attrByte >> 7) & 0x1) != 0;
    }

    /**
     * Extracts the vertical flip bit (Y flip, bit 6).
     * 
     * @param attrByte The BG Map attribute byte.
     * @return true if the tile should be drawn vertically flipped.
     */
    public static boolean getYFlip(int attrByte) {
        return ((attrByte >> 6) & 0x1) != 0;
    }

    /**
     * Extracts the horizontal flip bit (X flip, bit 5).
     * 
     * @param attrByte The BG Map attribute byte.
     * @return true if the tile should be drawn horizontally flipped.
     */
    public static boolean getXFlip(int attrByte) {
        return ((attrByte >> 5) & 0x1) != 0;
    }

    /**
     * Extracts the VRAM bank bit (bit 3).
     * 
     * @param attrByte The BG Map attribute byte.
     * @return 0 for bank 0, 1 for bank 1.
     */
    public static int getVRAMBank(int attrByte) {
        return (attrByte >> 3) & 0x1;
    }

    /**
     * Extracts the color palette index (bits 0-2).
     * 
     * @param attrByte The BG Map attribute byte.
     * @return Value from 0 to 7 indicating which palette to use.
     */
    public static int getPaletteIndex(int attrByte) {
        return attrByte & 0x7;
    }

    /**
     * Returns the address of the tile data in VRAM for a given tile index and mode.
     * 
     * @param tileIndex    The tile index from the BG Map.
     * @param unsignedMode True for unsigned mode (base 0x8000), false for signed
     *                     mode (base 0x8800).
     * @param vramBank     0 for bank 0, 1 for bank 1 (CGB only).
     * @return The address in VRAM where the tile data starts.
     */
    public static int getTileDataAddress(int tileIndex, boolean unsignedMode, int vramBank) {
        int base = unsignedMode ? 0x8000 : 0x8800;
        int index = unsignedMode ? tileIndex : (byte) tileIndex; // signed cast for mode 0x8800
        // Each tile is 16 bytes
        // TODO For CGB, I will need to add bank offset externally if VRAM is banked
        return base + (index * 16);
    }
}
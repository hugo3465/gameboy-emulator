package gb_emu.core.ppu;

import gb_emu.core.mem.RAM;

public class VRAM extends RAM {
    public static final int VRAM_CAPACITY = 0x2000; // 8KB
    public static final int VRAM_OFFSET = 0x8000;
    public static final int TILE_LENGHT = 16; // 16 bytes

    public VRAM() {
        super(VRAM_CAPACITY, VRAM_OFFSET);
    }

    public int readTileByte(int tileIndex, int byteOffset) {
        int address = VRAM_OFFSET + tileIndex * TILE_LENGHT + byteOffset;
        return read(address);
    }

    public int readTileMap0(int index) {
        return read(0x9800 + index);
    }

    public int readTileMap1(int index) {
        return read(0x9C00 + index);
    }
}

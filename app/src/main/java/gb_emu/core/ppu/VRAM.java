package gb_emu.core.ppu;

import gb_emu.core.mem.RAM;

public class VRAM extends RAM {
    public static final int CAPACITY = 0x2000; // 8KB
    public static final int OFFSET = 0x8000;
    public static final int TILE_SIZE = 16; // 16 bytes per tile

    public VRAM() {
        super(CAPACITY, OFFSET);

        // Work that should be done by the bootRom (opicional)
        initialize();
    }

    private void initialize() {
        // Fill VRAM with default values
        for (int i = 0; i < CAPACITY; i++) {
            write(OFFSET + i, 0x00);
        }
    }

    public int[] getTile(int startIndex) {
        int[] tile = new int[TILE_SIZE];
        for (int i = 0; i < TILE_SIZE; i++) {
            tile[i] = read(startIndex + i);
        }
        return tile;
    }

    public int readTileByte(int tileIndex, int byteOffset) {
        int address = OFFSET + tileIndex * TILE_SIZE + byteOffset;
        return read(address);
    }

    public int readTileMap0(int index) {
        return read(0x9800 + index);
    }

    public int readTileMap1(int index) {
        return read(0x9C00 + index);
    }
}

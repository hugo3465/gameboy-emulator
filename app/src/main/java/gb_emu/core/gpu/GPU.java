package gb_emu.core.gpu;

import gb_emu.core.mem.RAM;

public class GPU {
    private static final int VRAM_CAPACITY = 0x2000; // 8KB
    private static final int VRAM_OFFSET = 0x80000;

    private RAM vRam;
    
    public GPU() {
        this.vRam = new RAM(VRAM_CAPACITY, VRAM_OFFSET);
    }

    public int readVRAM(int address) {
        return vRam.read(address);
    }

    public void writeVRAM(int address, int value) {
        vRam.write(address, value);
    }
    
}

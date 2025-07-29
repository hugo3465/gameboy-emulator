package gb_emu.core.gpu;

import gb_emu.core.mem.RAM;

public class GPU {
    private static final int VRAM_CAPACITY = 0x2000; // 8KB
    private static final int VRAM_OFFSET = 0x80000;
    private static final int OAM_CAPACITY = 0xA0; // 160 bytes
    private static final int OAM_OFFSET = 0xFE00;

    private RAM vRam;
    private RAM oam;
    
    public GPU() {
        this.vRam = new RAM(VRAM_CAPACITY, VRAM_OFFSET);
        this.oam = new RAM(OAM_CAPACITY, OAM_OFFSET);
    }

    public int readVRAM(int address) {
        return vRam.read(address);
    }

    public void writeVRAM(int address, int value) {
        vRam.write(address, value);
    }

    public int readOAM(int address) {
        return oam.read(address);
    }

    public void writeOAM(int address, int value) {
        oam.write(address, value);
    }
    
}

package gb_emu.core.mem;

import gb_emu.core.mem.cartridge.Cartridge;
import gb_emu.core.ppu.PPU;

/**
 * MMU serves as a mediator between the CPU and the memmory.
 * So the CPU does not have to know the origin of the memmory, that could be
 * from: WRAM, HRAM, OAM, VRAM, Cartridge or I/O Registers
 */
public class MMU {
    private static final int WORK_RAM_LENGHT = 0x2000; // 8KB
    private static final int WORK_RAM_OFFSET = 0xC000;
    private static final int HIGH_RAM_LENGHT = 0x7F; // 127 bytes
    private static final int HIGH_RAM_OFFSET = 0xFF80;
    private static final int ECHO_RAM_TO_WRAM_SHIFT = 0x2000;

    private Cartridge cartridge;
    private PPU ppu;
    private RAM wram; // Work RAM
    private RAM hram; // High RAM
    private byte interruptEnable = 0;

    // Not implemented
    // private Registers registers; // acho que não coloquei os registers certos,
    // não são de IO estes

    public MMU(Cartridge cartridge, PPU ppu) {
        this.cartridge = cartridge;
        this.ppu = ppu;

        this.wram = new RAM(WORK_RAM_LENGHT, WORK_RAM_OFFSET);
        this.hram = new RAM(HIGH_RAM_LENGHT, HIGH_RAM_OFFSET);
    }

    public int read(int address) {
        if (address >= 0x0000 && address <= 0x7FFF) {
            return cartridge.read(address);
        } else if (address >= 0x8000 && address <= 0x9FFF) {
            return ppu.readVRAM(address);
        } else if (address >= 0xA000 && address <= 0xBFFF) {
            return cartridge.read(address); // external RAM
        } else if (address >= 0xC000 && address <= 0xDFFF) {
            return wram.read(address);
        } else if (address >= 0xE000 && address <= 0xFDFF) {
            return wram.read(address - ECHO_RAM_TO_WRAM_SHIFT); // Echo RAM
        } else if (address >= 0xFE00 && address <= 0xFE9F) {
            return ppu.readOAM(address);
        } else if (address >= 0xFF00 && address <= 0xFF7F) {
            // return registers.read(address);
        } else if (address >= 0xFF80 && address <= 0xFFFE) {
            return hram.read(address);
        } else if (address == 0xFFFF) {
            return Byte.toUnsignedInt(interruptEnable);
        }

        return 0xFF; // default
    }

    public void write(int address, int value) {
        value &= 0xFF; // guarantees 8-bit value

        if (address >= 0x0000 && address <= 0x7FFF) {
            cartridge.write(address, value);
        } else if (address >= 0x8000 && address <= 0x9FFF) {
            ppu.writeVRAM(address, value);
        } else if (address >= 0xA000 && address <= 0xBFFF) {
            cartridge.write(address, value);
        } else if (address >= 0xC000 && address <= 0xDFFF) {
            wram.write(address, value);
        } else if (address >= 0xE000 && address <= 0xFDFF) {
            wram.write((address - ECHO_RAM_TO_WRAM_SHIFT), value);
        } else if (address >= 0xFE00 && address <= 0xFE9F) {
            ppu.writeOAM(address, value);
        } else if (address >= 0xFF00 && address <= 0xFF7F) {
            // registers.write(address, value);
        } else if (address >= 0xFF80 && address <= 0xFFFE) {
            hram.write(address, value);
        } else if (address == 0xFFFF) {
            interruptEnable = (byte) value;
        }
    }
}

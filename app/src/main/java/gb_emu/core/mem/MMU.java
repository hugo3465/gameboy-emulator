package gb_emu.core.mem;

import gb_emu.core.mem.cartridge.Cartridge;

/**
 * MMU serves as a mediator between the CPU and the memmory.
 * So the CPU does not have to know the origin of the memmory, that could be from: RAM, VRAM, Cartridge or I/O Registers
 */
public class MMU {
    private Cartridge cartridge;
    private byte interruptEnable = 0;
    
    // Not implemented
    // private Registers registers; // acho que não coloquei os registers certos, não são de IO estes
    private byte[] wram = new byte[8 * 1024];   // WRAM 8 KiB
    private byte[] hram = new byte[127];        // HRAM

    public MMU(Cartridge cartridge) {
        this.cartridge = cartridge;
    }

    public int read(int address) {
        if (address >= 0x0000 && address <= 0x7FFF) {
            return cartridge.read(address);
        } else if (address >= 0x8000 && address <= 0x9FFF) {
            //  not implemented
            // return gpu.readVRAM(address - 0x8000);
        } else if (address >= 0xA000 && address <= 0xBFFF) {
            return cartridge.read(address); // RAM externa
        } else if (address >= 0xC000 && address <= 0xDFFF) {
            return Byte.toUnsignedInt(wram[address - 0xC000]);
        } else if (address >= 0xE000 && address <= 0xFDFF) {
            // Not implemented
            // Echo RAM
            return Byte.toUnsignedInt(wram[address - 0xE000]);
        } else if (address >= 0xFE00 && address <= 0xFE9F) {
            // not implemented
            // return gpu.readOAM(address - 0xFE00);
        } else if (address >= 0xFF00 && address <= 0xFF7F) {
            // return registers.read(address);
        } else if (address >= 0xFF80 && address <= 0xFFFE) {
            // not implemented
            // High RAM
            return Byte.toUnsignedInt(hram[address - 0xFF80]);
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
            // not implemented
            // gpu.writeVRAM(address - 0x8000, value);
        } else if (address >= 0xA000 && address <= 0xBFFF) {
            cartridge.write(address, value);
        } else if (address >= 0xC000 && address <= 0xDFFF) {
            wram[address - 0xC000] = (byte) value;
        } else if (address >= 0xE000 && address <= 0xFDFF) {
            wram[address - 0xE000] = (byte) value;
        } else if (address >= 0xFE00 && address <= 0xFE9F) {
            // gpu.writeOAM(address - 0xFE00, value);
        } else if (address >= 0xFF00 && address <= 0xFF7F) {
            // registers.write(address, value);
        } else if (address >= 0xFF80 && address <= 0xFFFE) {
            // not implemented
            hram[address - 0xFF80] = (byte) value;
        } else if (address == 0xFFFF) {
            interruptEnable = (byte) value;
        }
    }
}

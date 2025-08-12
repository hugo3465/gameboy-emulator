package gb_emu.core.mem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.cartridge.Cartridge;
import gb_emu.core.ppu.PPU;

/**
 * MMU serves as a mediator between the CPU and the memmory.
 * So the CPU does not have to know the origin of the memmory, that could be
 * from: WRAM, HRAM, OAM, VRAM, Cartridge or I/O Registers
 */
public class MMU {
    private static Logger LOGGER = LoggerFactory.getLogger(MMU.class);

    private static final int WORK_RAM_LENGHT = 0x2000; // 8KB
    private static final int WORK_RAM_OFFSET = 0xC000;
    private static final int HIGH_RAM_LENGHT = 0x7F; // 127 bytes
    private static final int HIGH_RAM_OFFSET = 0xFF80;
    private static final int ECHO_RAM_TO_WRAM_SHIFT = 0x2000;

    private Cartridge cartridge;
    private PPU ppu;
    private CPURegisters cpuRegisters;
    private RAM wram; // Work RAM
    private RAM hram; // High RAM

    // Not implemented
    // private Registers registers; // acho que não coloquei os registers certos,
    // não são de IO estes

    public MMU(Cartridge cartridge, PPU ppu, CPURegisters cpuRegisters) {
        this.cartridge = cartridge;
        this.ppu = ppu;
        this.cpuRegisters = cpuRegisters;

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
        } else if (address >= 0xFF40 && address <= 0xFF4B) {
            // Delegar a leitura dos registos PPU para PPURegisters
            return ppu.getRegisters().readRegister(address);
        } else if (address >= 0xFF00 && address <= 0xFF7F) {
            // LOGGER.warn(String.format("Leitura de registo não implementado: 0x%04X",
            // address));
            return 0xFF; // TODO implementar futuramente
        } else if (address >= 0xFF80 && address <= 0xFFFE) {
            return hram.read(address);
        } else if (address == 0xFF0F) { // IF register
            return cpuRegisters.getInterruptFlags();
        } else if (address == 0xFFFF) { // IE register
            return cpuRegisters.getInterruptEnable();
        } else {
            LOGGER.warn(String.format("Attempted to read out-of-bounds address: 0x%04X", address));
            return 0xFF;
        }
    }

    public void write(int address, int value) {
        value &= 0xFF; // garante valor 8 bits

        if (address >= 0x0000 && address <= 0x7FFF) {
            cartridge.write(address, value);
        } else if (address >= 0x8000 && address <= 0x9FFF) {
            ppu.writeVRAM(address, value);
        } else if (address >= 0xA000 && address <= 0xBFFF) {
            cartridge.write(address, value);
        } else if (address >= 0xC000 && address <= 0xDFFF) {
            wram.write(address, value);
        } else if (address >= 0xE000 && address <= 0xFDFF) {
            wram.write(address - ECHO_RAM_TO_WRAM_SHIFT, value);
        } else if (address >= 0xFE00 && address <= 0xFE9F) {
            ppu.writeOAM(address, value);
        } else if (address >= 0xFF40 && address <= 0xFF4B) {
            // Delegar escrita para PPURegisters
            ppu.getRegisters().writeRegister(address, value);
        } else if (address >= 0xFF00 && address <= 0xFF7F) {
            // registers.write(address, value); // ainda não implementado
        } else if (address >= 0xFF80 && address <= 0xFFFE) {
            hram.write(address, value);
        } else if (address == 0xFF0F) { // IF register
            cpuRegisters.setInterruptFlags(value);
        } else if (address == 0xFFFF) { // IE register
            cpuRegisters.setInterruptEnable(value);
        }
    }
}

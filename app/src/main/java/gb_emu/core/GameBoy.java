package gb_emu.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gb_emu.core.cpu.CPU;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;
import gb_emu.core.mem.RAM;
import gb_emu.core.mem.cartridge.Cartridge;
import gb_emu.core.ppu.PPU;

public class GameBoy {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameBoy.class);

    private CPU cpu;
    private Cartridge cartridge;
    private RAM ram;
    private MMU mmu;
    private PPU ppu;

    public GameBoy(Cartridge cartridge) {
        this.cartridge = cartridge;
        
        CPURegisters registers = new CPURegisters();
        this.ppu = new PPU();
        this.mmu = new MMU(cartridge, ppu);
        this.cpu = new CPU(mmu, registers);
    }

    public void start() {
        while (true) {
            int cycles = cpu.step();
            ppu.step();

            // try {
            //     Thread.sleep(100);
            // } catch (InterruptedException e) {
            //     // TODO Auto-generated catch block
            //     e.printStackTrace();
            // }
            // future: timers.step(), input.step()
        }
    }

    public CPU getCpu() {
        return cpu;
    }

    public Cartridge getCartridge() {
        return cartridge;
    }

    public RAM getRam() {
        return ram;
    }

    public int[] getScreen() {
        return ppu.getFrame();
    }
}

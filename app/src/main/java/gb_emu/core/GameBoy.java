package gb_emu.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gb_emu.core.cpu.CPU;
import gb_emu.core.gpu.GPU;
import gb_emu.core.mem.MMU;
import gb_emu.core.mem.RAM;
import gb_emu.core.mem.cartridge.Cartridge;

public class GameBoy {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameBoy.class);

    private CPU cpu;
    private Cartridge cartridge;
    private RAM ram;
    private MMU mmu;
    private GPU gpu;

    public GameBoy(Cartridge cartridge) {
        this.cartridge = cartridge;
        this.gpu = new GPU();
        this.mmu = new MMU(cartridge, gpu);
        this.cpu = new CPU(this, mmu);
    }

    public void start() {
        while (true) {
            int cycles = cpu.step();
            gpu.step(cycles);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // future: gpu.step(), timers.step(), input.step()
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
}

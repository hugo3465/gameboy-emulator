package gb_emu.core;

import gb_emu.core.cpu.CPU;
import gb_emu.core.mem.RAM;
import gb_emu.core.mem.cartridge.Cartridge;

public class GameBoy {
    private CPU cpu;
    private Cartridge cartridge;
    private RAM ram;

    public GameBoy(CPU cpu, Cartridge cartridge, RAM ram) {
        this.cpu = cpu;
        this.cartridge = cartridge;
        this.ram = ram;
    }

    public void start() {
        while (true) {
            cpu.step();
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

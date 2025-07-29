package gb_emu.core;

import gb_emu.core.cpu.CPU;
import gb_emu.core.mem.MMU;
import gb_emu.core.mem.RAM;
import gb_emu.core.mem.cartridge.Cartridge;

public class GameBoy {
    private CPU cpu;
    private Cartridge cartridge;
    private RAM ram;
    private MMU mmu;

    public GameBoy(Cartridge cartridge) {
        this.cartridge = cartridge;
        this.ram = new RAM();
        this.mmu = new MMU(cartridge);
        this.cpu = new CPU(this, mmu);
    }

    public void start() {
        while (true) {
            cpu.step();
            try {
                Thread.sleep(500);
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

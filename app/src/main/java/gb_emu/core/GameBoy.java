package gb_emu.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gb_emu.core.cpu.CPU;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;
import gb_emu.core.mem.cartridge.Cartridge;
import gb_emu.core.ppu.PPU;
import gb_emu.core.ppu.VRAM;
import gb_emu.core.ppu.PPURegisters;
import gb_emu.core.ppu.OAM;

public class GameBoy {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameBoy.class);

    private List<FrameObserver> observers;

    private CPU cpu;
    private Cartridge cartridge;
    private MMU mmu;
    private PPU ppu;

    public GameBoy(Cartridge cartridge) {
        this.cartridge = cartridge;
        this.observers = new ArrayList<>();

        CPURegisters cpuRegisters = new CPURegisters();
        PPURegisters ppuRegisters = new PPURegisters();
        OAM oam = new OAM();
        VRAM vram = new VRAM();

        this.mmu = new MMU(cartridge, cpuRegisters, ppuRegisters, vram, oam);
        this.ppu = new PPU(ppuRegisters, vram, oam, mmu);
        this.cpu = new CPU(mmu, cpuRegisters);
    }

    public void start() {
        while (true) {
            int cycles = cpu.step();
            ppu.step(cycles);

            // Notify the UI that a new Frame is ready
            if (ppu.isFrameReady()) {
                notifyObservers();
            }

            // try {
            // Thread.sleep(100);
            // } catch (InterruptedException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }

            // future: timers.step(), input.step()
        }
    }

    public CPU getCpu() {
        return cpu;
    }

    public PPU getPPU() {
        return ppu;
    }

    public Cartridge getCartridge() {
        return cartridge;
    }

    public int[] getScreen() {
        return ppu.getFrame();
    }

    /**
     * Add an observer to the list of observers.
     * 
     * @param observer The observer to add.
     */
    public void addObserver(FrameObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Remove an observer from the list of observers.
     * 
     * @param observer The observer to remove.
     */
    public void removeObserver(FrameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notify all registered observers that a new frame is ready.
     */
    private void notifyObservers() {
        int[] frame = ppu.getFrame();
        for (FrameObserver observer : observers) {
            observer.onFrameReady(frame);
        }
    }
}

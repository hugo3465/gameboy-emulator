package gb_emu.core.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gb_emu.core.mem.MMU;
import gb_emu.core.mem.cartridge.Cartridge;
import gb_emu.core.ppu.VRAM;
import gb_emu.core.ppu.PPURegisters;
import gb_emu.core.ppu.OAM;

class InstructionsMapTest {

    private static CPURegisters cpuRegisters;
    private static MMU mmu;
    private static CPU cpu;

    @BeforeAll
    static void init() {
        Cartridge cartridge = new Cartridge(
                "/home/hugo-guimar-es/roms/GB/test_roms/blargg/cpu_instrs/06-ld r,r.gb",
                "/home/hugo-guimar-es/roms/GB/bootix_dmg.bin");

        cpuRegisters = new CPURegisters();
        PPURegisters ppuRegisters = new PPURegisters();
        OAM oam = new OAM();
        VRAM vram = new VRAM();
        mmu = new MMU(cartridge, cpuRegisters, ppuRegisters, vram, oam);

        cpu = new CPU(mmu, cpuRegisters);
    }

    /**
     * JR
     */
    @Test
    void test_ld_0x20() {
        cpuRegisters.setA(127);
        cpuRegisters.setB(178);
        cpuRegisters.setC(123);
        cpuRegisters.setD(148);
        cpuRegisters.setE(22);
        cpuRegisters.setF(0);
        cpuRegisters.setH(208);
        cpuRegisters.setL(29);
        cpuRegisters.setPC(31505);
        cpuRegisters.setSP(40276);
        mmu.write(31504, 34);
        mmu.write(31505, 32);
        mmu.write(31506, 17);
        mmu.write(31540, 79);

        cpu.step();

        int pc = cpuRegisters.getPC();

        assertEquals(127, cpuRegisters.getA());
        assertEquals(178, cpuRegisters.getB());
        assertEquals(123, cpuRegisters.getC());
        assertEquals(148, cpuRegisters.getD());
        assertEquals(22, cpuRegisters.getE());
        assertEquals(0, cpuRegisters.getF());
        assertEquals(208, cpuRegisters.getH());
        assertEquals(29, cpuRegisters.getL());
        assertEquals(31541, cpuRegisters.getPC());
        assertEquals(40276, cpuRegisters.getSP());

        assertEquals(32, mmu.read(31504));
        assertEquals(34, mmu.read(31505));
        assertEquals(17, mmu.read(31506));
        assertEquals(79, mmu.read(31540));
    }
}

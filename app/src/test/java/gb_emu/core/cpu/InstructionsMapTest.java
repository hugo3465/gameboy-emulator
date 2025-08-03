package gb_emu.core.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gb_emu.core.mem.MMU;
import gb_emu.core.mem.cartridge.Cartridge;
import gb_emu.core.ppu.PPU;

class InstructionsMapTest {

    private static CPURegisters registers;
    private static MMU mmu;
    private static CPU cpu;

    @BeforeAll
    static void init() {
        Cartridge cartridge = new Cartridge(
                "/home/hugo-guimar-es/roms/GB/test_roms/blargg/cpu_instrs/06-ld r,r.gb",
                "/home/hugo-guimar-es/roms/GB/bootix_dmg.bin");
        registers = new CPURegisters();
        PPU ppu = new PPU();
        mmu = new MMU(cartridge, ppu);

        cpu = new CPU(mmu, registers);
    }

    /**
     * JR
     */
    @Test
    void test_ld_0x20() {
        registers.setA(127);
        registers.setB(178);
        registers.setC(123);
        registers.setD(148);
        registers.setE(22);
        registers.setF(0);
        registers.setH(208);
        registers.setL(29);
        registers.setPC(31505);
        registers.setSP(40276);
        mmu.write(31504, 34);
        mmu.write(31505, 32);
        mmu.write(31506, 17);
        mmu.write(31540, 79);

        cpu.step();

        int pc = registers.getPC();

        assertEquals(127, registers.getA());
        assertEquals(178, registers.getB());
        assertEquals(123, registers.getC());
        assertEquals(148, registers.getD());
        assertEquals(22, registers.getE());
        assertEquals(0, registers.getF());
        assertEquals(208, registers.getH());
        assertEquals(29, registers.getL());
        assertEquals(31541, registers.getPC());
        assertEquals(40276, registers.getSP());

        assertEquals(32, mmu.read(31504));
        assertEquals(34, mmu.read(31505));
        assertEquals(17, mmu.read(31506));
        assertEquals(79, mmu.read(31540));
    }
}

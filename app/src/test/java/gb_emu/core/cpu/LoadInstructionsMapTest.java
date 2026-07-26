package gb_emu.core.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.common.base.Supplier;

import gb_emu.core.cpu.instructions.InstructionsMap;
import gb_emu.core.mem.MMU;
import gb_emu.core.mem.cartridge.Cartridge;
import gb_emu.core.ppu.OAM;
import gb_emu.core.ppu.PPURegisters;
import gb_emu.core.ppu.VRAM;

public class LoadInstructionsMapTest {
    private static CPURegisters registers;
    private static MMU mmu;
    private static CPU cpu;
    private static InstructionsMap instructionsMap;

    @BeforeAll
    static void init() {
        Cartridge cartridge = new Cartridge(
            "/home/hugo/Games/Roms/gb/Dr. Mario.gb",
            "/home/hugo/Games/Roms/gb/bootix_dmg.bin"
        );

        PPURegisters ppuRegisters = new PPURegisters();
        OAM oam = new OAM();
        VRAM vram = new VRAM();

        registers = new CPURegisters();
        mmu = new MMU(cartridge, registers, ppuRegisters, vram, oam);
        instructionsMap = new InstructionsMap(cpu, mmu, registers);
    }

    private void testLoadRegtoReg(int id, Supplier<Integer> getterA, Supplier<Integer> getterB, Consumer<Integer> setterA, Consumer<Integer> setterB) {
        final int EXPECTED_VALUE = 2;
        setterA.accept(1);
        setterB.accept(EXPECTED_VALUE);

        instructionsMap.execute(id, false);

        assertEquals(getterA.get(), EXPECTED_VALUE);
    }

    private void testLoadMemToReg(int id, Supplier<Integer> getter) {
        int prevPC = registers.getPC();
        int immediateValue = mmu.read(prevPC);

        instructionsMap.execute(id, false);

        assertEquals(getter.get(), immediateValue);
        assertEquals(registers.getPC(), prevPC + 1);
    }

    // LD r, d8
    @Test void test_ld_0x06() { testLoadMemToReg(0x06, registers::getB); }
    @Test void test_ld_0x0E() { testLoadMemToReg(0x0E, registers::getC); }
    @Test void test_ld_0x16() { testLoadMemToReg(0x16, registers::getD); }
    @Test void test_ld_0x1E() { testLoadMemToReg(0x1E, registers::getE); }
    @Test void test_ld_0x26() { testLoadMemToReg(0x26, registers::getH); }
    @Test void test_ld_0x2E() { testLoadMemToReg(0x2E, registers::getL); }
    @Test void test_ld_0x3E() { testLoadMemToReg(0x3E, registers::getA); }

    // LD B, r
    @Test void test_ld_0x40() { testLoadRegtoReg(0x40, registers::getB, registers::getB, registers::setB, registers::setB); }
    @Test void test_ld_0x41() { testLoadRegtoReg(0x41, registers::getB, registers::getC, registers::setB, registers::setC); }
    @Test void test_ld_0x42() { testLoadRegtoReg(0x42, registers::getB, registers::getD, registers::setB, registers::setD); }
    @Test void test_ld_0x43() { testLoadRegtoReg(0x43, registers::getB, registers::getE, registers::setB, registers::setE); }
    @Test void test_ld_0x44() { testLoadRegtoReg(0x44, registers::getB, registers::getH, registers::setB, registers::setH); }
    @Test void test_ld_0x45() { testLoadRegtoReg(0x45, registers::getB, registers::getL, registers::setB, registers::setL); }
    @Test void test_ld_0x47() { testLoadRegtoReg(0x47, registers::getB, registers::getA, registers::setB, registers::setA); }

    // LD C, r
    @Test void test_ld_0x48() { testLoadRegtoReg(0x48, registers::getC, registers::getB, registers::setC, registers::setB); }
    @Test void test_ld_0x49() { testLoadRegtoReg(0x49, registers::getC, registers::getC, registers::setC, registers::setC); }
    @Test void test_ld_0x4A() { testLoadRegtoReg(0x4A, registers::getC, registers::getD, registers::setC, registers::setD); }
    @Test void test_ld_0x4B() { testLoadRegtoReg(0x4B, registers::getC, registers::getE, registers::setC, registers::setE); }
    @Test void test_ld_0x4C() { testLoadRegtoReg(0x4C, registers::getC, registers::getH, registers::setC, registers::setH); }
    @Test void test_ld_0x4D() { testLoadRegtoReg(0x4D, registers::getC, registers::getL, registers::setC, registers::setL); }
    @Test void test_ld_0x4F() { testLoadRegtoReg(0x4F, registers::getC, registers::getA, registers::setC, registers::setA); }

    // LD D, r
    @Test void test_ld_0x50() { testLoadRegtoReg(0x50, registers::getD, registers::getB, registers::setD, registers::setB); }
    @Test void test_ld_0x51() { testLoadRegtoReg(0x51, registers::getD, registers::getC, registers::setD, registers::setC); }
    @Test void test_ld_0x52() { testLoadRegtoReg(0x52, registers::getD, registers::getD, registers::setD, registers::setD); }
    @Test void test_ld_0x53() { testLoadRegtoReg(0x53, registers::getD, registers::getE, registers::setD, registers::setE); }
    @Test void test_ld_0x54() { testLoadRegtoReg(0x54, registers::getD, registers::getH, registers::setD, registers::setH); }
    @Test void test_ld_0x55() { testLoadRegtoReg(0x55, registers::getD, registers::getL, registers::setD, registers::setL); }
    @Test void test_ld_0x57() { testLoadRegtoReg(0x57, registers::getD, registers::getA, registers::setD, registers::setA); }

    // LD E, r
    @Test void test_ld_0x58() { testLoadRegtoReg(0x58, registers::getE, registers::getB, registers::setE, registers::setB); }
    @Test void test_ld_0x59() { testLoadRegtoReg(0x59, registers::getE, registers::getC, registers::setE, registers::setC); }
    @Test void test_ld_0x5A() { testLoadRegtoReg(0x5A, registers::getE, registers::getD, registers::setE, registers::setD); }
    @Test void test_ld_0x5B() { testLoadRegtoReg(0x5B, registers::getE, registers::getE, registers::setE, registers::setE); }
    @Test void test_ld_0x5C() { testLoadRegtoReg(0x5C, registers::getE, registers::getH, registers::setE, registers::setH); }
    @Test void test_ld_0x5D() { testLoadRegtoReg(0x5D, registers::getE, registers::getL, registers::setE, registers::setL); }
    @Test void test_ld_0x5F() { testLoadRegtoReg(0x5F, registers::getE, registers::getA, registers::setE, registers::setA); }

    // LD H, r
    @Test void test_ld_0x60() { testLoadRegtoReg(0x60, registers::getH, registers::getB, registers::setH, registers::setB); }
    @Test void test_ld_0x61() { testLoadRegtoReg(0x61, registers::getH, registers::getC, registers::setH, registers::setC); }
    @Test void test_ld_0x62() { testLoadRegtoReg(0x62, registers::getH, registers::getD, registers::setH, registers::setD); }
    @Test void test_ld_0x63() { testLoadRegtoReg(0x63, registers::getH, registers::getE, registers::setH, registers::setE); }
    @Test void test_ld_0x64() { testLoadRegtoReg(0x64, registers::getH, registers::getH, registers::setH, registers::setH); }
    @Test void test_ld_0x65() { testLoadRegtoReg(0x65, registers::getH, registers::getL, registers::setH, registers::setL); }
    @Test void test_ld_0x67() { testLoadRegtoReg(0x67, registers::getH, registers::getA, registers::setH, registers::setA); }

    // LD L, r
    @Test void test_ld_0x68() { testLoadRegtoReg(0x68, registers::getL, registers::getB, registers::setL, registers::setB); }
    @Test void test_ld_0x69() { testLoadRegtoReg(0x69, registers::getL, registers::getC, registers::setL, registers::setC); }
    @Test void test_ld_0x6A() { testLoadRegtoReg(0x6A, registers::getL, registers::getD, registers::setL, registers::setD); }
    @Test void test_ld_0x6B() { testLoadRegtoReg(0x6B, registers::getL, registers::getE, registers::setL, registers::setE); }
    @Test void test_ld_0x6C() { testLoadRegtoReg(0x6C, registers::getL, registers::getH, registers::setL, registers::setH); }
    @Test void test_ld_0x6D() { testLoadRegtoReg(0x6D, registers::getL, registers::getL, registers::setL, registers::setL); }
    @Test void test_ld_0x6F() { testLoadRegtoReg(0x6F, registers::getL, registers::getA, registers::setL, registers::setA); }

    // LD A, r
    @Test void test_ld_0x78() { testLoadRegtoReg(0x78, registers::getA, registers::getB, registers::setA, registers::setB); }
    @Test void test_ld_0x79() { testLoadRegtoReg(0x79, registers::getA, registers::getC, registers::setA, registers::setC); }
    @Test void test_ld_0x7A() { testLoadRegtoReg(0x7A, registers::getA, registers::getD, registers::setA, registers::setD); }
    @Test void test_ld_0x7B() { testLoadRegtoReg(0x7B, registers::getA, registers::getE, registers::setA, registers::setE); }
    @Test void test_ld_0x7C() { testLoadRegtoReg(0x7C, registers::getA, registers::getH, registers::setA, registers::setH); }
    @Test void test_ld_0x7D() { testLoadRegtoReg(0x7D, registers::getA, registers::getL, registers::setA, registers::setL); }
    @Test void test_ld_0x7F() { testLoadRegtoReg(0x7F, registers::getA, registers::getA, registers::setA, registers::setA); }
}

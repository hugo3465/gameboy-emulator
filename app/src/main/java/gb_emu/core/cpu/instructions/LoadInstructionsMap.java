package gb_emu.core.cpu.instructions;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import gb_emu.core.Instruction;
import gb_emu.core.cpu.CPU;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class LoadInstructionsMap extends AbstractInstruction implements InstructionSet {
    public LoadInstructionsMap(CPU cpu, CPURegisters registers, MMU mmu) {
        super(cpu, registers, mmu);
    }

    private int pop16() {
        int low = mmu.read(registers.getSP()) & 0xFF;
        registers.incrementSP();
        int high = mmu.read(registers.getSP()) & 0xFF;
        registers.incrementSP();
        return (high << 8) | low;
    }

    private void ld_r_n(Consumer<Integer> setter, int value) {
        setter.accept(value);
        registers.incrementPC();
    }

    private void ld_r_r(Consumer<Integer> setter, Supplier<Integer> getter) {
        int value = getter.get();
        setter.accept(value);
    }

    private void ld_r_pair(Consumer<Integer> setter, Supplier<Integer> getPair) {
        int pair = getPair.get();
        int value = mmu.read(pair);
        registers.setB(value);
    }

    private void ld_pair_r(Supplier<Integer> getPair, Supplier<Integer> getReg) {
        int pair = getPair.get();
        int value = getReg.get();
        mmu.write(pair, value);
    }

    @Override
    public void registerAll(HashMap<Integer, Instruction> functions) {
        /**
         * LD
         */

        // LD B, r

        functions.put(0x06, wrap(() -> ld_r_n(registers::setB, readImmediate8()), 8)); // LD B, d8

        functions.put(0x40, wrap(() -> ld_r_r(registers::setB, registers::getB), 4)); // LD B, B

        functions.put(0x41, wrap(() -> ld_r_r(registers::setB, registers::getC), 4)); // LD B, C

        functions.put(0x42, wrap(() -> ld_r_r(registers::setB, registers::getD), 4)); // LD B, D

        functions.put(0x43, wrap(() -> ld_r_r(registers::setB, registers::getE), 4)); // LD B, E

        functions.put(0x44, wrap(() -> ld_r_r(registers::setB, registers::getH), 4)); // LD B, H

        functions.put(0x45, wrap(() -> ld_r_r(registers::setB, registers::getL), 4)); // LD B, L

        functions.put(0x46, wrap(() -> ld_r_pair(registers::setB, registers::getHL), 8)); // LD B, (HL)

        functions.put(0x47, wrap(() -> ld_r_r(registers::setB, registers::getA), 4)); // LD B, A

        // LD C, r

        functions.put(0x0E, wrap(() -> ld_r_n(registers::setC, readImmediate8()), 8)); // LD C, d8

        functions.put(0x48, wrap(() -> ld_r_r(registers::setC, registers::getB), 4)); // LD C, B

        functions.put(0x49, wrap(() -> ld_r_r(registers::setC, registers::getC), 4)); // LD C, C

        functions.put(0x4A, wrap(() -> ld_r_r(registers::setC, registers::getD), 4)); // LD C, D

        functions.put(0x4B, wrap(() -> ld_r_r(registers::setC, registers::getE), 4)); // LD C, E

        functions.put(0x4C, wrap(() -> ld_r_r(registers::setC, registers::getH), 4)); // LD C, H

        functions.put(0x4D, wrap(() -> ld_r_r(registers::setC, registers::getL), 4)); // LD C, L

        functions.put(0x4E, wrap(() -> ld_r_pair(registers::setC, registers::getHL), 8)); // LD C, (HL)

        functions.put(0x4F, wrap(() -> ld_r_r(registers::setC, registers::getA), 4)); // LD C, A

        // LD D, r
        functions.put(0x16, wrap(() -> ld_r_n(registers::setD, readImmediate8()), 8)); // LD D, d8

        functions.put(0x50, wrap(() -> ld_r_r(registers::setD, registers::getB), 4)); // LD D, B

        functions.put(0x51, wrap(() -> ld_r_r(registers::setD, registers::getC), 4)); // LD D, C

        functions.put(0x52, wrap(() -> ld_r_r(registers::setD, registers::getD), 4)); // LD D, D

        functions.put(0x53, wrap(() -> ld_r_r(registers::setD, registers::getE), 4)); // LD D, E

        functions.put(0x54, wrap(() -> ld_r_r(registers::setD, registers::getH), 4)); // LD D, H

        functions.put(0x55, wrap(() -> ld_r_r(registers::setD, registers::getL), 4)); // LD D, L

        functions.put(0x56, wrap(() -> ld_r_pair(registers::setD, registers::getHL), 8)); // LD D, (HL)

        functions.put(0x57, wrap(() -> ld_r_r(registers::setD, registers::getA), 4)); // LD D, A

        // LD E, r
        functions.put(0x1E, wrap(() -> ld_r_n(registers::setE, readImmediate8()), 8)); // LD E, d8

        functions.put(0x58, wrap(() -> ld_r_r(registers::setE, registers::getB), 4)); // LD E, B

        functions.put(0x59, wrap(() -> ld_r_r(registers::setE, registers::getC), 4)); // LD E, C

        functions.put(0x5A, wrap(() -> ld_r_r(registers::setE, registers::getD), 4)); // LD E, D

        functions.put(0x5B, wrap(() -> ld_r_r(registers::setE, registers::getE), 4)); // LD E, E

        functions.put(0x5C, wrap(() -> ld_r_r(registers::setE, registers::getH), 4)); // LD E, H

        functions.put(0x5D, wrap(() -> ld_r_r(registers::setE, registers::getL), 4)); // LD E, L

        functions.put(0x5E, wrap(() -> ld_r_pair(registers::setE, registers::getHL), 8)); // LD E, (HL)

        functions.put(0x5F, wrap(() -> ld_r_r(registers::setE, registers::getA), 4)); // LD E, A

        // LD H, r
        functions.put(0x26, wrap(() -> ld_r_n(registers::setH, readImmediate8()), 8)); // LD H, d8

        functions.put(0x60, wrap(() -> ld_r_r(registers::setH, registers::getB), 4)); // LD H, B

        functions.put(0x61, wrap(() -> ld_r_r(registers::setH, registers::getC), 4)); // LD H, C

        functions.put(0x62, wrap(() -> ld_r_r(registers::setH, registers::getD), 4)); // LD H, D

        functions.put(0x63, wrap(() -> ld_r_r(registers::setH, registers::getE), 4)); // LD H, E

        functions.put(0x64, wrap(() -> ld_r_r(registers::setH, registers::getH), 4)); // LD H, H

        functions.put(0x65, wrap(() -> ld_r_r(registers::setH, registers::getL), 4)); // LD H, L

        functions.put(0x66, wrap(() -> ld_r_pair(registers::setH, registers::getHL), 8)); // LD H, (HL)

        functions.put(0x67, wrap(() -> ld_r_r(registers::setH, registers::getA), 4)); // LD H, A

        // LD L, r
        functions.put(0x2E, wrap(() -> ld_r_n(registers::setL, readImmediate8()), 8)); // LD L, d8

        functions.put(0x68, wrap(() -> ld_r_r(registers::setL, registers::getB), 4)); // LD L, B

        functions.put(0x69, wrap(() -> ld_r_r(registers::setL, registers::getC), 4)); // LD L, C
        
        functions.put(0x6A, wrap(() -> ld_r_r(registers::setL, registers::getD), 4)); // LD L, D

        functions.put(0x6B, wrap(() -> ld_r_r(registers::setL, registers::getE), 4)); // LD L, E

        functions.put(0x6C, wrap(() -> ld_r_r(registers::setL, registers::getH), 4)); // LD L, H

        functions.put(0x6D, wrap(() -> ld_r_r(registers::setL, registers::getL), 4)); // LD L, L

        functions.put(0x6E, wrap(() -> ld_r_pair(registers::setL, registers::getHL), 8)); // LD L, (HL)

        functions.put(0x6F, wrap(() -> ld_r_r(registers::setL, registers::getA), 4)); // LD L, A

        // LD (HL), r
        functions.put(0x36, wrap(() -> {
            mmu.write(registers.getHL(), readImmediate8());
            registers.incrementPC();
        }, 12)); // LD (HL), d8

        functions.put(0x70, wrap(() -> ld_pair_r(registers::getHL, registers::getB), 8)); // LD (HL), B

        functions.put(0x71, wrap(() -> ld_pair_r(registers::getHL, registers::getC), 8)); // LD (HL), C

        functions.put(0x72, wrap(() -> ld_pair_r(registers::getHL, registers::getD), 8)); // LD (HL), D

        functions.put(0x73, wrap(() -> ld_pair_r(registers::getHL, registers::getE), 8)); // LD (HL), E

        functions.put(0x74, wrap(() -> ld_pair_r(registers::getHL, registers::getH), 8)); // LD (HL), H

        functions.put(0x75, wrap(() -> ld_pair_r(registers::getHL, registers::getL), 8)); // LD (HL), L

        functions.put(0x77, wrap(() -> ld_pair_r(registers::getHL, registers::getA), 8)); // LD (HL), A

        // LD A, r
        functions.put(0x1A, wrap(() -> ld_r_pair(registers::setA, registers::getDE), 8)); // LD A, (DE)

        functions.put(0x3E, wrap(() -> ld_r_n(registers::setA, readImmediate8()), 8)); // LD A, d8

        functions.put(0x78, wrap(() -> ld_r_r(registers::setA, registers::getB), 4)); // LD A, B

        functions.put(0x79, wrap(() -> ld_r_r(registers::setA, registers::getC), 4)); // LD A, C

        functions.put(0x7A, wrap(() -> ld_r_r(registers::setA, registers::getD), 4)); // LD A, D

        functions.put(0x7B, wrap(() -> ld_r_r(registers::setA, registers::getE), 4)); // LD A, E

        functions.put(0x7C, wrap(() -> ld_r_r(registers::setA, registers::getH), 4)); // LD A, H

        functions.put(0x7D, wrap(() -> ld_r_r(registers::setA, registers::getL), 4)); // LD A, L

        functions.put(0x7E, wrap(() -> ld_r_pair(registers::setA, registers::getHL), 8)); // LD A, (HL)

        functions.put(0x7F, wrap(() -> ld_r_r(registers::setA, registers::getA), 4)); // LD A, A

        // LD (BC), r
        functions.put(0x02, wrap(() -> ld_pair_r(registers::getBC, registers::getA), 8)); // LD (BC), A

        // LD (DE), r
        functions.put(0x12, wrap(() -> ld_pair_r(registers::getDE, registers::getA), 8)); // LD (DE), A

        // ===== Special LD instructions =====
        functions.put(0x22, wrap(() -> { // LD (HL+), A
            int address = registers.getHL();
            mmu.write(address, registers.getA());
            registers.setHL((address + 1) & 0xFFFF);
            registers.incrementPC();
        }, 8));

        functions.put(0x2A, wrap(() -> { // LD A, (HL+)
            int value = mmu.read(registers.getHL());
            registers.setA(value);
            registers.setHL((registers.getHL() + 1) & 0xFFFF);
            registers.incrementPC();
        }, 8));

        functions.put(0x32, wrap(() -> { // LD (HL-), A
            int address = registers.getHL();
            mmu.write(address, registers.getA());
            registers.setHL((address - 1) & 0xFFFF);
            registers.incrementPC();
        }, 8));

        functions.put(0x3A, wrap(() -> { // LD A, (HL-)
            int value = mmu.read(registers.getHL());
            registers.setA(value);
            registers.setHL((registers.getHL() - 1) & 0xFFFF);
            registers.incrementPC();
        }, 8));

        functions.put(0xE0, wrap(() -> { // LDH (a8), A
            int address = 0xFF00 + readImmediate8();
            mmu.write(address, registers.getA());
            registers.incrementPC();
        }, 12));

        functions.put(0xE2, wrap(() -> { // LD (C), A
            int address = 0xFF00 + registers.getC();
            mmu.write(address, registers.getA());
            registers.incrementPC();
        }, 8));

        functions.put(0xEA, wrap(() -> { // LD (a16), A
            int address = readImmediate16();
            mmu.write(address, registers.getA());
            registers.incrementPC(3);
        }, 16));

        functions.put(0xF0, wrap(() -> { // LDH A, (a8)
            int address = 0xFF00 + readImmediate8();
            int value = mmu.read(address);
            registers.setA(value);
            registers.incrementPC();
        }, 12));

        functions.put(0xF2, wrap(() -> { // LD A, (C)
            int address = 0xFF00 + registers.getC();
            int value = mmu.read(address);
            registers.setA(value);
            registers.incrementPC();
        }, 8));

        functions.put(0xFA, wrap(() -> { // LD A, (a16)
            int address = readImmediate16();
            int value = mmu.read(address);
            registers.setA(value);
            registers.incrementPC(3);
        }, 16));

        functions.put(0x01, wrap(() -> { // LD BC, d16
            int value = readImmediate16();
            registers.setBC(value);
            registers.incrementPC(3);
        }, 12));

        functions.put(0x08, wrap(() -> { // LD (a16), SP
            int address = readImmediate16();
            int sp = registers.getSP();
            mmu.write(address, sp & 0xFF); // low byte
            mmu.write((address + 1) & 0xFFFF, sp >> 8); // high byte
            registers.incrementPC(3);
        }, 20));

        functions.put(0x11, wrap(() -> { // LD DE, d16
            int value = readImmediate16();
            registers.setDE(value);
            registers.incrementPC(3);
        }, 12));

        functions.put(0x21, wrap(() -> { // LD HL, d16
            int value = readImmediate16();
            registers.setHL(value);
            registers.incrementPC(3);
        }, 12));

        functions.put(0x31, wrap(() -> { // LD SP, d16
            int value = readImmediate16();
            registers.setSP(value);
            registers.incrementPC(3);
        }, 12));

        functions.put(0xF8, wrap(() -> { // LD HL, SP+r8 (signed offset)
            int offset = (byte) readImmediate8();
            int sp = registers.getSP();
            int result = (sp + offset) & 0xFFFF;

            registers.setHL(result);

            registers.setFlagZ(false);
            registers.setFlagN(false);
            registers.setFlagH(((sp ^ offset ^ result) & 0x10) != 0);
            registers.setFlagC(((sp ^ offset ^ result) & 0x100) != 0);

            registers.incrementPC();
        }, 12));

        functions.put(0xF9, wrap(() -> { // LD SP, HL
            registers.setSP(registers.getHL());
            registers.incrementPC();
        }, 8));

        /**
         * POP
         */
        functions.put(0xC1, wrap(() -> { // POP BC
            int value = pop16();
            registers.setBC(value);
        }, 12));

        functions.put(0xD1, wrap(() -> { // POP DE
            int value = pop16();
            registers.setDE(value);
        }, 12));

        functions.put(0xE1, wrap(() -> { // POP HL
            int value = pop16();
            registers.setHL(value);
        }, 12));

        functions.put(0xF1, wrap(() -> { // POP AF
            int value = pop16();
            registers.setAF(value & 0xFFF0); // clear AF registers
        }, 12));

    }
}

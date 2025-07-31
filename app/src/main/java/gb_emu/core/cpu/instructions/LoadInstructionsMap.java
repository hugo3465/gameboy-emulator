package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.Instruction;
import gb_emu.core.cpu.CPU;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class LoadInstructionsMap extends AbstractInstruction implements InstructionSet {
    public LoadInstructionsMap(CPU cpu, CPURegisters registers, MMU mmu) {
        super(cpu, registers, mmu);
    }

    @Override
    public void registerAll(HashMap<Integer, Instruction> functions) {
        /**
         * LD
         */

        functions.put(0x02, wrap(() -> {
            mmu.write(registers.getBC(), registers.getA());
            registers.incrementPC(); // opcode 1 byte
        }, 8)); // LD (BC), A

        functions.put(0x12, wrap(() -> {
            mmu.write(registers.getDE(), registers.getA());
            registers.incrementPC();
        }, 8)); // LD (DE), A

        // LD B, r
        functions.put(0x06, wrap(() -> {
            registers.setB(readImmediate8());
            registers.incrementPC(2); // opcode + 1 byte imediato
        }, 8)); // LD B, d8

        functions.put(0x40, wrap(() -> {
            registers.setB(registers.getB());
            registers.incrementPC();
        }, 4)); // LD B, B

        functions.put(0x41, wrap(() -> {
            registers.setB(registers.getC());
            registers.incrementPC();
        }, 4)); // LD B, C

        functions.put(0x42, wrap(() -> {
            registers.setB(registers.getD());
            registers.incrementPC();
        }, 4)); // LD B, D

        functions.put(0x43, wrap(() -> {
            registers.setB(registers.getE());
            registers.incrementPC();
        }, 4)); // LD B, E

        functions.put(0x44, wrap(() -> {
            registers.setB(registers.getH());
            registers.incrementPC();
        }, 4)); // LD B, H

        functions.put(0x45, wrap(() -> {
            registers.setB(registers.getL());
            registers.incrementPC();
        }, 4)); // LD B, L

        functions.put(0x46, wrap(() -> {
            registers.setB(mmu.read(registers.getHL()));
            registers.incrementPC();
        }, 8)); // LD B, (HL)

        functions.put(0x47, wrap(() -> {
            registers.setB(registers.getA());
            registers.incrementPC();
        }, 4)); // LD B, A

        // LD C, r
        functions.put(0x0E, wrap(() -> {
            registers.setC(readImmediate8());
            registers.incrementPC(2);
        }, 8)); // LD C, d8

        functions.put(0x48, wrap(() -> {
            registers.setC(registers.getB());
            registers.incrementPC();
        }, 4)); // LD C, B

        functions.put(0x49, wrap(() -> {
            registers.setC(registers.getC());
            registers.incrementPC();
        }, 4)); // LD C, C

        functions.put(0x4A, wrap(() -> {
            registers.setC(registers.getD());
            registers.incrementPC();
        }, 4)); // LD C, D

        functions.put(0x4B, wrap(() -> {
            registers.setC(registers.getE());
            registers.incrementPC();
        }, 4)); // LD C, E

        functions.put(0x4C, wrap(() -> {
            registers.setC(registers.getH());
            registers.incrementPC();
        }, 4)); // LD C, H

        functions.put(0x4D, wrap(() -> {
            registers.setC(registers.getL());
            registers.incrementPC();
        }, 4)); // LD C, L

        functions.put(0x4E, wrap(() -> {
            registers.setC(mmu.read(registers.getHL()));
            registers.incrementPC();
        }, 8)); // LD C, (HL)

        functions.put(0x4F, wrap(() -> {
            registers.setC(registers.getA());
            registers.incrementPC();
        }, 4)); // LD C, A

        // LD D, r
        functions.put(0x16, wrap(() -> {
            registers.setD(readImmediate8());
            registers.incrementPC(2);
        }, 8)); // LD D, d8

        functions.put(0x50, wrap(() -> {
            registers.setD(registers.getB());
            registers.incrementPC();
        }, 4)); // LD D, B

        functions.put(0x51, wrap(() -> {
            registers.setD(registers.getC());
            registers.incrementPC();
        }, 4)); // LD D, C

        functions.put(0x52, wrap(() -> {
            registers.setD(registers.getD());
            registers.incrementPC();
        }, 4)); // LD D, D

        functions.put(0x53, wrap(() -> {
            registers.setD(registers.getE());
            registers.incrementPC();
        }, 4)); // LD D, E

        functions.put(0x54, wrap(() -> {
            registers.setD(registers.getH());
            registers.incrementPC();
        }, 4)); // LD D, H

        functions.put(0x55, wrap(() -> {
            registers.setD(registers.getL());
            registers.incrementPC();
        }, 4)); // LD D, L

        functions.put(0x56, wrap(() -> {
            registers.setD(mmu.read(registers.getHL()));
            registers.incrementPC();
        }, 8)); // LD D, (HL)

        functions.put(0x57, wrap(() -> {
            registers.setD(registers.getA());
            registers.incrementPC();
        }, 4)); // LD D, A

        // LD E, r
        functions.put(0x1E, wrap(() -> {
            registers.setE(readImmediate8());
            registers.incrementPC(2);
        }, 8)); // LD E, d8

        functions.put(0x58, wrap(() -> {
            registers.setE(registers.getB());
            registers.incrementPC();
        }, 4)); // LD E, B

        functions.put(0x59, wrap(() -> {
            registers.setE(registers.getC());
            registers.incrementPC();
        }, 4)); // LD E, C

        functions.put(0x5A, wrap(() -> {
            registers.setE(registers.getD());
            registers.incrementPC();
        }, 4)); // LD E, D

        functions.put(0x5B, wrap(() -> {
            registers.setE(registers.getE());
            registers.incrementPC();
        }, 4)); // LD E, E

        functions.put(0x5C, wrap(() -> {
            registers.setE(registers.getH());
            registers.incrementPC();
        }, 4)); // LD E, H

        functions.put(0x5D, wrap(() -> {
            registers.setE(registers.getL());
            registers.incrementPC();
        }, 4)); // LD E, L

        functions.put(0x5E, wrap(() -> {
            registers.setE(mmu.read(registers.getHL()));
            registers.incrementPC();
        }, 8)); // LD E, (HL)

        functions.put(0x5F, wrap(() -> {
            registers.setE(registers.getA());
            registers.incrementPC();
        }, 4)); // LD E, A

        // LD H, r
        functions.put(0x26, wrap(() -> {
            registers.setH(readImmediate8());
            registers.incrementPC(2);
        }, 8)); // LD H, d8

        functions.put(0x60, wrap(() -> {
            registers.setH(registers.getB());
            registers.incrementPC();
        }, 4)); // LD H, B

        functions.put(0x61, wrap(() -> {
            registers.setH(registers.getC());
            registers.incrementPC();
        }, 4)); // LD H, C

        functions.put(0x62, wrap(() -> {
            registers.setH(registers.getD());
            registers.incrementPC();
        }, 4)); // LD H, D

        functions.put(0x63, wrap(() -> {
            registers.setH(registers.getE());
            registers.incrementPC();
        }, 4)); // LD H, E

        functions.put(0x64, wrap(() -> {
            registers.setH(registers.getH());
            registers.incrementPC();
        }, 4)); // LD H, H

        functions.put(0x65, wrap(() -> {
            registers.setH(registers.getL());
            registers.incrementPC();
        }, 4)); // LD H, L

        functions.put(0x66, wrap(() -> {
            registers.setH(mmu.read(registers.getHL()));
            registers.incrementPC();
        }, 8)); // LD H, (HL)

        functions.put(0x67, wrap(() -> {
            registers.setH(registers.getA());
            registers.incrementPC();
        }, 4)); // LD H, A

        // LD L, r
        functions.put(0x2E, wrap(() -> {
            registers.setL(readImmediate8());
            registers.incrementPC(2);
        }, 8)); // LD L, d8

        functions.put(0x68, wrap(() -> {
            registers.setL(registers.getB());
            registers.incrementPC();
        }, 4)); // LD L, B

        functions.put(0x69, wrap(() -> {
            registers.setL(registers.getC());
            registers.incrementPC();
        }, 4)); // LD L, C

        functions.put(0x6A, wrap(() -> {
            registers.setL(registers.getD());
            registers.incrementPC();
        }, 4)); // LD L, D

        functions.put(0x6B, wrap(() -> {
            registers.setL(registers.getE());
            registers.incrementPC();
        }, 4)); // LD L, E

        functions.put(0x6C, wrap(() -> {
            registers.setL(registers.getH());
            registers.incrementPC();
        }, 4)); // LD L, H

        functions.put(0x6D, wrap(() -> {
            registers.setL(registers.getL());
            registers.incrementPC();
        }, 4)); // LD L, L

        functions.put(0x6E, wrap(() -> {
            registers.setL(mmu.read(registers.getHL()));
            registers.incrementPC();
        }, 8)); // LD L, (HL)

        functions.put(0x6F, wrap(() -> {
            registers.setL(registers.getA());
            registers.incrementPC();
        }, 4)); // LD L, A

        // LD (HL), r
        functions.put(0x36, wrap(() -> {
            mmu.write(registers.getHL(), readImmediate8());
            registers.incrementPC(2);
        }, 12)); // LD (HL), d8

        functions.put(0x70, wrap(() -> {
            mmu.write(registers.getHL(), registers.getB());
            registers.incrementPC();
        }, 8)); // LD (HL), B

        functions.put(0x71, wrap(() -> {
            mmu.write(registers.getHL(), registers.getC());
            registers.incrementPC();
        }, 8)); // LD (HL), C

        functions.put(0x72, wrap(() -> {
            mmu.write(registers.getHL(), registers.getD());
            registers.incrementPC();
        }, 8)); // LD (HL), D

        functions.put(0x73, wrap(() -> {
            mmu.write(registers.getHL(), registers.getE());
            registers.incrementPC();
        }, 8)); // LD (HL), E

        functions.put(0x74, wrap(() -> {
            mmu.write(registers.getHL(), registers.getH());
            registers.incrementPC();
        }, 8)); // LD (HL), H

        functions.put(0x75, wrap(() -> {
            mmu.write(registers.getHL(), registers.getL());
            registers.incrementPC();
        }, 8)); // LD (HL), L

        functions.put(0x77, wrap(() -> {
            mmu.write(registers.getHL(), registers.getA());
            registers.incrementPC();
        }, 8)); // LD (HL), A

        // LD A, r
        functions.put(0x1A, wrap(() -> {
            registers.setA(mmu.read(registers.getDE()));
            registers.incrementPC();
        }, 8)); // LD A, (DE)

        functions.put(0x3E, wrap(() -> {
            registers.setA(readImmediate8());
            registers.incrementPC(2);
        }, 8)); // LD A, d8

        functions.put(0x78, wrap(() -> {
            registers.setA(registers.getB());
            registers.incrementPC();
        }, 4)); // LD A, B

        functions.put(0x79, wrap(() -> {
            registers.setA(registers.getC());
            registers.incrementPC();
        }, 4)); // LD A, C

        functions.put(0x7A, wrap(() -> {
            registers.setA(registers.getD());
            registers.incrementPC();
        }, 4)); // LD A, D

        functions.put(0x7B, wrap(() -> {
            registers.setA(registers.getE());
            registers.incrementPC();
        }, 4)); // LD A, E

        functions.put(0x7C, wrap(() -> {
            registers.setA(registers.getH());
            registers.incrementPC();
        }, 4)); // LD A, H

        functions.put(0x7D, wrap(() -> {
            registers.setA(registers.getL());
            registers.incrementPC();
        }, 4)); // LD A, L

        functions.put(0x7E, wrap(() -> {
            registers.setA(mmu.read(registers.getHL()));
            registers.incrementPC();
        }, 8)); // LD A, (HL)

        functions.put(0x7F, wrap(() -> {
            registers.setA(registers.getA());
            registers.incrementPC();
        }, 4)); // LD A, A

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
            registers.incrementPC(2);
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
            registers.incrementPC(2);
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

            registers.incrementPC(2);
        }, 12));

        functions.put(0xF9, wrap(() -> { // LD SP, HL
            registers.setSP(registers.getHL());
            registers.incrementPC();
        }, 8));
    }
}

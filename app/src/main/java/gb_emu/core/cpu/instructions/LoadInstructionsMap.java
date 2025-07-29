package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.cpu.Registers;
import gb_emu.core.mem.MMU;

public class LoadInstructionsMap implements InstructionSet {
    private Registers registers;
    private MMU mmu;

    public LoadInstructionsMap(Registers registers, MMU mmu) {
        this.registers = registers;
        this.mmu = mmu;
    }

    private int readImmediate8() {
        int value = mmu.read(registers.getPC());
        registers.incrementPC();
        return value;
    }

    private int readImmediate16() {
        int low = mmu.read(registers.getPC());
        registers.incrementPC();
        int high = mmu.read(registers.getPC());
        registers.incrementPC();
        return (high << 8) | low;
    }

    @Override
    public void registerAll(HashMap<Integer, Runnable> functions) {
        functions.put(0x00, () -> { }); // NOP

        functions.put(0x02, () -> { mmu.write(registers.getBC(), registers.getA()); }); // LD (BC), A

        functions.put(0x12, () -> { mmu.write(registers.getDE(), registers.getA()); }); // LD (DE), A

        // LD B, r
        functions.put(0x06, () -> { registers.setB(readImmediate8()); }); // LD B, d8
        functions.put(0x40, () -> { registers.setB(registers.getB()); }); // LD B, B
        functions.put(0x41, () -> { registers.setB(registers.getC()); }); // LD B, C
        functions.put(0x42, () -> { registers.setB(registers.getD()); }); // LD B, D
        functions.put(0x43, () -> { registers.setB(registers.getE()); }); // LD B, E
        functions.put(0x44, () -> { registers.setB(registers.getH()); }); // LD B, H
        functions.put(0x45, () -> { registers.setB(registers.getL()); }); // LD B, L
        functions.put(0x46, () -> { registers.setB(mmu.read(registers.getHL())); }); // LD B, (HL)
        functions.put(0x47, () -> { registers.setB(registers.getA()); }); // LD B, A

        // LD C, r
        functions.put(0x0E, () -> { registers.setC(readImmediate8()); }); // LD C, d8
        functions.put(0x48, () -> { registers.setC(registers.getB()); }); // LD C, B
        functions.put(0x49, () -> { registers.setC(registers.getC()); }); // LD C, C
        functions.put(0x4A, () -> { registers.setC(registers.getD()); }); // LD C, D
        functions.put(0x4B, () -> { registers.setC(registers.getE()); }); // LD C, E
        functions.put(0x4C, () -> { registers.setC(registers.getH()); }); // LD C, H
        functions.put(0x4D, () -> { registers.setC(registers.getL()); }); // LD C, L
        functions.put(0x4E, () -> { registers.setC(mmu.read(registers.getHL())); }); // LD C, (HL)
        functions.put(0x4F, () -> { registers.setC(registers.getA()); }); // LD C, A

        // LD D, r
        functions.put(0x16, () -> { registers.setD(readImmediate8()); }); // LD D, d8
        functions.put(0x50, () -> { registers.setD(registers.getB()); }); // LD D, B
        functions.put(0x51, () -> { registers.setD(registers.getC()); }); // LD D, C
        functions.put(0x52, () -> { registers.setD(registers.getD()); }); // LD D, D
        functions.put(0x53, () -> { registers.setD(registers.getE()); }); // LD D, E
        functions.put(0x54, () -> { registers.setD(registers.getH()); }); // LD D, H
        functions.put(0x55, () -> { registers.setD(registers.getL()); }); // LD D, L
        functions.put(0x56, () -> { registers.setD(mmu.read(registers.getHL())); }); // LD D, (HL)
        functions.put(0x57, () -> { registers.setD(registers.getA()); }); // LD D, A

        // LD E, r
        functions.put(0x1E, () -> { registers.setE(readImmediate8()); }); // LD E, d8
        functions.put(0x58, () -> { registers.setE(registers.getB()); }); // LD E, B
        functions.put(0x59, () -> { registers.setE(registers.getC()); }); // LD E, C
        functions.put(0x5A, () -> { registers.setE(registers.getD()); }); // LD E, D
        functions.put(0x5B, () -> { registers.setE(registers.getE()); }); // LD E, E
        functions.put(0x5C, () -> { registers.setE(registers.getH()); }); // LD E, H
        functions.put(0x5D, () -> { registers.setE(registers.getL()); }); // LD E, L
        functions.put(0x5E, () -> { registers.setE(mmu.read(registers.getHL())); }); // LD E, (HL)
        functions.put(0x5F, () -> { registers.setE(registers.getA()); }); // LD E, A

        // LD H, r
        functions.put(0x26, () -> { registers.setH(readImmediate8()); }); // LD H, d8
        functions.put(0x60, () -> { registers.setH(registers.getB()); }); // LD H, B
        functions.put(0x61, () -> { registers.setH(registers.getC()); }); // LD H, C
        functions.put(0x62, () -> { registers.setH(registers.getD()); }); // LD H, D
        functions.put(0x63, () -> { registers.setH(registers.getE()); }); // LD H, E
        functions.put(0x64, () -> { registers.setH(registers.getH()); }); // LD H, H
        functions.put(0x65, () -> { registers.setH(registers.getL()); }); // LD H, L
        functions.put(0x66, () -> { registers.setH(mmu.read(registers.getHL())); }); // LD H, (HL)
        functions.put(0x67, () -> { registers.setH(registers.getA()); }); // LD H, A

        // LD L, r
        functions.put(0x2E, () -> { registers.setL(readImmediate8()); }); // LD L, d8
        functions.put(0x68, () -> { registers.setL(registers.getB()); }); // LD L, B
        functions.put(0x69, () -> { registers.setL(registers.getC()); }); // LD L, C
        functions.put(0x6A, () -> { registers.setL(registers.getD()); }); // LD L, D
        functions.put(0x6B, () -> { registers.setL(registers.getE()); }); // LD L, E
        functions.put(0x6C, () -> { registers.setL(registers.getH()); }); // LD L, H
        functions.put(0x6D, () -> { registers.setL(registers.getL()); }); // LD L, L
        functions.put(0x6E, () -> { registers.setL(mmu.read(registers.getHL())); }); // LD L, (HL)
        functions.put(0x6F, () -> { registers.setL(registers.getA()); }); // LD L, A

        // LD (HL), r
        functions.put(0x36, () -> { mmu.write(registers.getHL(), readImmediate8()); }); // LD (HL), d8
        functions.put(0x70, () -> { mmu.write(registers.getHL(), registers.getB()); }); // LD (HL), B
        functions.put(0x71, () -> { mmu.write(registers.getHL(), registers.getC()); }); // LD (HL), C
        functions.put(0x72, () -> { mmu.write(registers.getHL(), registers.getD()); }); // LD (HL), D
        functions.put(0x73, () -> { mmu.write(registers.getHL(), registers.getE()); }); // LD (HL), E
        functions.put(0x74, () -> { mmu.write(registers.getHL(), registers.getH()); }); // LD (HL), H
        functions.put(0x75, () -> { mmu.write(registers.getHL(), registers.getL()); }); // LD (HL), L
        functions.put(0x77, () -> { mmu.write(registers.getHL(), registers.getA()); }); // LD (HL), A

        // LD A, r
        functions.put(0x1A, () -> { registers.setA(mmu.read(registers.getDE())); }); // LD A, (DE)
        functions.put(0x3E, () -> { registers.setA(readImmediate8()); }); // LD A, d8
        functions.put(0x78, () -> { registers.setA(registers.getB()); }); // LD A, B
        functions.put(0x79, () -> { registers.setA(registers.getC()); }); // LD A, C
        functions.put(0x7A, () -> { registers.setA(registers.getD()); }); // LD A, D
        functions.put(0x7B, () -> { registers.setA(registers.getE()); }); // LD A, E
        functions.put(0x7C, () -> { registers.setA(registers.getH()); }); // LD A, H
        functions.put(0x7D, () -> { registers.setA(registers.getL()); }); // LD A, L
        functions.put(0x7E, () -> { registers.setA(mmu.read(registers.getHL())); }); // LD A, (HL)
        functions.put(0x7F, () -> { registers.setA(registers.getA()); }); // LD A, A

        // ===== Special LD instructions =====
        functions.put(0x22, () -> { // LD (HL+), A
            int address = registers.getHL();
            mmu.write(address, registers.getA());
            registers.setHL((address + 1) & 0xFFFF);
        });

        functions.put(0x2A, () -> { // LD A, (HL+)
            int value = mmu.read(registers.getHL());
            registers.setA(value);
            registers.setHL((registers.getHL() + 1) & 0xFFFF);
        });

        

        functions.put(0x32, () -> { // LD (HL-), A
            int address = registers.getHL();
            mmu.write(address, registers.getA());
            registers.setHL((address - 1) & 0xFFFF);
        });

        

        functions.put(0x3A, () -> { // LD A, (HL-)
            int value = mmu.read(registers.getHL());
            registers.setA(value);
            registers.setHL((registers.getHL() - 1) & 0xFFFF);
        });

        functions.put(0xE0, () -> { // LDH (a8), A
            int address = 0xFF00 + readImmediate8();
            mmu.write(address, registers.getA());
        });

        functions.put(0xE2, () -> { // LD (C), A
            int address = 0xFF00 + registers.getC();
            mmu.write(address, registers.getA());
        });

        functions.put(0xEA, () -> { // LD (a16), A
            int address = readImmediate16();
            mmu.write(address, registers.getA());
        });

        functions.put(0xF0, () -> { // LDH A, (a8)
            int address = 0xFF00 + readImmediate8();
            int value = mmu.read(address);
            registers.setA(value);
        });

        functions.put(0xF2, () -> { // LD A, (C)
            int address = 0xFF00 + registers.getC();
            int value = mmu.read(address);
            registers.setA(value);
        });

        functions.put(0xFA, () -> { // LD A, (a16)
            int address = readImmediate16();
            int value = mmu.read(address);
            registers.setA(value);
        });

        functions.put(0x01, () -> { // LD BC, d16
            int value = readImmediate16();
            registers.setBC(value);
        });

        functions.put(0x08, () -> { // LD (a16), SP
            int address = readImmediate16();
            int sp = registers.getSP();
            mmu.write(address, sp & 0xFF);           // low byte
            mmu.write((address + 1) & 0xFFFF, sp >> 8); // high byte
        });

        functions.put(0x11, () -> { // LD DE, d16
            int value = readImmediate16();
            registers.setDE(value);
        });
        
        functions.put(0x21, () -> { // LD HL, d16
            int value = readImmediate16();
            registers.setHL(value);
        });

        functions.put(0x31, () -> { // LD SP, d16
            int value = readImmediate16();
            registers.setSP(value);
        });

        functions.put(0xF8, () -> { // LD HL, SP+r8 (signed offset)
            int offset = (byte) readImmediate8(); // signed 8-bit
            int sp = registers.getSP();
            int result = (sp + offset) & 0xFFFF;

            registers.setHL(result);

            registers.setFlagZ(false);
            registers.setFlagN(false);
            registers.setFlagH(((sp ^ offset ^ (result & 0xFFFF)) & 0x10) != 0);
            registers.setFlagC(((sp ^ offset ^ (result & 0xFFFF)) & 0x100) != 0);
        });
        
        functions.put(0xF9, () -> { // LD SP, HL
            registers.setSP(registers.getHL());
        });
    }
}

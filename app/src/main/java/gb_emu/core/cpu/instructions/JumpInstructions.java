package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class JumpInstructions extends AbstractInstruction implements InstructionSet {

    public JumpInstructions(CPURegisters registers, MMU mmu) {
        super(registers, mmu);
    }

    private void rst(int address) {
        int pc = registers.getPC() + 1;
        int sp = registers.getSP();

        sp = (sp - 1) & 0xFFFF;
        mmu.write(sp, (pc >> 8) & 0xFF);
        sp = (sp - 1) & 0xFFFF;
        mmu.write(sp, pc & 0xFF);

        registers.setSP(sp);
        registers.setPC(address);
    }

    /**
     * Helper method to perform the call operation (push return address and jump)
     * @param address
     */
    private void call(int address) {
        int pc = registers.getPC();
        int returnAddress = (pc + 3) & 0xFFFF; // address after the CALL instruction (3 bytes)
        int sp = (registers.getSP() - 2) & 0xFFFF;

        // Push return address on stack (little endian)
        mmu.write(sp, returnAddress & 0xFF);
        mmu.write((sp + 1) & 0xFFFF, (returnAddress >> 8) & 0xFF);

        registers.setSP(sp);
        registers.setPC(address);
    }

    @Override
    public void registerAll(HashMap<Integer, Runnable> functions) {
        /**
         * JP
         */
        functions.put(0xC3, () -> { // JP a16
            int address = readImmediate16();
            registers.setPC(address);
        });

        /**
         * RST
         */
        functions.put(0xC7, () -> rst(0x00)); // RST 00H
        functions.put(0xCF, () -> rst(0x08)); // RST 08H
        functions.put(0xD7, () -> rst(0x10)); // RST 10H
        functions.put(0xDF, () -> rst(0x18)); // RST 18H
        functions.put(0xE7, () -> rst(0x20)); // RST 20H
        functions.put(0xEF, () -> rst(0x28)); // RST 28H
        functions.put(0xF7, () -> rst(0x30)); // RST 30H
        functions.put(0xFF, () -> rst(0x38)); // RST 38H

        /**
         * JR
         */
        functions.put(0x18, () -> { // JR r8 (unconditional jump)
            registers.incrementPC(); // skip opcode
            byte offset = (byte) readImmediate8(); // signed 8-bit offset
            int pc = registers.getPC();
            registers.setPC((pc + offset) & 0xFFFF);
        });

        functions.put(0x20, () -> { // JR NZ, r8 (jump if Zero flag is not set)
            registers.incrementPC();
            byte offset = (byte) readImmediate8();
            if (!registers.getFlagZ()) {
                int pc = registers.getPC();
                registers.setPC((pc + offset) & 0xFFFF);
            }
        });

        functions.put(0x28, () -> { // JR Z, r8 (jump if Zero flag is not set)
            registers.incrementPC();
            byte offset = (byte) readImmediate8();
            if (registers.getFlagZ()) {
                int pc = registers.getPC();
                registers.setPC((pc + offset) & 0xFFFF);
            }
        });

        functions.put(0x30, () -> { // JR NC, r8 (jump if Carry flag is not set)
            registers.incrementPC();
            byte offset = (byte) readImmediate8();
            if (!registers.getFlagC()) {
                int pc = registers.getPC();
                registers.setPC((pc + offset) & 0xFFFF);
            }
        });

        functions.put(0x38, () -> { // JR C, r8 (jump if Carry flag is set)
            registers.incrementPC();
            byte offset = (byte) readImmediate8();
            if (registers.getFlagC()) {
                int pc = registers.getPC();
                registers.setPC((pc + offset) & 0xFFFF);
            }
        });

        functions.put(0x2F, () -> { // CPL
            int a = registers.getA();
            a = a ^ 0xFF; // bitwise complement (XOR with 0xFF)
            registers.setA(a);

            registers.setFlagN(true);
            registers.setFlagH(true);

            registers.incrementPC();
        });

        /**
         * CALL
         */
        functions.put(0xC4, () -> { // CALL NZ, a16 (opcode 0xC4)
            int address = readImmediate16();
            if (!registers.getFlagZ()) { // if Z flag is reset (NZ)
                call(address);
            }
        });

        functions.put(0xD4, () -> { // CALL NC, a16 (opcode 0xD4)
            int address = readImmediate16();
            if (!registers.getFlagC()) { // if C flag is reset (NC)
                call(address);
            }
        });

        functions.put(0xCC, () -> { // CALL Z, a16 (opcode 0xCC)
            int address = readImmediate16();
            if (registers.getFlagZ()) { // if Z flag is set (Z)
                call(address);
            }
        });
        
        functions.put(0xCD, () -> { // CALL a16 (unconditional) (opcode 0xCD)
            int address = readImmediate16();
            call(address);
        });

        functions.put(0xDC, () -> { // CALL C, a16 (opcode 0xDC)
            int address = readImmediate16();
            if (registers.getFlagC()) { // if C flag is set (C)
                call(address);
            }
        });
    }
}

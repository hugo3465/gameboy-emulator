package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.Instruction;
import gb_emu.core.cpu.CPU;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class JumpInstructions extends AbstractInstruction implements InstructionSet {

    public JumpInstructions(CPU cpu, CPURegisters registers, MMU mmu) {
        super(cpu, registers, mmu);
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
     * 
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
    public void registerAll(HashMap<Integer, Instruction> functions) {
        /**
         * JP
         */
        functions.put(0xC3, wrap(() -> { // JP a16
            int address = readImmediate16();
            registers.setPC(address);
        }, 16));

        /**
         * RST
         */
        functions.put(0xC7, wrap(() -> rst(0x00), 16)); // RST 00H
        functions.put(0xCF, wrap(() -> rst(0x08), 16)); // RST 08H
        functions.put(0xD7, wrap(() -> rst(0x10), 16)); // RST 10H
        functions.put(0xDF, wrap(() -> rst(0x18), 16)); // RST 18H
        functions.put(0xE7, wrap(() -> rst(0x20), 16)); // RST 20H
        functions.put(0xEF, wrap(() -> rst(0x28), 16)); // RST 28H
        functions.put(0xF7, wrap(() -> rst(0x30), 16)); // RST 30H
        functions.put(0xFF, wrap(() -> rst(0x38), 16)); // RST 38H

        /**
         * JR
         */
        functions.put(0x18, wrap(() -> { // JR r8 (unconditional jump)
            registers.incrementPC(); // skip opcode
            byte offset = (byte) readImmediate8(); // signed 8-bit offset
            int pc = registers.getPC();
            registers.setPC((pc + offset) & 0xFFFF);
        }, 12));

        functions.put(0x20, wrap(() -> { // JR NZ, r8
            registers.incrementPC();
            byte offset = (byte) readImmediate8();
            if (!registers.getFlagZ()) {
                int pc = registers.getPC();
                registers.setPC((pc + offset) & 0xFFFF);
            }
        }, 12));

        functions.put(0x28, wrap(() -> { // JR Z, r8
            registers.incrementPC();
            byte offset = (byte) readImmediate8();
            if (registers.getFlagZ()) {
                int pc = registers.getPC();
                registers.setPC((pc + offset) & 0xFFFF);
            }
        }, 12));

        functions.put(0x30, wrap(() -> { // JR NC, r8
            registers.incrementPC();
            byte offset = (byte) readImmediate8();
            if (!registers.getFlagC()) {
                int pc = registers.getPC();
                registers.setPC((pc + offset) & 0xFFFF);
            }
        }, 12));

        functions.put(0x38, wrap(() -> { // JR C, r8
            registers.incrementPC();
            byte offset = (byte) readImmediate8();
            if (registers.getFlagC()) {
                int pc = registers.getPC();
                registers.setPC((pc + offset) & 0xFFFF);
            }
        }, 12));

        functions.put(0x2F, wrap(() -> { // CPL
            int a = registers.getA();
            a = a ^ 0xFF;
            registers.setA(a);

            registers.setFlagN(true);
            registers.setFlagH(true);

            registers.incrementPC();
        }, 4));

        /**
         * CALL
         */
        functions.put(0xC4, () -> { // CALL NZ, a16
            int address = readImmediate16();
            if (!registers.getFlagZ()) {
                call(address);
                return 24;
            } else {
                return 12;
            }
        });

        functions.put(0xCC, () -> { // CALL Z, a16
            int address = readImmediate16();
            if (registers.getFlagZ()) {
                call(address);
                return 24;
            } else {
                return 12;
            }
        });

        functions.put(0xD4, () -> { // CALL NC, a16
            int address = readImmediate16();
            if (!registers.getFlagC()) {
                call(address);
                return 24;
            } else {
                return 12;
            }
        });

        functions.put(0xDC, () -> { // CALL C, a16
            int address = readImmediate16();
            if (registers.getFlagC()) {
                call(address);
                return 24;
            } else {
                return 12;
            }
        });

        functions.put(0xCD, wrap(() -> { // CALL a16 (unconditional)
            int address = readImmediate16();
            call(address);
        }, 24));
    }
}

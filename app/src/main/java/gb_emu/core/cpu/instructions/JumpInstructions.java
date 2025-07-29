package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.cpu.Registers;
import gb_emu.core.mem.MMU;

public class JumpInstructions extends AbstractInstruction implements InstructionSet {

    public JumpInstructions(Registers registers, MMU mmu) {
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
    }
}

package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.Instruction;
import gb_emu.core.cpu.CPU;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class BitInstructions extends AbstractInstruction implements InstructionSet {
    public BitInstructions(CPU cpu, CPURegisters registers, MMU mmu) {
        super(cpu, registers, mmu);
    }

    @Override
    public void registerAll(HashMap<Integer, Instruction> functions) {

        functions.put(0x0F, wrap(() -> {
            int a = registers.getA();
            int carry = a & 0x01;
            a = ((a >> 1) | (carry << 7)) & 0xFF;
            registers.setA(a);

            registers.setFlagZ(false); // Zero flag reset
            registers.setFlagN(false); // Subtract flag reset
            registers.setFlagH(false); // Half Carry flag reset
            registers.setFlagC(carry != 0); // Carry flag set if bit 0 was 1
        }, 4)); // RRCA

        functions.put(0x17, wrap(() -> { // RLA
            int a = registers.getA();
            boolean carry = (a & 0x80) != 0;
            int result = ((a << 1) & 0xFF) | (registers.getFlagC() ? 1 : 0);

            registers.setA(result);
            registers.setFlagZ(false);
            registers.setFlagN(false);
            registers.setFlagH(false);
            registers.setFlagC(carry);
        }, 4));

        functions.put(0x07, wrap(() -> { // RLCA
            int a = registers.getA();
            boolean carry = (a & 0x80) != 0;
            int result = ((a << 1) & 0xFF) | (carry ? 1 : 0);

            registers.setA(result);
            registers.setFlagZ(false);
            registers.setFlagN(false);
            registers.setFlagH(false);
            registers.setFlagC(carry);
        }, 4));
    }
}

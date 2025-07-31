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
        functions.put(0x17, wrap(() -> { // RLA
            int a = registers.getA();
            boolean carry = (a & 0x80) != 0;
            int result = ((a << 1) & 0xFF) | (registers.getFlagC() ? 1 : 0);

            registers.setA(result);
            registers.setFlagZ(false);
            registers.setFlagN(false);
            registers.setFlagH(false);
            registers.setFlagC(carry);

            registers.incrementPC();
        }, 4));

    }
}

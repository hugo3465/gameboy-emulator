package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.Instruction;
import gb_emu.core.cpu.CPU;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class InstructionsMap {
    private HashMap<Integer, Instruction> functions = new HashMap<>();
    private HashMap<Integer, Instruction> prefixFunctions = new HashMap<>();

    public InstructionsMap(CPU cpu, MMU mmu, CPURegisters registers) {
        new LoadInstructionsMap(cpu, registers, mmu).registerAll(functions);
        new ArithmeticInstructionsMap(cpu, registers, mmu).registerAll(functions);
        new IncDecInstructions(cpu, registers, mmu).registerAll(functions);
        new JumpInstructions(cpu, registers, mmu).registerAll(functions);
        new MiscInstructions(cpu, registers, mmu).registerAll(functions);

        new PrefixCBInstructions(cpu, registers, mmu).registerAll(prefixFunctions);
    }

    public int execute(Integer id, boolean isPrefixCB) {
        Instruction instruction = (isPrefixCB) ? prefixFunctions.get(id) : functions.get(id);

        if (instruction != null) {
            return instruction.run();
        } else {
            throw new IllegalArgumentException("Instruction not found: " + id);
        }
    }
}
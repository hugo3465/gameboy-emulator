package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.Instruction;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class InstructionsMap {
    private HashMap<Integer, Instruction> functions = new HashMap<>();

    public InstructionsMap(MMU mmu, CPURegisters registers) {
        // register all functions
        new LoadInstructionsMap(registers, mmu).registerAll(functions);
        new ArithmeticInstructionsMap(registers, mmu).registerAll(functions);
        new IncDecInstructions(registers, mmu).registerAll(functions);
        new JumpInstructions(registers, mmu).registerAll(functions);
        new MiscInstructions(registers, mmu).registerAll(functions);
        new BitInstructions(registers, mmu).registerAll(functions);
    }

    public int execute(Integer id) {
        Instruction instruction = functions.get(id);
        if (instruction != null) {
            return instruction.run();
        } else {
            throw new IllegalArgumentException("Instruction not found: " + id);
        }
    }
}
package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class InstructionsMap {
    private HashMap<Integer, Runnable> functions = new HashMap<>();

    public InstructionsMap(MMU mmu, CPURegisters registers) {
        // register all functions
        new LoadInstructionsMap(registers, mmu).registerAll(functions);
        new ArithmeticInstructionsMap(registers, mmu).registerAll(functions);
        new IncDecInstructions(registers).registerAll(functions);
        new JumpInstructions(registers, mmu).registerAll(functions);
        new MiscInstructions(registers, mmu).registerAll(functions);
        new BitInstructions(registers, mmu).registerAll(functions);
    }

    public void execute(Integer id) {
        Runnable instruction = functions.get(id);
        if (instruction != null) {
            instruction.run();
        } else {
            throw new IllegalArgumentException("Instruction not found: " + id);
        }
    }
}
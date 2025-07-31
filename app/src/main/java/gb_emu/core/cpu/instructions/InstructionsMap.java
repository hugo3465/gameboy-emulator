package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.Instruction;
import gb_emu.core.cpu.CPU;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class InstructionsMap {
    private HashMap<Integer, Instruction> functions = new HashMap<>();

    public InstructionsMap(CPU cpu, MMU mmu, CPURegisters registers) {
        // register all functions
        new LoadInstructionsMap(cpu, registers, mmu).registerAll(functions);
        new ArithmeticInstructionsMap(cpu, registers, mmu).registerAll(functions);
        new IncDecInstructions(cpu, registers, mmu).registerAll(functions);
        new JumpInstructions(cpu, registers, mmu).registerAll(functions);
        new MiscInstructions(cpu, registers, mmu).registerAll(functions);
        new BitInstructions(cpu, registers, mmu).registerAll(functions);
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
package gb_emu.core.cpu;

import java.util.HashMap;

import gb_emu.core.cpu.instructions.ArithmeticInstructionsMap;
import gb_emu.core.cpu.instructions.LoadInstructionsMap;
import gb_emu.core.mem.MMU;
import gb_emu.core.mem.RAM;

public class InstructionsMap {
    private HashMap<Integer, Runnable> functions = new HashMap<>();

    public InstructionsMap(MMU mmu, RAM ram, Registers registers) {
        // register all functions
        new LoadInstructionsMap(registers, mmu).registerAll(functions);
        new ArithmeticInstructionsMap(registers, ram, mmu).registerAll(functions);
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
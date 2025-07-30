package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.Instruction;

public interface InstructionSet {
    void registerAll(HashMap<Integer, Instruction> functions);
}
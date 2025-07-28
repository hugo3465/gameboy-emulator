package gb_emu.core.cpu.instructions;

import java.util.HashMap;

public interface InstructionSet {
    void registerAll(HashMap<Integer, Runnable> functions);
}
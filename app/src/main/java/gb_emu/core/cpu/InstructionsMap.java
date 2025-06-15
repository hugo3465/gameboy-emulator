package gb_emu.core.cpu;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import gb_emu.core.RAM;

public class InstructionsMap {
    private RAM ram;
    private Registers registers;
    private HashMap<String, Consumer<List<Operand>>> functions = new HashMap<>();

    public InstructionsMap(RAM ram, Registers registers) {
        this.ram = ram;
        this.registers = registers;
    }

    public void execute(String id, List<Operand> args) {
        Consumer<List<Operand>> fn = functions.get(id);
        if (fn != null) {
            fn.accept(args);
        } else {
            throw new IllegalArgumentException("Instruction not found: " + id);
        }
    }

    private void registerFunction(String id, Consumer<List<Operand>> fn) {
        functions.put(id, fn);
    }

    {
        registerFunction("LD", (args) -> {
            Operand dest = args.get(0);
            Operand src = args.get(1);

            int valueToWrite = 0;

            switch (src.getType()) {
                case IMMEDIATE:
                    valueToWrite = src.getValue();
                    break;
                case REGISTER:
                    valueToWrite = registers.getRegister(src.getRegisterName());
                    break;
                case MEMORY:
                    valueToWrite = ram.read(src.getValue());
            }

            if (dest.getType() == Operand.Type.REGISTER) {
                registers.setRegister(dest.getRegisterName(), valueToWrite);
            } else if (dest.getType() == Operand.Type.MEMORY) {
                ram.write(dest.getValue(), valueToWrite);
            } else {
                throw new IllegalArgumentException("Cannot write to immediate value");
            }
        });

    }
}

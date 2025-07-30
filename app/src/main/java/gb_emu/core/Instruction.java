package gb_emu.core;

@FunctionalInterface
public interface Instruction {
    int run(); // returns the number of cycles used
}

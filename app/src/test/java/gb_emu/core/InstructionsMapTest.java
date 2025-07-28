package gb_emu.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gb_emu.core.cpu.InstructionsMap;
import gb_emu.core.cpu.Registers;
import gb_emu.core.mem.RAM;

class InstructionsMapTest {

    // private RAM ram;
    // private Registers registers;
    // private InstructionsMap instructionsMap;

    // @BeforeEach
    // void setup() {
    //     ram = new RAM();
    //     registers = new Registers();
    //     instructionsMap = new InstructionsMap(ram, registers);
    // }

    // @Test
    // void testLD_RegisterImmediate() {
    //     // LD B, 42
    //     instructionsMap.execute("LD", List.of(
    //             Operand.register("B"),
    //             Operand.immediate(42)
    //     ));

    //     assertEquals(42, registers.getB());
    // }

    // @Test
    // void testLD_RegisterToRegister() {
    //     // First put the C value
    //     registers.setC(99);

    //     // LD B, C
    //     instructionsMap.execute("LD", List.of(
    //             Operand.register("B"),
    //             Operand.register("C")
    //     ));

    //     assertEquals(99, registers.getB());
    // }

    // @Test
    // void testLD_MemoryToRegister() {
    //     ram.write(0xA00, 77);

    //     // LD A, (0xA00)
    //     instructionsMap.execute("LD", List.of(
    //             Operand.register("A"),
    //             Operand.memory(0xA00)
    //     ));

    //     assertEquals(77, registers.getA());
    // }

    // @Test
    // void testLD_RegisterToMemory() {
    //     registers.setA(123);

    //     // LD (0xA00), A
    //     instructionsMap.execute("LD", List.of(
    //             Operand.memory(0xA00),
    //             Operand.register("A")
    //     ));

    //     assertEquals(123, ram.read(0xA00));
    // }

    // @Test
    // void testLD_ImmediateToImmediate_ShouldThrow() {
    //     assertThrows(IllegalArgumentException.class, () -> {
    //         instructionsMap.execute("LD", List.of(
    //                 Operand.immediate(5),
    //                 Operand.immediate(10)
    //         ));
    //     });
    // }
}

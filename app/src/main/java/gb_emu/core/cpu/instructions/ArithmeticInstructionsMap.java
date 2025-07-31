package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.Instruction;
import gb_emu.core.cpu.CPU;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class ArithmeticInstructionsMap extends AbstractInstruction implements InstructionSet {

    public ArithmeticInstructionsMap(CPU cpu, CPURegisters registers, MMU mmu) {
        super(cpu, registers, mmu);
    }

    /**
     * Helper method to perform 8-bit ADD operation with flags
     * 
     * @param value Value to add to register A
     */
    private void add8(int value) {
        int a = registers.getA();
        int result = a + value;

        // Set flags
        registers.setFlagZ((result & 0xFF) == 0);
        registers.setFlagN(false);
        registers.setFlagH((a & 0xF) + (value & 0xF) > 0xF);
        registers.setFlagC(result > 0xFF);

        registers.setA(result & 0xFF);
        registers.incrementPC();
    }

    /**
     * Helper method to perform 8-bit ADC (Add with Carry) operation with flags
     * 
     * @param value Value to add to register A (plus carry)
     */
    private void adc8(int value) {
        int a = registers.getA();
        int carry = registers.getFlagC() ? 1 : 0;
        int result = a + value + carry;

        // Set flags
        registers.setFlagZ((result & 0xFF) == 0);
        registers.setFlagN(false);
        registers.setFlagH((a & 0xF) + (value & 0xF) + carry > 0xF);
        registers.setFlagC(result > 0xFF);

        registers.setA(result & 0xFF);
        registers.incrementPC();
    }

    /**
     * Helper method to perform 16-bit ADD operation with HL register
     * 
     * @param value 16-bit value to add to HL
     */
    private void add16(int value) {
        int hl = registers.getHL();
        int result = hl + value;

        // Set flags (only N, H, C are affected)
        registers.setFlagN(false);
        registers.setFlagH((hl & 0xFFF) + (value & 0xFFF) > 0xFFF);
        registers.setFlagC(result > 0xFFFF);

        registers.setHL(result & 0xFFFF);
        registers.incrementPC();
    }

    /**
     * Helper method to perform ADD SP, r8 operation with special flag handling
     * 
     * @param offset Signed 8-bit offset to add to SP
     */
    private void addSP(byte offset) {
        int sp = registers.getSP();
        int result = sp + offset;

        // Special flag handling for SP operations
        registers.setFlagZ(false);
        registers.setFlagN(false);
        registers.setFlagH((sp & 0xF) + (offset & 0xF) > 0xF);
        registers.setFlagC((sp & 0xFF) + (offset & 0xFF) > 0xFF);

        registers.setSP(result & 0xFFFF);
        registers.incrementPC();
    }

    /**
     * Helper method to perform 8-bit SUB operation (A - value)
     * 
     * @param value Value to subtract from register A
     */
    private void sub8(int value) {
        int a = registers.getA();
        int result = a - value;

        // Set flags
        registers.setFlagZ((result & 0xFF) == 0);
        registers.setFlagN(true);
        registers.setFlagH((a & 0xF) < (value & 0xF));
        registers.setFlagC(a < value);

        registers.setA(result & 0xFF);
        registers.incrementPC();
    }

    /**
     * Helper method to perform 8-bit SBC (Subtract with Carry)
     * 
     * @param value Value to subtract from A along with carry
     */
    private void sbc8(int value) {
        int a = registers.getA();
        int carry = registers.getFlagC() ? 1 : 0;
        int result = a - value - carry;

        // Set flags
        registers.setFlagZ((result & 0xFF) == 0);
        registers.setFlagN(true);
        registers.setFlagH((a & 0xF) < ((value & 0xF) + carry));
        registers.setFlagC((a & 0xFF) < (value + carry));

        registers.setA(result & 0xFF);
        registers.incrementPC();
    }

    @Override
    public void registerAll(HashMap<Integer, Instruction> functions) {
        // ===== 8-bit ADD instructions =====

        functions.put(0x80, wrap(() -> add8(registers.getB()), 4)); // ADD A, B
        functions.put(0x81, wrap(() -> add8(registers.getC()), 4)); // ADD A, C
        functions.put(0x82, wrap(() -> add8(registers.getD()), 4)); // ADD A, D
        functions.put(0x83, wrap(() -> add8(registers.getE()), 4)); // ADD A, E
        functions.put(0x84, wrap(() -> add8(registers.getH()), 4)); // ADD A, H
        functions.put(0x85, wrap(() -> add8(registers.getL()), 4)); // ADD A, L
        functions.put(0x86, wrap(() -> add8(mmu.read(registers.getHL())), 8)); // ADD A, (HL)
        functions.put(0x87, wrap(() -> add8(registers.getA()), 4)); // ADD A, A

        functions.put(0xC6, wrap(() -> { // ADD A, d8
            int value = readImmediate8();
            add8(value);
        }, 8));

        // ===== 8-bit ADC (Add with Carry) instructions =====

        functions.put(0x88, wrap(() -> adc8(registers.getB()), 4)); // ADC A, B
        functions.put(0x89, wrap(() -> adc8(registers.getC()), 4)); // ADC A, C
        functions.put(0x8A, wrap(() -> adc8(registers.getD()), 4)); // ADC A, D
        functions.put(0x8B, wrap(() -> adc8(registers.getE()), 4)); // ADC A, E
        functions.put(0x8C, wrap(() -> adc8(registers.getH()), 4)); // ADC A, H
        functions.put(0x8D, wrap(() -> adc8(registers.getL()), 4)); // ADC A, L
        functions.put(0x8E, wrap(() -> adc8(mmu.read(registers.getHL())), 8)); // ADC A, (HL)
        functions.put(0x8F, wrap(() -> adc8(registers.getA()), 4)); // ADC A, A

        functions.put(0xCE, wrap(() -> { // ADC A, d8
            int value = readImmediate8();
            adc8(value);
        }, 8));

        // ===== 16-bit ADD instructions =====

        functions.put(0x09, wrap(() -> add16(registers.getBC()), 8)); // ADD HL, BC
        functions.put(0x19, wrap(() -> add16(registers.getDE()), 8)); // ADD HL, DE
        functions.put(0x29, wrap(() -> add16(registers.getHL()), 8)); // ADD HL, HL
        functions.put(0x39, wrap(() -> add16(registers.getSP()), 8)); // ADD HL, SP

        // ===== Special ADD SP instruction =====

        functions.put(0xE8, wrap(() -> { // ADD SP, r8
            byte offset = (byte) readImmediate8(); // signed 8-bit offset
            addSP(offset);
        }, 16));

        // ===== 8-bit SUB instructions =====

        functions.put(0x90, wrap(() -> sub8(registers.getB()), 4)); // SUB A, B
        functions.put(0x91, wrap(() -> sub8(registers.getC()), 4)); // SUB A, C
        functions.put(0x92, wrap(() -> sub8(registers.getD()), 4)); // SUB A, D
        functions.put(0x93, wrap(() -> sub8(registers.getE()), 4)); // SUB A, E
        functions.put(0x94, wrap(() -> sub8(registers.getH()), 4)); // SUB A, H
        functions.put(0x95, wrap(() -> sub8(registers.getL()), 4)); // SUB A, L
        functions.put(0x96, wrap(() -> sub8(mmu.read(registers.getHL())), 8)); // SUB A, (HL)
        functions.put(0x97, wrap(() -> sub8(registers.getA()), 4)); // SUB A, A

        functions.put(0xD6, wrap(() -> { // SUB A, d8
            int value = readImmediate8();
            sub8(value);
        }, 8));

        // ===== 8-bit SBC instructions =====

        functions.put(0x98, wrap(() -> sbc8(registers.getB()), 4)); // SBC A, B
        functions.put(0x99, wrap(() -> sbc8(registers.getC()), 4)); // SBC A, C
        functions.put(0x9A, wrap(() -> sbc8(registers.getD()), 4)); // SBC A, D
        functions.put(0x9B, wrap(() -> sbc8(registers.getE()), 4)); // SBC A, E
        functions.put(0x9C, wrap(() -> sbc8(registers.getH()), 4)); // SBC A, H
        functions.put(0x9D, wrap(() -> sbc8(registers.getL()), 4)); // SBC A, L
        functions.put(0x9E, wrap(() -> sbc8(mmu.read(registers.getHL())), 8)); // SBC A, (HL)
        functions.put(0x9F, wrap(() -> sbc8(registers.getA()), 4)); // SBC A, A

        functions.put(0xDE, wrap(() -> { // SBC A, d8
            int value = readImmediate8();
            sbc8(value);
        }, 8));

    }
}

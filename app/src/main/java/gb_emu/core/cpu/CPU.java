package gb_emu.core.cpu;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gb_emu.core.cpu.instructions.InstructionsMap;
import gb_emu.core.mem.MMU;

public class CPU implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CPU.class);

    private CPURegisters registers;
    private MMU mmu;
    private InstructionsMap instructionsMap;
    private boolean stopped = false;
    private boolean interruptsEnabled = true;

    public CPU(MMU mmu, CPURegisters registers) {
        this.mmu = mmu;
        this.registers = registers;

        this.instructionsMap = new InstructionsMap(this, mmu, registers);
    }

    public int step() {
        boolean halt = registers.getHalt();
        int cycles = 0;

        if (!stopped) {
            if (!halt) {
                cycles = executeInstruction();
            } else {
                cycles = 1;
            }

            // Handle Timer (Not implemented)

            handleInterrupts();

        } else {
            shouldWakeUp();
        }

        return cycles;
    }

    private int executeInstruction() {
        boolean isPrefixCbInstruction = false;
        int opcode = readOpcode();

        if (opcode == 0xCB) { // PREFIX CB
            opcode = readOpcode();
            isPrefixCbInstruction = true;
            LOGGER.debug("CB-Prefixed Opcode: " + String.format("0x%02X", opcode));
        } else {
            LOGGER.debug("Opcode: " + String.format("0x%02X", opcode));
        }

        LOGGER.debug("PC: " + String.format("0x%04X", registers.getPC()));

        return instructionsMap.execute(opcode, isPrefixCbInstruction);
    }

    private int readOpcode() {
        int opcode = mmu.read(registers.getPC());
        registers.incrementPC();
        return opcode;
    }

    /**
     * Check if CPU should wake up from STOP/HALT mode
     * 
     * @return true if should wake up
     */
    private boolean shouldWakeUp() {
        // Check for pending interrupts
        int interruptEnable = registers.getInterruptEnable(); // IE register
        int interruptFlags = registers.getInterruptFlags(); // IF register

        // LOGGER.debug("Interrupt Enable (IE): 0x" + String.format("%02X",
        // interruptEnable));
        // LOGGER.debug("Interrupt Flags (IF): 0x" + String.format("%02X",
        // interruptFlags));

        // If any enabled interrupt is pending, wake up
        boolean hasInterrupt = (interruptEnable & interruptFlags) != 0;

        // For STOP mode, also check for joypad input (bit 4)
        if (stopped) {
            boolean joypadInterrupt = (interruptEnable & interruptFlags & 0x10) != 0;
            if (joypadInterrupt) {
                LOGGER.debug("Joypad interrupt detected, waking up from STOP.");
                stopped = false; // wakey-wakey
                return true;
            }
        }

        return hasInterrupt;
    }

    /**
     * Handle interrupt processing
     */
    private void handleInterrupts() {
        if (!interruptsEnabled) {
            return;
        }

        int interruptEnable = registers.getInterruptEnable(); // IE register
        int interruptFlags = registers.getInterruptFlags(); // IF register

        int pendingInterrupts = interruptEnable & interruptFlags;

        if (pendingInterrupts != 0) {
            // Find highest priority interrupt (lowest bit number)
            for (int i = 0; i < 5; i++) {
                if ((pendingInterrupts & (1 << i)) != 0) {
                    // Clear the interrupt flag
                    mmu.write(0xFF0F, interruptFlags & ~(1 << i));

                    // Disable interrupts
                    interruptsEnabled = false;

                    // Push PC onto stack
                    registers.decrementSP();
                    mmu.write(registers.getSP(), (registers.getPC() >> 8) & 0xFF);
                    registers.decrementSP();
                    mmu.write(registers.getSP(), registers.getPC() & 0xFF);

                    // Jump to interrupt vector
                    switch (i) {
                        case 0:
                            registers.setPC(0x40);
                            break; // V-Blank
                        case 1:
                            registers.setPC(0x48);
                            break; // LCD STAT
                        case 2:
                            registers.setPC(0x50);
                            break; // Timer
                        case 3:
                            registers.setPC(0x58);
                            break; // Serial
                        case 4:
                            registers.setPC(0x60);
                            break; // Joypad
                    }

                    // LOGGER.debug("Interrupt {} triggered, jumping to 0x{:02X}", i,
                    // registers.getPC());
                    break;
                }
            }
        }
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean isInterruptsEnabled() {
        return interruptsEnabled;
    }

    public CPURegisters getRegisters() {
        return registers;
    }

    public void setInterruptsEnabled(boolean interruptsEnabled) {
        this.interruptsEnabled = interruptsEnabled;
        // LOGGER.debug("Interrupts {}", interruptsEnabled ? "enabled" : "disabled");
    }
}

package gb_emu.core.cpu;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gb_emu.core.GameBoy;
import gb_emu.core.cpu.instructions.InstructionsMap;
import gb_emu.core.mem.MMU;

public class CPU implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CPU.class);

    private GameBoy gb;
    private CPURegisters registers;
    private MMU mmu;
    private InstructionsMap instructionsMap;
    private boolean stopped = false;
    private boolean halted = false;
    private boolean interruptsEnabled = true;

    public CPU(GameBoy gb, MMU mmu) {
        this.gb = gb;
        this.mmu = mmu;

        this.registers = new CPURegisters();
        this.instructionsMap = new InstructionsMap(this, mmu, registers);
    }

    public int step() {
        // Check for interrupts that can wake up from STOP/HALT
        if (stopped || halted) {
            if (shouldWakeUp()) {
                stopped = false;
                halted = false;
                LOGGER.debug("CPU woke up from STOP/HALT mode");
            } else {
                // Still stopped/halted, consume minimal cycles
                return 4;
            }
        }

        int pc = registers.getPC();
        int opcode = mmu.read(pc);
        registers.incrementPC();

        if (opcode == 0xCB) { // PREFIX
            int cbOpcode = mmu.read(registers.getPC());
            registers.incrementPC();
            LOGGER.debug("CB-Prefixed Opcode: " + String.format("0x%02X", cbOpcode));
            return instructionsMap.execute(cbOpcode, true); // novo método!
        }

        LOGGER.debug("Opcode: " + String.format("0x%02X", opcode));
        LOGGER.debug("PC: " + String.format("0x%04X", pc));

        int cycles = instructionsMap.execute(opcode, false);

        // Handle interrupts if enabled
        if (interruptsEnabled) {
            handleInterrupts();
        }

        return cycles;
    }

    /**
     * Check if CPU should wake up from STOP/HALT mode
     * 
     * @return true if should wake up
     */
    private boolean shouldWakeUp() {
        // Check for pending interrupts
        int interruptEnable = mmu.read(0xFFFF) & 0xFF; // IE register
        int interruptFlags = mmu.read(0xFF0F) & 0xFF; // IF register

        // If any enabled interrupt is pending, wake up
        boolean hasInterrupt = (interruptEnable & interruptFlags) != 0;

        // For STOP mode, also check for joypad input (button press)
        if (stopped) {
            // Joypad interrupt (bit 4) can wake from STOP
            boolean joypadInterrupt = (interruptEnable & interruptFlags & 0x10) != 0;
            return joypadInterrupt;
        }

        // For HALT mode, any interrupt can wake up
        return hasInterrupt;
    }

    /**
     * Handle interrupt processing
     */
    private void handleInterrupts() {
        int interruptEnable = mmu.read(0xFFFF) & 0xFF; // IE register
        int interruptFlags = mmu.read(0xFF0F) & 0xFF; // IF register

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

                    LOGGER.debug("Interrupt {} triggered, jumping to 0x{:02X}", i, registers.getPC());
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

    public boolean isHalted() {
        return halted;
    }

    public void setHalted(boolean halted) {
        this.halted = halted;
        if (halted) {
            LOGGER.debug("CPU entered HALT mode");
        }
    }

    public boolean isInterruptsEnabled() {
        return interruptsEnabled;
    }

    public void setInterruptsEnabled(boolean interruptsEnabled) {
        this.interruptsEnabled = interruptsEnabled;
        LOGGER.debug("Interrupts {}", interruptsEnabled ? "enabled" : "disabled");
    }
}

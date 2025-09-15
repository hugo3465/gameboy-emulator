package gb_emu.ui.debugScreen;

import gb_emu.core.GameBoy;

import javax.swing.*;
import java.awt.*;

public class DebugScreen extends JFrame {


    public DebugScreen(GameBoy gb) {
        setTitle("Debug - Game Boy Emulator");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // CU Registers Tab
        CPURegistersPanel cpuRegistersPanel = new CPURegistersPanel(gb.getCpu().getRegisters());
        tabbedPane.addTab("CPU Registers", cpuRegistersPanel);

        // PPU Registers Tab
        PPURegistersPanel ppuRegistersPanel = new PPURegistersPanel(gb.getPPU().getRegisters());
        tabbedPane.addTab("PPU Registers", ppuRegistersPanel);


        // VRAM Tab
        VRAMPanel vramPanel = new VRAMPanel(gb.getPPU().getVram());
        tabbedPane.addTab("VRAM", vramPanel);

        // OAM Tab
        OAMPanel oamPanel = new OAMPanel(gb.getPPU().getOam());
        tabbedPane.addTab("OAM", oamPanel);

        // HRAM Tab
        JPanel hramPanel = new JPanel(new BorderLayout());
        JTextArea hramText = new JTextArea();
        hramText.setEditable(false);
        hramPanel.add(new JScrollPane(hramText), BorderLayout.CENTER);
        tabbedPane.addTab("HRAM", hramPanel);

        // Test Tab
        TestPannel testPannel = new TestPannel(gb.getPPU().getVram(), gb.getPPU().getBgPalette());
        gb.addObserver(testPannel);
        tabbedPane.addTab("Test", testPannel);

        // Add the tabbed pane to the frame
        add(tabbedPane);

        // Start a thread to update the debug information
        new Thread(() -> {
            while (true) {
                try {
                    cpuRegistersPanel.updateRegisters();
                    ppuRegistersPanel.updateRegisters();
                    vramPanel.updateVRAM();
                    oamPanel.updateOAM();

                    // Update HRAM
                    // hramText.setText(gb.getMemory().getHramDump());

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
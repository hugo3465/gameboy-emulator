package gb_emu.ui.debugScreen;

import gb_emu.core.ppu.VRAM;

import javax.swing.*;
import java.awt.*;

public class VRAMPanel extends JPanel {

    private VRAM vram;
    private JTextArea tileDataText;
    private JTextArea tileMap0Text;
    private JTextArea tileMap1Text;

    public VRAMPanel(VRAM vram) {
        this.vram = vram;
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tile Data Tab
        tileDataText = new JTextArea();
        tileDataText.setEditable(false);
        JScrollPane tileDataScrollPane = new JScrollPane(tileDataText);
        tabbedPane.addTab("Tile Data", tileDataScrollPane);

        // Tile Map 0 Tab
        tileMap0Text = new JTextArea();
        tileMap0Text.setEditable(false);
        JScrollPane tileMap0ScrollPane = new JScrollPane(tileMap0Text);
        tabbedPane.addTab("Tile Map 0", tileMap0ScrollPane);

        // Tile Map 1 Tab
        tileMap1Text = new JTextArea();
        tileMap1Text.setEditable(false);
        JScrollPane tileMap1ScrollPane = new JScrollPane(tileMap1Text);
        tabbedPane.addTab("Tile Map 1", tileMap1ScrollPane);

        add(tabbedPane, BorderLayout.CENTER);
    }

    public void updateVRAM() {
        // Update Tile Data
        StringBuilder tileDataBuilder = new StringBuilder();
        for (int tileIndex = 0; tileIndex < 384; tileIndex++) { // 384 tiles in VRAM
            tileDataBuilder.append(String.format("Tile %03d: ", tileIndex));
            for (int byteOffset = 0; byteOffset < VRAM.TILE_SIZE; byteOffset++) {
                tileDataBuilder.append(String.format("%02X ", vram.readTileByte(tileIndex, byteOffset)));
            }
            tileDataBuilder.append("\n");
        }
        tileDataText.setText(tileDataBuilder.toString());

        // Update Tile Map 0
        StringBuilder tileMap0Builder = new StringBuilder();
        for (int i = 0; i < 1024; i++) { // Tile Map 0 has 1024 entries
            if (i % 32 == 0) tileMap0Builder.append("\n"); // New line every 32 tiles
            tileMap0Builder.append(String.format("%02X ", vram.readTileMap0(i)));
        }
        tileMap0Text.setText(tileMap0Builder.toString());

        // Update Tile Map 1
        StringBuilder tileMap1Builder = new StringBuilder();
        for (int i = 0; i < 1024; i++) { // Tile Map 1 has 1024 entries
            if (i % 32 == 0) tileMap1Builder.append("\n"); // New line every 32 tiles
            tileMap1Builder.append(String.format("%02X ", vram.readTileMap1(i)));
        }
        tileMap1Text.setText(tileMap1Builder.toString());
    }
}

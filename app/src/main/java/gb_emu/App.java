package gb_emu;

import gb_emu.core.GameBoy;
import gb_emu.core.mem.cartridge.Cartridge;
import gb_emu.ui.Screen;
import gb_emu.ui.debugScreen.DebugScreen;

public class App {
    public static void main(String[] args) {
        String filePath = "/home/hugo-guimar-es/roms/GB/Dr. Mario.gb";
        // String filePath =
        // "/home/hugo-guimar-es/roms/GB/test_roms/blargg/cpu_instrs.gb";
        String botRomPath = "/home/hugo-guimar-es/roms/GB/bootix_dmg.bin";
        Cartridge cartridge = new Cartridge(filePath, botRomPath);
        System.out.println(cartridge.getCartridgeType());

        GameBoy gameBoy = new GameBoy(cartridge);
        
        
        DebugScreen debugScreen = new DebugScreen(gameBoy);
        debugScreen.setVisible(true);
        
        Screen screen = new Screen(160, 144);
        gameBoy.setObsever(screen);
        screen.start();
        
        gameBoy.start();
    }
}

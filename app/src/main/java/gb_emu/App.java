package gb_emu;

import gb_emu.core.GameBoy;
import gb_emu.core.mem.cartridge.Cartridge;
import gb_emu.ui.Screen;

public class App {
    public static void main(String[] args) {
        String filePath = "/home/hugo-guimar-es/roms/gb/Tetris (World).gb";
        Cartridge cartridge = new Cartridge(filePath);
        System.out.println(cartridge.getCartridgeType());

        GameBoy gameBoy = new GameBoy(cartridge);
        
        new Thread(() -> {
            gameBoy.start();
        }).start();
        
        new Thread(() -> {
            Screen screen = new Screen(160, 144, gameBoy);
            screen.start();
        }).start();
    }
}

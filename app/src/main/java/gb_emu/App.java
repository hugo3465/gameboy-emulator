package gb_emu;

import gb_emu.core.GameBoy;
import gb_emu.core.mem.cartridge.Cartridge;
import gb_emu.ui.Screen;

public class App {
    public static void main(String[] args) {
        String filePath = "/home/hugo-guimar-es/roms/gb/Tetris (World).gb";
        String botRomPath ="/home/hugo-guimar-es/roms/gb/bootix_dmg.bin";
        Cartridge cartridge = new Cartridge(filePath, botRomPath);
        System.out.println(cartridge.getCartridgeType());

        GameBoy gameBoy = new GameBoy(cartridge);

        new Thread(() -> {
            gameBoy.start();
        }).start();

        Screen screen = new Screen(160, 144, gameBoy);
        screen.start();
    }
}

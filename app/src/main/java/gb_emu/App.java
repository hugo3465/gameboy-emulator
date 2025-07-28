package gb_emu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        String filePath = "/home/hugo-guimar-es/Documents/Tetris (World).gb";
        File file = new File(filePath);
        byte[] romData = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            int bytesRead = fis.read(romData);
            if(bytesRead != romData.length) {
                throw new IOException("Error while reading the ROM file.");
            }
        } catch (Exception e) {
            System.out.println("Unexpected Error!");
        }

        // printa como deve de ser os bytes
        for (int i = 0; i < romData.length; i++) {
            int unsigned = romData[i] & 0xFF; // tira o sinal
            System.out.print(unsigned); // mo
            if ((i + 1) % 16 == 0) System.out.println(); // Quebra de linha a cada 16 bytes
        }
        
        // return romData;
    }
}

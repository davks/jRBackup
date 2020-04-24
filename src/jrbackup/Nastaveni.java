/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrbackup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author david
 */
public class Nastaveni {

    private static String konsole;
    private static final String NAZEV_SOUBORU = "setting.txt";

    private static final String KLIC_KONSOLE = "konsole=";

    static {
        konsole = "xterm";

        if (!Files.exists(Paths.get(NAZEV_SOUBORU))) {
            try ( PrintWriter pw = new PrintWriter(new FileWriter(NAZEV_SOUBORU))) {
                pw.write("konsole=xterm -hold -e");
            } catch (IOException e) {
                System.out.println("Nepodarilo se vytvorit soubor s nastavenim.");
            }
        }

        if (Files.exists(Paths.get(NAZEV_SOUBORU))) {
            try ( BufferedReader br = new BufferedReader(new FileReader(NAZEV_SOUBORU))) {

                String radek;
                while ((radek = br.readLine()) != null) {
                    radek = radek.trim();

                    if (radek.startsWith("#")) {
                        continue;
                    }
                    if (radek.startsWith(KLIC_KONSOLE)) {
                        konsole = radek.replace(KLIC_KONSOLE, "");
                    }
                }

            } catch (IOException e) {
            }
        }
    }

    public static String getKonsole() {
        return konsole;
    }

}

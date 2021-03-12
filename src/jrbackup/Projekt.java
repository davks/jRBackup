/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrbackup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 *
 * @author david
 */
public class Projekt {

    private String nazevProjektu;
    private String adresarProjektu;
    private String cestaKSouboru;
    private String koncovkaSouboru;

    // klic uvedeny v souboru s projektem
    public static final String DESCRIPTION = "description";
    public static final String CUSTOM_PARAMETERS = "parameters";
    public static final String EXCLUDE = "exclude";
    public static final String SOURCE = "source";
    public static final String TARGET = "target";
    public static final String INCLUDE = "include";
    public static final String SSH_PORT = "sshport";
    public static final String SEZNAM_PREPINACU = "prepinace";

    // parametry rsync, ktere je mozne v programu pouzit
    public static final String PARAMETER_SSH = "ssh";
    public static final String PARAMETER_R = "-r";
    public static final String PARAMETER_L = "-l";
    public static final String PARAMETER_P = "-p";
    public static final String PARAMETER_T = "-t";
    public static final String PARAMETER_G = "-g";
    public static final String PARAMETER_O = "-o";
    public static final String PARAMETER_BIG_D = "-D";
    public static final String PARAMETER_A = "-a";
    public static final String PARAMETER_NO_TIMES = "--no-times";
    public static final String PARAMETER_NO_PERMS = "--no-perms";
    public static final String PARAMETER_NO_OWNER = "--no-owner";
    public static final String PARAMETER_NO_GROUP = "--no-group";
    public static final String PARAMETER_OMIT_DIR_TIMES = "--omit-dir-times";
    public static final String PARAMETER_STATS = "--stats";
    public static final String PARAMETER_BIG_E = "-E";
    public static final String PARAMETER_BIG_A = "-A";
    public static final String PARAMETER_BIG_X = "-X";
    public static final String PARAMETER_BIG_L = "-L";
    public static final String PARAMETER_COMPRESS = "--compress";
    public static final String PARAMETER_DELETE = "--delete";
    public static final String PARAMETER_IGNORE_EXISTING = "--ignore-existing";
    public static final String PARAMETER_EXISTING = "--existing";
    public static final String PARAMETER_U = "-u";
    public static final String PARAMETER_BIG_P = "-P";
    public static final String PARAMETER_SIZE_ONLY = "--size-only";
    public static final String PARAMETER_CHECKSUM = "--checksum";
    public static final String PARAMETER_BIG_H = "-H";
    public static final String PARAMETER_NUMERIC_IDS = "--numeric-ids";
    public static final String PARAMETER_X = "-x";
    public static final String PARAMETER_PROGRESS = "--progress";
    public static final String PARAMETER_PRUNE_EMPTY_DIRS = "--prune-empty-dirs";

    public Projekt() {
        this("");
    }

    public Projekt(String nazevProjektu) {
        this(nazevProjektu, "projects");
    }

    public Projekt(String nazevProjektu, String adresarProjektu) {
        this.nazevProjektu = nazevProjektu;
        this.adresarProjektu = adresarProjektu;
        this.koncovkaSouboru = ".txt";
        this.cestaKSouboru = adresarProjektu + "/" + nazevProjektu + koncovkaSouboru;
        new File(adresarProjektu).mkdirs();
    }

    /**
     * Nastavit nazev projektu. Pokud je v nazvu i koncovka souboru, odstrani
     * se.
     *
     * @param nazevProjektu nazev projektu
     */
    public void setNazevProjektu(String nazevProjektu) {
        this.nazevProjektu = nazevProjektu;
        if (nazevProjektu.endsWith(koncovkaSouboru)) {
            this.nazevProjektu = nazevProjektu.replace(koncovkaSouboru, "");
        }
        cestaKSouboru = adresarProjektu + "/" + this.nazevProjektu + koncovkaSouboru;
    }

    /**
     * Nastavi se koncovka souboru daneho projektu.
     *
     * @param koncovka koncovka souboru
     */
    public void setKoncovkaSouboru(String koncovka) {
        koncovkaSouboru = koncovka;
    }

    /**
     * Nastavi se adresar, do ktereho se budou projekty ukladat.
     *
     * @param adresar nazev adresare
     */
    public void setAdresarProjektu(String adresar) {
        adresarProjektu = adresar;
    }

    /**
     * Ziskame nazev projektu.
     *
     * @return nazev projektu
     */
    public String getNazevProjektu() {
        return nazevProjektu;
    }

    /**
     * Ziskame koncovku souboru projektu.
     *
     * @return koncovka souboru
     */
    public String getKoncovkaSouboru() {
        return koncovkaSouboru;
    }

    /**
     * Zjistujeme existenci souboru s projektem.
     *
     * @return vrati ano/ne podle toho, jestli soubor existuje
     */
    public boolean existujeSouborProjektu() {
        return Files.exists(Paths.get(cestaKSouboru));
    }

    /**
     * Cteme soubor, radek po radku a zaznamenavame hodnoty. Soubor obsahuje
     * prepinace a dalsi volby k programu rsync. Pokud se volby nenaleznou,
     * nebude se nic zaznamenávat.
     *
     * @return seznam prepinacu programu rsync a dalsich voleb. Hodnota je
     * Object
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Map<String, Object> nacistSouborProjektu() throws FileNotFoundException, IOException {
        Map<String, Object> volby = new LinkedHashMap<>();
        List<String> seznamPrepinacu = new ArrayList<>();

        try ( BufferedReader br = new BufferedReader(new FileReader(cestaKSouboru))) {

            String radek;
            while ((radek = br.readLine()) != null) {
                radek = radek.trim();
                if (radek.startsWith("description=")) {
                    volby.put(DESCRIPTION, radek.replace("description=", "").replace("**", "\n"));
                } else if (radek.startsWith("parameters=")) {
                    volby.put(CUSTOM_PARAMETERS, radek.replace("parameters=", ""));
                } else if (radek.startsWith("exclude=")) {
                    volby.put(EXCLUDE, radek.replace("exclude=", "").replace("\t", "\n"));
                } else if (radek.startsWith("include=")) {
                    volby.put(INCLUDE, radek.replace("include=", "").replace("\t", "\n"));
                } else if (radek.startsWith("source=")) {
                    volby.put(SOURCE, radek.replace("source=", "").replace("\"", ""));
                } else if (radek.startsWith("target=")) {
                    volby.put(TARGET, radek.replace("target=", "").replace("\"", ""));
                } else if (radek.startsWith("sshport=")) {
                    volby.put(SSH_PORT, radek.replace("sshport=", ""));
                } else {
                    seznamPrepinacu.add(radek);
                }
            }
            volby.put(SEZNAM_PREPINACU, seznamPrepinacu);
        }
        return volby;
    }

    /**
     * Ulozime projekt pro program rsync jako obycejny textoveho souboru.
     *
     * @param description poznamka projektu
     * @param parameters vlastni, uzivatelem zadane parametry k rsync
     * @param sshport cislo portu ssh
     * @param exclude soubory a adresare, ktere se nebudou synchronizovat
     * @param include soubory a dresare, ktere se budou synchronizovat
     * @param source zdroj synchronizace
     * @param target cil synchronizace
     * @param rsyncVolby prepinace programu rsync
     * @throws IOException
     */
    public void ulozitProjekt(String description, String parameters, int sshport,
            String exclude, String include, String source, String target, List<String> rsyncVolby) throws IOException {

        StringBuilder prikaz = new StringBuilder();
        if (!description.trim().equals("")) {
            prikaz.append("description=").append(description.trim().replace("\n", "**")).append("\n");
        }
        if (!parameters.trim().equals("")) {
            prikaz.append("parameters=").append(parameters.trim()).append("\n");
        }
        if (!exclude.trim().equals("")) {
            prikaz.append("exclude=").append(exclude.trim().replace("\n", "\t")).append("\n");
        }
        if (!include.trim().equals("")) {
            prikaz.append("include=").append(include.trim().replace("\n", "\t")).append("\n");
        }
        if (sshport > 0) {
            prikaz.append("sshport=").append(String.valueOf(sshport)).append("\n");
        }
        
        
        prikaz.append("source=\"").append(source.trim()).append("\"\n");
        prikaz.append("target=\"").append(target.trim()).append("\"\n");

        rsyncVolby.forEach((volba) -> {
            prikaz.append(volba).append("\n");
        });

        try ( PrintWriter pw = new PrintWriter(new FileWriter(cestaKSouboru))) {
            pw.write(prikaz.toString() + "\n");
        }
    }

    /**
     * Ulozime projekt pro program rsync jako obycejny textoveho souboru.
     *
     * @param description poznamka projektu
     * @param nazevProjektu nazev projektu, ktery odstrani pripadnou koncovku
     * @param parameters vlastni, uzivatelem zadane parametry k rsync
     * @param sshport cislo ssh portu
     * @param exclude soubory a adresare, ktere se nebudou synchronizovat
     * @param include soubory a dresare, ktere se budou synchronizovat
     * @param source zdroj synchronizace
     * @param target cil synchronizace
     * @param rsyncVolby prepinace programu rsync
     * @throws IOException
     */
    public void ulozitProjekt(String nazevProjektu, String description, String parameters, int sshport,
            String exclude, String include, String source, String target, List<String> rsyncVolby) throws IOException {

        setNazevProjektu(nazevProjektu);
        ulozitProjekt(description, parameters, sshport, exclude, include, source, target, rsyncVolby);
    }

    /**
     * Ulozime soubor .rsync-filter, kde jsou ulozene informace o tom, co se
     * bude filtrovat.
     *
     * @param source sdresar, ktery se bude zalohovat
     * @param filter obsah souboru (vsechny filtry)
     * @throws IOException
     */
    public void ulozitSouborFilter(String source, String filter) throws IOException {
        String lomitko = source.endsWith("/") ? "" : "/";
        String cestaKSouboruFilter = source + lomitko + ".rsync-filter";

        File f = new File(cestaKSouboruFilter);

        if (filter.trim().equals("")) {
            if (f.exists()) {
                f.delete();
            }
        } else {
            try ( PrintWriter pw = new PrintWriter(new FileWriter(cestaKSouboruFilter))) {
                pw.write(filter.trim());
            }
        }
        
        /*if (f.exists() && filter.trim().equals("")) {
            f.delete();
        } else {
            try ( PrintWriter pw = new PrintWriter(new FileWriter(cestaKSouboruFilter))) {
                pw.write(filter.trim());
            }
        }*/
    }

    /**
     * Nacteme soubor .rsync-filter ulozeny v adresari toho, co se bude
     * zalohovat
     *
     * @param source adresar, ktery se bude zalohovat
     * @return obsah souboru .rsync-filter
     * @throws IOException
     */
    public String nacistSouborFilter(String source) throws IOException {
        String lomitko = source.endsWith("/") ? "" : "/";
        String cestaKSouboruFilter = source + lomitko + ".rsync-filter";
        String line;
        String obsah = "";

        File f = new File(cestaKSouboruFilter);

        if (f.exists()) {
            try ( BufferedReader br = new BufferedReader(new FileReader(cestaKSouboruFilter))) {
                while ((line = br.readLine()) != null) {
                    obsah += line + "\n";
                }
            }
        }

        return obsah;
    }

    /**
     * Odstrani projekt - soubor, ktery je uz vybrany.
     *
     * @throws IOException
     */
    public void odstranitVybranyProjekt() throws IOException {
        Files.deleteIfExists(Paths.get(cestaKSouboru));
    }

    /**
     * Prohleda adresar projektu a vytvori jejich seznam.
     *
     * @return seznam projektu
     */
    public List<String> ziskatSeznamProjektu() {
        List<String> seznam = new ArrayList<>();

        File folder = new File(adresarProjektu);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            Arrays.sort(listOfFiles, Comparator.comparing(a -> a.getName().toLowerCase()));
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(koncovkaSouboru)) {
                    if (file.getName().endsWith("exclude" + koncovkaSouboru)) {
                        continue;
                    }
                    seznam.add(file.getName().replace(koncovkaSouboru, ""));
                }
            }
        }
        return seznam;
    }

}

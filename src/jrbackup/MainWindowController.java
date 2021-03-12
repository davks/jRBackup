package jrbackup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;

public class MainWindowController implements Initializable {

    private Projekt projekt;

    private String version;

    private static final int VYSTUP_CELY = 0;
    private static final int VYSTUP_PRIDANE = 1;
    private static final int VYSTUP_SMAZANE = 2;
    private static final int VYSTUP_CHYBY = 3;

    private final ObservableList<String> seznamVystup;
    private final ObservableList<String> seznamPridano;
    private final ObservableList<String> seznamSmazano;
    private final ObservableList<String> seznamChyby;

    private final Map<CheckBox, ParametrPrikazu> seznamVsechPrepinacu = new LinkedHashMap<>();

    @FXML
    private ComboBox<String> cbxVybratProjekt;

    @FXML
    private Button btnUlozitProjekt;

    @FXML
    private Button btnOdstranitProjekt;

    @FXML
    private Button btnZobrazitPrikaz;

    @FXML
    private Button btnOtestovatPrikaz;

    @FXML
    private Button btnSpustitPrikaz;

    @FXML
    private TextField tfSource;

    @FXML
    private TextField tfTarget;

    @FXML
    private Button btnZdroj;

    @FXML
    private Button btnCil;

    @FXML
    private CheckBox chbSSH;

    @FXML
    private CheckBox chbSshpass;

    @FXML
    private CheckBox chbStats;

    @FXML
    private CheckBox chbArchive;

    @FXML
    private CheckBox chbNoPerms;

    @FXML
    private CheckBox chbNoOwner;

    @FXML
    private CheckBox chbNoTimes;

    @FXML
    private CheckBox chbNoGroups;

    @FXML
    private CheckBox chbOmitDirTimes;

    @FXML
    private CheckBox chbRecursive;

    @FXML
    private CheckBox chbLinks;

    @FXML
    private CheckBox chbPerms;

    @FXML
    private CheckBox chbTimes;

    @FXML
    private CheckBox chbGroup;

    @FXML
    private CheckBox chbOwner;

    @FXML
    private CheckBox chbD;

    @FXML
    private CheckBox chbE;

    @FXML
    private CheckBox chbProgress;

    @FXML
    private CheckBox chbCompress;

    @FXML
    private CheckBox chbDelete;

    @FXML
    private CheckBox chbIgnoreExisting;

    @FXML
    private CheckBox chbExisting;

    @FXML
    private CheckBox chbUpdate;

    @FXML
    private CheckBox chbX;

    @FXML
    private CheckBox chbSizeOnly;

    @FXML
    private CheckBox chbChecksum;

    @FXML
    private CheckBox chbP;

    @FXML
    private CheckBox chbNumericIds;

    @FXML
    private CheckBox chbHardLinks;

    @FXML
    private CheckBox chbAcls;

    @FXML
    private CheckBox chbXattrs;

    @FXML
    private CheckBox chbCopyLinks;

    @FXML
    private CheckBox chbKonsole;

    @FXML
    private CheckBox chbPruneEmptyDirs;

    @FXML
    private TextField tfCustomParameters;

    @FXML
    private TextField tfSshPort;

    @FXML
    private TextArea taExcludeFile;

    @FXML
    private TextArea taIncludeFile;

    @FXML
    private TextArea taDescription;

    @FXML
    private TextArea taFilterFile;

    @FXML
    private Label lblZprava;

    @FXML
    private ListView<String> lvVystup;

    @FXML
    private ListView<String> lvPridano;

    @FXML
    private ListView<String> lvSmazano;

    @FXML
    private ListView<String> lvChyby;

    @FXML
    private TabPane tpVystupy;

    public MainWindowController() {
        seznamVystup = FXCollections.observableArrayList();
        seznamPridano = FXCollections.observableArrayList();
        seznamSmazano = FXCollections.observableArrayList();
        seznamChyby = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        projekt = new Projekt(); // novy projekt bezejmena - doplni se az vybranim ze seznamu
        lvVystup.setItems(seznamVystup);
        lvPridano.setItems(seznamPridano);
        lvSmazano.setItems(seznamSmazano);
        lvChyby.setItems(seznamChyby);

        nastavitPrepinace();
        nastavitTooltip();
        ziskatSeznamProjektu(false);
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Vybere se projekt v ComboBoxu a nastavi se jednotlive polozky okna jako
     * ruzne prepinace (checkbox) a volby (textfield, textarea).
     */
    @FXML
    void onVybratProjekt(ActionEvent event) {
        lblZprava.setText("");

        projekt.setNazevProjektu(cbxVybratProjekt.getSelectionModel().getSelectedItem());

        if (projekt.existujeSouborProjektu()) {
            resetFormulare();

            try {
                Map<String, Object> volby = projekt.nacistSouborProjektu(); // hodnota je objekt, ktery nemusi byt nastaveny a muze byt null!! Musi se ale vkladat text
                taDescription.setText((volby.get(Projekt.DESCRIPTION)) == null ? "" : (String) volby.get(Projekt.DESCRIPTION));
                tfCustomParameters.setText(volby.get(Projekt.CUSTOM_PARAMETERS) == null ? "" : (String) volby.get(Projekt.CUSTOM_PARAMETERS));
                taExcludeFile.setText(volby.get(Projekt.EXCLUDE) == null ? "" : (String) volby.get(Projekt.EXCLUDE));
                taIncludeFile.setText(volby.get(Projekt.INCLUDE) == null ? "" : (String) volby.get(Projekt.INCLUDE));
                tfSource.setText(volby.get(Projekt.SOURCE) == null ? "" : (String) volby.get(Projekt.SOURCE));
                tfTarget.setText(volby.get(Projekt.TARGET) == null ? "" : (String) volby.get(Projekt.TARGET));
                taFilterFile.setText(projekt.nacistSouborFilter(volby.get(Projekt.SOURCE) == null ? "" : (String) volby.get(Projekt.SOURCE)));
                
                String sshString = volby.get(Projekt.SSH_PORT) == null ? "" : (String) volby.get(Projekt.SSH_PORT);
                tfSshPort.setText(sshString);

                boolean sshpass = volby.get(Projekt.SSH_PASS) != null;
                chbSshpass.setSelected(sshpass);

                // Ziskame seznam zvolenych prepinacu, ktery projdeme a v okne nastavime prislusny checkbox jako selected
                List<String> seznamZvolenychPrepinacu = (List<String>) volby.get(Projekt.SEZNAM_PREPINACU);
                seznamVsechPrepinacu.forEach((chb, parametr) -> { // prochazi se seznam vsech podporovanych prepinacu rsync v programu
                    if (seznamZvolenychPrepinacu.contains(parametr.getParametr())) { // pokud nacteny radek najde klic setjneho jmena
                        chb.setSelected(true);
                    }
                });

                prepnoutArchivacniMod();
                prepnoutRekurzi();
                prepnoutSSH(sshString);

            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Chyba s nactenim projektu");
            }
        }
    }

    /**
     * Z okna se ziskaji informace a uloží se do souboru jako projekt.
     */
    @FXML
    void onUlozitProjekt(ActionEvent event) {
        Node node = (Node) event.getSource();
        String novyNazev = cbxVybratProjekt.getSelectionModel().getSelectedItem();

        if (novyNazev == null || novyNazev.trim().equals("")) {
            AlertBox.displayOK("Uložit projekt",
                    "Projekt nelze uložit",
                    "Není zadaný název projektu. Pokud chcete aktuální projekt uložit, musíte jej pojmenovat.",
                    node, Alert.AlertType.ERROR);
        } else {
            String detail = taDescription.getText();
            String customParameter = tfCustomParameters.getText();
            String exclude = taExcludeFile.getText();
            String include = taIncludeFile.getText();
            String source = tfSource.getText();
            String target = tfTarget.getText();
            String filter = taFilterFile.getText();
            boolean sshpass = chbSshpass.isSelected();

            int sshport = -1;
            if (tfSshPort.getText().length() > 0) {
                try {
                    sshport = Integer.valueOf(tfSshPort.getText());
                } catch (NumberFormatException e) {
                    sshport = 22;
                }
            }

            List<String> p = new ArrayList<>();
            seznamVsechPrepinacu.forEach((chb, parametr) -> {
                if (chb.isSelected()) {
                    p.add(parametr.getParametr());
                }
            });

            try {
                projekt.setNazevProjektu(novyNazev);
                projekt.ulozitProjekt(detail, customParameter, sshport, sshpass, exclude, include, source, target, p);
                projekt.ulozitSouborFilter(source, filter);
                ziskatSeznamProjektu(false);
                cbxVybratProjekt.getSelectionModel().select(novyNazev);

                lblZprava.setText("Projekt uložen");

            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Vybrany projekt se odstrani ze seznamu.
     */
    @FXML
    void onOdstranitProjekt(ActionEvent event) {
        String nazev = cbxVybratProjekt.getSelectionModel().getSelectedItem();
        if (nazev.equals("")) {
            AlertBox.displayOK("Odstranit projekt",
                    "Projekt nelze odstranit",
                    "Nejdříve vyberte projekt, který chcete odstranit.",
                    event, Alert.AlertType.ERROR);
        } else {
            if (AlertBox.displayYN("Odstranit projekt",
                    "Opravdu chcete odstranit projekt?",
                    "Dojde k odstranění projektu.\nTento krok již nepůjde vzít zpět.",
                    event)) {
                try {
                    projekt.odstranitVybranyProjekt();
                    ziskatSeznamProjektu(true);
                    resetFormulare();
                    prepnoutArchivacniMod();
                    prepnoutRekurzi();
                    lblZprava.setText("Projekt odstraněn");

                } catch (IOException ex) {
                    AlertBox.displayOK("Odstranit projekt",
                            "Projekt nelze odstranit",
                            "Projekt se nepodařilo odstranit.",
                            event, Alert.AlertType.ERROR);
                }

            }
        }
    }

    /**
     * Zobrazi se nove okno s rsync prikazem.
     *
     * @param event udalost
     */
    @FXML
    void onZobrazitPrikaz(ActionEvent event) {
        List<String> param = sestrojitPrikaz();
        if (chbSSH.isSelected() && chbSshpass.isSelected()) {
            param.add(2, "<password>");
            param.set(5, "\"" + param.get(5) + "\"");
        } else {
            param.set(2, "\"" + param.get(2) + "\"");
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/jrbackup/zobrazit_prikaz.fxml"));
            Parent root = loader.load();

            ZobrazitPrikazController zobrazitPrikazController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Zobrazit příkaz");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());

            stage.setScene(new Scene(root));

            zobrazitPrikazController.zobrazitText(param);

            stage.showAndWait();

        } catch (IOException e) {
            System.out.println("nepodarilo se otevrit okno.");
        }
    }

    /**
     * Provede se rsync s testovacím režimu - s přepínačem -n
     *
     * @param event udalost
     */
    @FXML
    void onOtestovatPrikaz(ActionEvent event) {
        if (tfSource.getText().equals("") || tfTarget.getText().equals("")) {
            AlertBox.displayOK("Chyba",
                    "Není zadán zdroj, nebo cíl zálohy",
                    "Zadejte nejdříve zdroj a cíl zálohy.",
                    event, Alert.AlertType.ERROR);

        } else {
            spustitPrikaz(event, true);
        }
    }

    /**
     * Spusti se rsync příkaz.
     *
     * @param event udalost
     */
    @FXML
    void onSpustitPrikaz(ActionEvent event) {
        if (tfSource.getText().equals("") || tfTarget.getText().equals("")) {
            AlertBox.displayOK("Chyba",
                    "Není zadán zdroj, nebo cíl zálohy",
                    "Zadejte nejdříve zdroj a cíl zálohy.",
                    event, Alert.AlertType.ERROR);

        } else {
            spustitPrikaz(event, false);
        }
    }

    /**
     * Pokud v okně vybere archivační mod, znemožníme výběr voleb, které jej
     * nahrazují.
     */
    @FXML
    void onArchivacniMod() {
        prepnoutArchivacniMod();
    }

    /**
     * Pokud v okně vybere rekurzi, znemožníme výběr voleb, které jsou s ním v
     * konflikt.
     */
    @FXML
    void onRekurze() {
        prepnoutRekurzi();
    }
    
    @FXML
    void onNastavitSsh(ActionEvent event) {
        prepnoutSSH("22");
    }

    /**
     * Vybere se zdroj zálohy. Zobrazi se dialogove okno, kde si jej uzivatel
     * vybere.
     *
     * @param event udalost
     */
    @FXML
    void onVybratZdroj(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Vybrat zdroj");
        
        File source = new File(tfSource.getText());
        if (source.exists()) {
            directoryChooser.setInitialDirectory(source);
        }
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = directoryChooser.showDialog(stage);

        if (file != null) {
            tfSource.setText(file.getPath() + "/");
            try {
                taFilterFile.setText(projekt.nacistSouborFilter(tfSource.getText()));
                
            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Nelze nacist soubor s filtrem");
            }
        }
    }

    /**
     * Vybere se cíl, kam se bude zalohovat. Zobrazi se dialogove okno, kde si
     * jej uzivatel vybere.
     *
     * @param event udalost
     */
    @FXML
    void onVybratCil(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Vybrat cíl");
        
        File targetFile = new File(tfTarget.getText());
        if (targetFile.exists()) {
            directoryChooser.setInitialDirectory(targetFile);
        }
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = directoryChooser.showDialog(stage);

        if (file != null) {
            tfTarget.setText(file.getPath() + "/");
        }
    }

    @FXML
    void onUlozitFilter(ActionEvent event) {
        String source = tfSource.getText();
        String filter = taFilterFile.getText();
        try {
            projekt.ulozitSouborFilter(source, filter);

        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Zobrazí okno o programu s napovedou a dalsimi informacemi.
     */
    @FXML
    void onOProgramu(ActionEvent event) {
        AlertBox.displayOK("O jRBackup", "jRBackup je GUI k programu rsync. Verze " + version,
                "Je možné zálohovat soubory a složky jak na lokálním počítači,\n"
                + "tak vzdáleně. K zálohování se používá program rsync.\n\n"
                + "Aby jRBackup fungoval správně, je potřeba mít na počítači nainstalován\n"
                + "rsync a sshpass. Pokud chcete použí externí konzoli jako výstup\n"
                + "programu, je potřeba mít nainstalovaný program typu Xterm, Konsole...\n\n"
                + "Editací souboru setting.txt v adresáří programu je možné si terminál zvolit.\n"
                + "Je potřeba zadat i potřebné parametry terminálu, které umožní rsync spustit.\n"
                + "Například: xterm -hold -e",
                event, Alert.AlertType.INFORMATION);
    }

    /**
     * Nepouzite
     *
     * @param event
     */
    @FXML
    void onZmenitVystup(ActionEvent event) {
        //
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // soukrome metody
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Pripravi vse potrebne, sestroji se prikaz a spusti bud v okne programu,
     * nebo v konzoli.
     *
     * @param event
     * @param test
     */
    private void spustitPrikaz(ActionEvent event, boolean test) {
        seznamVystup.clear();
        seznamPridano.clear();
        seznamSmazano.clear();
        seznamChyby.clear();

        List<String> param = sestrojitPrikaz();
        String heslo = "";

        if (chbSSH.isSelected() && chbSshpass.isSelected()) {
            heslo = AlertBox.displayPassword("Zadat heslo", "Zadejte heslo", event);
            param.add(2, heslo);
        }

        if (heslo != null) {
            if (test) {
                param.add(param.indexOf("rsync") + 1, "-n");
            }

            zakazatTlacitka(true);
            if (chbKonsole.isSelected()) {
                provestVTerminalu(param);
            } else {
                provestVProgramu(param);
            }
            zakazatTlacitka(false);
        }
    }

    /**
     * Zjištují se, jak jsou vybrány jednotlivé prvky okna a vytvoří se příkaz,
     * který se bude spouštět. Parametry pro rsync jsou uvedeny v konkretnim
     * poradi.
     *
     * @return pole příkazů
     */
    private List<String> sestrojitPrikaz() {
        List<String> parametry = new ArrayList<>();
        if (chbSSH.isSelected() && chbSshpass.isSelected()) {
            parametry.add("sshpass");
            parametry.add("-p");
        }
        parametry.add("rsync");

        // ssh port
        if (chbSSH.isSelected()) {
            String sshport = "22";

            if (isNumeric(tfSshPort.getText())) {
                sshport = tfSshPort.getText();
            }

            parametry.add("-e");
            parametry.add("ssh -p " + sshport);
        }
        parametry.add("-i");

        seznamVsechPrepinacu.forEach((chb, parametr) -> {
            if (!parametr.getParametr().equals("ssh")) {
                if (chb.isSelected()) {
                    parametry.add(parametr.getParametr());
                }
            }
        });

        if (tfCustomParameters.getText() != null) {
            String[] vlastniParametry = tfCustomParameters.getText().trim().split(" ");

            for (String p : vlastniParametry) {
                if (!p.equals("")) {
                    parametry.add(p.trim());
                }
            }
        }

        if (!taFilterFile.getText().equals("")) {
            parametry.add("-F");
        }

        if (taIncludeFile.getText() != null) {
            parametry.addAll(ziskatInclude(taIncludeFile.getText().trim()));
        }

        if (taExcludeFile.getText() != null) {
            parametry.addAll(ziskatExclude(taExcludeFile.getText().trim()));
        }

        parametry.add(tfSource.getText());
        parametry.add(tfTarget.getText());

        return parametry;
    }

    /**
     * Provede se samotny příkaz. Vystup je
     *
     * @param params seznam parametrů
     */
    private void provestVProgramu(List<String> params) {
        new Thread(() -> {
            zakazatTlacitka(true);
            ProcessBuilder pb = new ProcessBuilder(params);

            try {
                Process p = pb.start();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                final BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                String line;
                try {
                    while ((line = reader.readLine()) != null) {

                        if (line.startsWith("*deleting")) {
                            zobrazitVystup(line.replace("*deleting", "").trim(), VYSTUP_SMAZANE);
                        }
                        if (line.startsWith("<") || line.startsWith(">") || line.startsWith("cd") || line.startsWith("cL")) {
                            zobrazitVystup(line.substring(11).trim(), VYSTUP_PRIDANE);
                        }
                        if (line.startsWith("cannot delete")) {
                            zobrazitVystup(line, VYSTUP_CHYBY);
                        }
                        zobrazitVystup(line, VYSTUP_CELY);
                    }

                    while ((line = error.readLine()) != null) {
                        if (line.startsWith("Permission denied, please try again.")) {
                            System.out.println("Spatne zadane heslo!!");
                        }

                        zobrazitVystup(line, VYSTUP_CELY);
                        zobrazitVystup(line, VYSTUP_CHYBY);
                    }

                } catch (IOException ex) {
                    Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }

                p.waitFor();
                p.destroy();
                zakazatTlacitka(false);
            } catch (IOException | InterruptedException e) {
                System.out.println("Chyba");
            }
        }).start();
    }

    /**
     * Vystup se vlozi seznamu, ktery je provazany s ListView, takze se vystup
     * zobrazi v okne. Dulezity je Platform.runlate(), ktery vytvari nove
     * vlakno, ktere vklada vystup do listview a vse synchronizuje s bezici
     * operaci rsync.
     *
     * @param radek vystup programu rsync
     * @param vystup typ vystupu. urcuje, kde se vystup zobrazi
     */
    private void zobrazitVystup(String radek, int vystup) {
        switch (vystup) {
            case VYSTUP_CELY:
                Platform.runLater(() -> seznamVystup.add(radek));
                break;
            case VYSTUP_PRIDANE:
                Platform.runLater(() -> seznamPridano.add(radek));
                break;
            case VYSTUP_SMAZANE:
                Platform.runLater(() -> seznamSmazano.add(radek));
                break;
            case VYSTUP_CHYBY:
                Platform.runLater(() -> seznamChyby.add(radek));
        }
    }

    /**
     * Prikaz se provede v terminalu. Terminal je urcen v textovem konfiguracnim
     * souboru.
     *
     * @param params seznam parametru
     */
    private void provestVTerminalu(List<String> params) {

        new Thread(() -> {
            // ziskame prikaz terminalu
            String[] prikazKonsole = Nastaveni.getKonsole().split(" ");

            // vlozime ho na zacatek prikazu
            int i = 0;
            for (String pk : prikazKonsole) {
                params.add(i, pk);
                ++i;
            }

            ProcessBuilder pb = new ProcessBuilder(params);

            try {
                Process p = pb.start();

                p.waitFor();
                p.destroy();
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    /**
     * Ziskame seznam projektu a vlozime jej do comboboxu. Vybereme v combobxu
     * bud aktualni projekt, nebo nastavime prazdny
     *
     * @param odstranitNazev nastavit aktualni nebo prazdny projekt jako vybrany
     */
    private void ziskatSeznamProjektu(boolean odstranitNazev) {
        cbxVybratProjekt.getItems().setAll(projekt.ziskatSeznamProjektu());
        if (odstranitNazev) {
            cbxVybratProjekt.getSelectionModel().select("");
        } else {
            cbxVybratProjekt.getSelectionModel().select(projekt.getNazevProjektu());
        }
    }

    /**
     * Nastaví se tooltipy jednotlivých prvků okna.
     */
    private void nastavitTooltip() {
        btnZobrazitPrikaz.setTooltip(new Tooltip("Zobrazí rsync příkaz"));
        btnSpustitPrikaz.setTooltip(new Tooltip("Spustí úlohu"));
        btnOtestovatPrikaz.setTooltip(new Tooltip("Pouze otestuje úlohu.\nNic se neprovede"));
        btnUlozitProjekt.setTooltip(new Tooltip("Uloží aktuální projekt"));
        btnOdstranitProjekt.setTooltip(new Tooltip("Odstraní projekt"));
        chbSshpass.setTooltip(new Tooltip("Použije program sshpas, který musí být nainstalovaný"));

        seznamVsechPrepinacu.forEach((chb, parametr) -> chb.setTooltip(new Tooltip(parametr.getTooltip())));
    }

    /**
     * Všechny prvky okna se nastaví na defaultní hodnoty. Okno se resetuje.
     */
    private void resetFormulare() {
        taDescription.setText("");
        taExcludeFile.setText("");
        taIncludeFile.setText("");
        taFilterFile.setText("");
        tfSource.setText("");
        tfTarget.setText("");
        tfCustomParameters.setText("");

        seznamVsechPrepinacu.forEach((chb, hodnota) -> chb.setSelected(false));
    }

    /**
     * Pokud v okně vybereme archivační mod zneaktivníme výběr voleb, které jej
     * nahrazují.
     */
    private void prepnoutArchivacniMod() {
        if (chbArchive.isSelected()) {
            chbLinks.setSelected(false);
            chbRecursive.setSelected(false);
            chbPerms.setSelected(false);
            chbTimes.setSelected(false);
            chbGroup.setSelected(false);
            chbOwner.setSelected(false);
            chbD.setSelected(false);

            chbRecursive.setDisable(true);
            chbLinks.setDisable(true);
            chbPerms.setDisable(true);
            chbTimes.setDisable(true);
            chbGroup.setDisable(true);
            chbOwner.setDisable(true);
            chbD.setDisable(true);
        } else {
            chbRecursive.setDisable(false);
            chbLinks.setDisable(false);
            chbPerms.setDisable(false);
            chbTimes.setDisable(false);
            chbGroup.setDisable(false);
            chbOwner.setDisable(false);
            chbD.setDisable(false);
        }
    }

    /**
     * Pokud v okně vybereme rekurzi zneaktivníme výběr voleb, které s ním jsou
     * v konfliktu.
     */
    private void prepnoutRekurzi() {
        if (chbRecursive.isSelected()) {
            chbArchive.setSelected(false);
            chbNoTimes.setSelected(false);
            chbNoPerms.setSelected(false);
            chbNoOwner.setSelected(false);
            chbNoGroups.setSelected(false);

            chbArchive.setDisable(true);
            chbNoTimes.setDisable(true);
            chbNoPerms.setDisable(true);
            chbNoOwner.setDisable(true);
            chbNoGroups.setDisable(true);
        } else {
            chbArchive.setDisable(false);
            chbNoTimes.setDisable(false);
            chbNoPerms.setDisable(false);
            chbNoOwner.setDisable(false);
            chbNoGroups.setDisable(false);
        }
    }
    
    private void prepnoutSSH(String ssh) {
        if (chbSSH.isSelected()) {
            chbSshpass.setDisable(false);
            tfSshPort.setDisable(false);
            tfSshPort.setText(ssh);
        } else {
            chbSshpass.setDisable(true);
            chbSshpass.setSelected(false);
            tfSshPort.setDisable(true);
            tfSshPort.setText("");
        }
    }

    /**
     * Nastaví seznam přepínačů programu rsync (combobox). Využije se při práci
     * s comboboxem. Usetri to spoustu psani a opakovani kodu.
     */
    private void nastavitPrepinace() {
        // zakladni
        seznamVsechPrepinacu.put(chbSSH, new ParametrPrikazu(Projekt.PARAMETER_SSH, "-e SSH - Umožní pracovat se vzdáleným úložistěm"));

        seznamVsechPrepinacu.put(chbRecursive, new ParametrPrikazu(Projekt.PARAMETER_R, "-r, --recursive - Rekurzivní procházení adresáře"));
        seznamVsechPrepinacu.put(chbLinks, new ParametrPrikazu(Projekt.PARAMETER_L, "-l, --links - kopíruje symbolické odkazyy, pokud to cíl dovolí"));
        seznamVsechPrepinacu.put(chbPerms, new ParametrPrikazu(Projekt.PARAMETER_P, "-p, --perms - zachová přístupová práva jako na zdroji"));
        seznamVsechPrepinacu.put(chbTimes, new ParametrPrikazu(Projekt.PARAMETER_T, "-t, --times - zachová čas jako na zdroji"));
        seznamVsechPrepinacu.put(chbGroup, new ParametrPrikazu(Projekt.PARAMETER_G, "-g, --group - zachová skupinu jako na zdroji"));
        seznamVsechPrepinacu.put(chbOwner, new ParametrPrikazu(Projekt.PARAMETER_O, "-o, --owner - zchová vlastníka jako na zdroji (pouze super-user)"));
        seznamVsechPrepinacu.put(chbD, new ParametrPrikazu(Projekt.PARAMETER_BIG_D, "-D - přenese znakové, blokové a speciální soubory k cíly"));

        seznamVsechPrepinacu.put(chbArchive, new ParametrPrikazu(Projekt.PARAMETER_A, "-a - archivační mod (nahrazuje -rlptgoD)"));
        seznamVsechPrepinacu.put(chbNoTimes, new ParametrPrikazu(Projekt.PARAMETER_NO_TIMES, "--no-times - nenastavuje čas v cíly. Nastaví se aktuílní datum"));
        seznamVsechPrepinacu.put(chbNoPerms, new ParametrPrikazu(Projekt.PARAMETER_NO_PERMS, "--no-perms - nenastavuje oprávnění v cíly"));
        seznamVsechPrepinacu.put(chbNoOwner, new ParametrPrikazu(Projekt.PARAMETER_NO_OWNER, "--no-owner - nenastavuje vlastníka v cíly"));
        seznamVsechPrepinacu.put(chbNoGroups, new ParametrPrikazu(Projekt.PARAMETER_NO_GROUP, "--no-group - nenastavuje skupinu v cíly"));
        seznamVsechPrepinacu.put(chbOmitDirTimes, new ParametrPrikazu(Projekt.PARAMETER_OMIT_DIR_TIMES, "--omit-dir-times - vynechá adresář z –times"));
        seznamVsechPrepinacu.put(chbStats, new ParametrPrikazu(Projekt.PARAMETER_STATS, "--stats - po skončení zobrazí užitečné informace o přenosu"));

        seznamVsechPrepinacu.put(chbE, new ParametrPrikazu(Projekt.PARAMETER_BIG_E, "-E - zachová spustitelný soubor jako na zdroji"));
        seznamVsechPrepinacu.put(chbAcls, new ParametrPrikazu(Projekt.PARAMETER_BIG_A, "-A, --acls - zachová ACL"));
        seznamVsechPrepinacu.put(chbXattrs, new ParametrPrikazu(Projekt.PARAMETER_BIG_X, "-X, --xattrs - zacová rozšířené atributy"));

        // rozsirene
        seznamVsechPrepinacu.put(chbCopyLinks, new ParametrPrikazu(Projekt.PARAMETER_BIG_L, "-L, --copy-links - přetvoří symbolické odkazy a vytvoří skutečné soubory a adresáře"));
        seznamVsechPrepinacu.put(chbCompress, new ParametrPrikazu(Projekt.PARAMETER_COMPRESS, "--compress - komprimuje data před odesláním"));
        seznamVsechPrepinacu.put(chbDelete, new ParametrPrikazu(Projekt.PARAMETER_DELETE, "--delete - z cílového adresáře odstraní vše, co není ve zdrojovém adresáři"));
        seznamVsechPrepinacu.put(chbIgnoreExisting, new ParametrPrikazu(Projekt.PARAMETER_IGNORE_EXISTING, "--ignore-existing - ignoruje existující soubory v cíly"));
        seznamVsechPrepinacu.put(chbExisting, new ParametrPrikazu(Projekt.PARAMETER_EXISTING, "--existing - aktualizuje pouze existující soubory v cíly"));
        seznamVsechPrepinacu.put(chbUpdate, new ParametrPrikazu(Projekt.PARAMETER_U, "-u, --update - novější soubory v cíly přeskakovat"));
        seznamVsechPrepinacu.put(chbP, new ParametrPrikazu(Projekt.PARAMETER_BIG_P, "-P - zachovat částečně přenesené soubory"));
        seznamVsechPrepinacu.put(chbSizeOnly, new ParametrPrikazu(Projekt.PARAMETER_SIZE_ONLY, "--size-only - kontrolovat jen velikost a ignorovat čas a kontrolní součet"));
        seznamVsechPrepinacu.put(chbChecksum, new ParametrPrikazu(Projekt.PARAMETER_CHECKSUM, "--checksum - vždy porovnávat kontrolní součty"));
        seznamVsechPrepinacu.put(chbHardLinks, new ParametrPrikazu(Projekt.PARAMETER_BIG_H, "-H, --hard-links - zachová pevné odkazy"));
        seznamVsechPrepinacu.put(chbNumericIds, new ParametrPrikazu(Projekt.PARAMETER_NUMERIC_IDS, "--numeric-ids - zachovat hodnoty UID, GID namísto jména uživatele a skupiny"));
        seznamVsechPrepinacu.put(chbX, new ParametrPrikazu(Projekt.PARAMETER_X, "-x, --one-file-system - nepřepisovat hranice souborového systému"));
        seznamVsechPrepinacu.put(chbProgress, new ParametrPrikazu(Projekt.PARAMETER_PROGRESS, "--progress - zobrazuje informace o průběhu"));
        seznamVsechPrepinacu.put(chbPruneEmptyDirs, new ParametrPrikazu(Projekt.PARAMETER_PRUNE_EMPTY_DIRS, "--prune-empty-dirs - vynechává prázdné adresáře ze seznamu souborů"));
    }

    /**
     * Pri spusteni zalohovani pomoci rsync zakazeme prislusna tlacitka, aby
     * rsync nesel spustit znovu.
     *
     * @param b zakazat nebo povolit tlacitka
     */
    private void zakazatTlacitka(boolean b) {
        btnOtestovatPrikaz.setDisable(b);
        btnSpustitPrikaz.setDisable(b);
        cbxVybratProjekt.setDisable(b);
    }

    /**
     * Forma --exclude v příkazu
     *
     * @param exclude seznam adresářů a souborů oddělený znakem konce řádku.
     * @return vrátí exclude ve správném formátu.
     */
    private List<String> ziskatExclude(String exclude) {
        List<String> param = new ArrayList<>();
        String[] excludeList = exclude.trim().split("\n");
        for (String e : excludeList) {
            if (!e.isEmpty()) {
                param.add("--exclude=" + e);
            }
        }

        return param;
    }

    /**
     * Forma --include v příkazu
     *
     * @param include seznam adresářů a souborů oddělený znakem konce řádku.
     * @return vrátí exclude ve správném formátu.
     */
    private List<String> ziskatInclude(String include) {
        List<String> param = new ArrayList<>();
        String[] includeList = include.trim().split("\n");
        for (String e : includeList) {
            if (!e.isEmpty()) {
                param.add("--include=" + e);
            }
        }

        return param;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

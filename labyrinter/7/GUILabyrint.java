import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.layout.*;

public class GUILabyrint extends Application {
    public boolean[][] bool;
    public static Labyrint l = null;
    GridPane pane = new GridPane();
    GridPane pane2 = new GridPane();
    int rad;
    int kolonne;
    Liste<String> utveier;
    private static Boolean erHvit;
    private static Boolean velg = false;
    static Ruteknapp ruteknapp;
    Button stoppknapp;
    Button resettKnapp;
    static Ruteknapp rr;
    double tilpassing;

    GridPane rutenett = new GridPane();
    Text utveiInfo;

    Button velgFilKnapp;
    Button lastInnKnapp;

    //lager ruteknapp
    private class Ruteknapp extends Button {
        int kol;
        int rad;

        private Ruteknapp(Labyrint l, int kol, int rad) {
            this.kol = kol;
            this.rad = rad;
            setOnMouseEntered((m) -> setStyle("-fx-cursor: hand;"));
            setOnMouseExited((m) -> setStyle("-fx-cursor: pointer; "));
            Knappebehandler behandler = new Knappebehandler();
            setOnAction(behandler);

        }
        // Handterer musetrykk og kaller pa losning
        class Knappebehandler implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent e) {
                Ruteknapp ruteknapp = (Ruteknapp) e.getSource();

                ruteknapp.setStyle("-fx-background-color: GREEN;");
                velg = true;

                finnLosning(ruteknapp.giKol(), ruteknapp.giRad());

                // utvei infork\masjon
                if (utveier.stoerrelse() == 0) {
                    utveiInfo.setText("Ingen utveier.");
                } else {
                    utveiInfo.setText("Antall uteveri: " + utveier.stoerrelse());
                }

                
            // reset button
            resettKnapp = new Button("Resett");
            resettKnapp.setTranslateX(130);
            resettKnapp.setTranslateY(tilpassing + 50);
            Resettbehandler resett = new Resettbehandler();
            resettKnapp.setOnAction(resett);
            pane.getChildren().add(resettKnapp);

            }
        }

        //storrelsen på ruteknappene
        private void settStorrelse(int i) {
            setMinWidth(i);
            setMinHeight(i);
        }

        public int giRad() {
            return this.rad;
        }

        public int giKol() {
            return this.kol;
        }

    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void start(Stage stage) throws Exception {

        // legge objektene i en grid
        GridPane.setConstraints(lagToppBoks(), 0, 0);
        GridPane.setConstraints(pane2, 0, 10);

        // plassering
        pane.setMinSize(400, 500);
        pane.getChildren().add(lagToppBoks());

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Labyrint");
        stage.show();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    // lager rutenett med infor fra Labyrinten
    public void nett(File fil) throws FileNotFoundException {

        try {
            l = Labyrint.lesFraFil(fil);
        } catch (FileNotFoundException e) {
            System.exit(1);
        }

        Scanner scanner = new Scanner(fil);
        String[] str = scanner.nextLine().split(" ");
        rad = Integer.parseInt(str[0]);
        kolonne = Integer.parseInt(str[1]);

        int rd = 0;

        while (scanner.hasNextLine()) {
            String linje = scanner.nextLine();
            rutenett.setGridLinesVisible(true);

            //lager ruteknappene
            for (int j = 0; j < kolonne; j++) {
                char tegn = linje.charAt(j);

                Ruteknapp r = new Ruteknapp(l, j, rd);
                StackPane cell = new StackPane();

                if (tegn == '.') {
                    r.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                    erHvit = true;
                    r.setBorder(new Border(
                            new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0.5))));
                } else if (tegn == '#') {
                    r.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
                    erHvit = false;
                    //r.setBorder(new Border(
                            //new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(0.5))));
                }

                // Setter storrelsene til rutene
                if (rad < 20) {
                    r.settStorrelse(45);
                } else if (rad < 30) {
                    r.settStorrelse(25);
                } else {
                    r.settStorrelse(2);
                }

                cell.getChildren().add(r);
                pane2.add(cell, j, rd);
                pane2.setTranslateX(60);
                pane2.setTranslateY(50);

            }
            rd++;
        }
        pane.getChildren().add(pane2);

        //tilpasser storrelsen etter antall rader
        if (l.hentRad() < 6)
            tilpassing = l.hentRad() * 68;
        else if (l.hentRad() < 10)
            tilpassing = l.hentRad() * 60;
        else if (l.hentRad() < 20)
            tilpassing = l.hentRad() * 55;
        else if (l.hentRad() < 30)
            tilpassing = l.hentRad() * 38;
        else
            tilpassing = l.hentRad() * 20;

        // stoppe button
        stoppknapp = new Button("Stopp");
        stoppknapp.setTranslateX(60);
        stoppknapp.setTranslateY(tilpassing + 50);
        Stoppbehandler stopp = new Stoppbehandler();
        stoppknapp.setOnAction(stopp);
        pane.getChildren().add(stoppknapp);

        // utvei infork\masjon
        utveiInfo = new Text("Velg en rute!");
        utveiInfo.setFont(new Font(20));
        utveiInfo.setFill(Color.GREEN);
        utveiInfo.setTranslateX(60);
        utveiInfo.setTranslateY(tilpassing);
        pane.getChildren().add(utveiInfo);

        scanner.close();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 7
    // Klikkbehandler-klasse:
    class Klikkbehandler implements EventHandler<ActionEvent> {
        TextField filFelt;

        public Klikkbehandler(TextField filfelt) {
            this.filFelt = filfelt;
        }

        public void handle(ActionEvent e) {

            // Viss det er velgFilkKnapp som blir trykka på:
            if ((Button) e.getSource() == velgFilKnapp) {

                // lager ny filvelger:
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt", "*.in"));
                fileChooser.setTitle("Velg tekstfil");
                File selectedFile = fileChooser.showOpenDialog(null);

                // Setter path'en til valgt fil inn i tekstfeltet
                if (selectedFile != null) {
                    filFelt.setText(selectedFile.getPath());

                }
            }
            // Viss det er lastInnKnapp som blir trykka på:
            else if ((Button) e.getSource() == lastInnKnapp) {

                try {
                    nett(new File(filFelt.getText()));
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Lager en horisontal boks med filvelger, filfelt og last inn-knapp
    private HBox lagToppBoks() {
        TextField filFelt = new TextField();
        velgFilKnapp = new Button("Velg fil...");
        Klikkbehandler klikk = new Klikkbehandler(filFelt);
        velgFilKnapp.setOnAction(klikk);
        lastInnKnapp = new Button("Last inn");
        lastInnKnapp.setOnAction(klikk);
        HBox returBoks = new HBox(50, velgFilKnapp, filFelt, lastInnKnapp);
        returBoks.setPadding(new Insets(15, 12, 15, 12));
        returBoks.setSpacing(10);
        return returBoks;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // stoppe behandler
    class Stoppbehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            Platform.exit();
        }
    }

    //reset behandler
    class Resettbehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            RESETT();
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
    // Finner alle losninger og farger utveiene gronne.
    private void finnLosning(int kol, int rad) {

        utveier = l.finnUtveiFra(kol, rad);
        if (utveier.stoerrelse() != 0) {

        for(String s : utveier){
            bool = losningStringTilTabell(s, l.hentRad(), l.hentKolonne());
        }
        for (int x = 0; x < l.hentKolonne(); x++) {
            for (int y = 0; y < l.hentRad(); y++) {
                if (bool[x][y]) {         
                    //lagre en rutevei hvis boolen er true
                    rr = new Ruteknapp(l, y, x);
                    StackPane cell = new StackPane();
                    cell.getChildren().add(rr);
                
                    // tilpasser storrelser
                    if (l.hentRad() < 20) {
                        rr.settStorrelse(45);
                    } else if (l.hentRad() < 30) {
                        rr.settStorrelse(20);
                    } else {
                        rr.settStorrelse(2);
                    }
                    pane2.add(cell, y, x);
                    rr.setBackground(
                        new Background(new BackgroundFill(Color.AQUAMARINE, null, null)));
                
                    velg = true;
                    } 
                }
            }                

        }
    }

    /**
     * Konverterer losning-String fra oblig 5 til en boolean[][]-representasjon av
     * losningstien.
     * 
     * @param losningString String-representasjon av utveien
     * @param bredde        bredde til labyrinten
     * @param hoyde         hoyde til labyrinten
     * @return 2D-representasjon av rutene der true indikerer at ruten er en del av
     *         utveien.
     */
    static boolean[][] losningStringTilTabell(String losningString, int bredde, int hoyde) {
        boolean[][] losning = new boolean[hoyde][bredde];
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\(([0-9]+),([0-9]+)\\)");
        java.util.regex.Matcher m = p.matcher(losningString.replaceAll("\\s", ""));
        while (m.find()) {
            int x = Integer.parseInt(m.group(1));
            int y = Integer.parseInt(m.group(2));
            losning[y][x] = true;
        }
        return losning;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    //Setter alle utveien/losningen ruter tilbake til a vere hvite
    public void RESETT() {
        for (int x = 0; x < l.hentKolonne(); x++) {
            for (int y = 0; y < l.hentRad(); y++) {
                if (bool[x][y]) {
                    rr = new Ruteknapp(l, y, x);
                    StackPane cell = new StackPane();
                    cell.getChildren().add(rr);

                     // storrelsene til rutene
                    if (l.hentRad() < 20) {
                        rr.settStorrelse(45);
                    } else if (l.hentRad() < 30) {
                        rr.settStorrelse(20);
                    } else {
                        rr.settStorrelse(2);
                    }
                    pane2.add(cell, y, x);
                    rr.setBackground(
                        new Background(new BackgroundFill(Color.WHITE, null, null)));
                    rr.setBorder(new Border(
                        new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(0.5))));
                

                    velg = false;
                }
            }
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////

public static void main(String[] args) {

    Application.launch(args);
    }

}
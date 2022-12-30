import java.io.*;
import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.event.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.stage.FileChooser.*;
import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.event.*;
import java.io.*;
import java.util.*;
import javafx.geometry.*;
import javafx.stage.FileChooser.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Oblig7 extends Application{
  static Pane pane = new Pane();
  final static int PADDINGY = 50;//padding fra toppen slik at det er plass til filvelger

  Button velgFilKnapp;
  Button lastInnKnapp;

  public static void main(String[] args) {
          launch();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//leser fra filen
    static Labyrint lesFraFil(File fil) throws FileNotFoundException {
        // Lese inn alle linjene fra filen
        Scanner scanner = new Scanner(fil);
        String[] str = scanner.nextLine().split(" ");
        int rad = Integer.parseInt(str[0]);
        int kolonne = Integer.parseInt(str[1]);

        ArrayList<ArrayList<Rute>> ruteArray = new ArrayList<ArrayList<Rute>>();

        int r = 0;

        // sjekker rutene i filen og lager nye Ruter ut fra informasjonen fra filen
        while (scanner.hasNextLine()) {
            String linje = scanner.nextLine();
            ArrayList<Rute> rader = new ArrayList<Rute>();

            for (int i = 0; i < kolonne; i++) {
                char tegn = linje.charAt(i);
                Rute nyRute;
                if (tegn == '.' && (r == kolonne - 1 || r == 0 || i == kolonne - 1 || i == 0)) {
                    nyRute = new Aapning(r, i);
                    // traade = new Thread(nyRute); // lage nye trade
                    // traade.start(); // starte tråden

                } else if (tegn == '.') {
                    nyRute = new HvitRute(r, i);

                } else {
                    nyRute = new SortRute(r, i);
                }
                rader.add(nyRute);
            }
            r++;
            ruteArray.add(rader);
        }
        for (int x = 0; x < rad; x++) {
            for (int y = 0; y < kolonne; y++) {
                if (y < kolonne - 1) {
                    ruteArray.get(x).get(y).ost(ruteArray.get(x).get(y + 1));
                    ruteArray.get(x).get(y + 1).vest(ruteArray.get(x).get(y));

                }
                if (x < rad - 1) {
                    ruteArray.get(x).get(y).sor(ruteArray.get(x + 1).get(y));
                    ruteArray.get(x + 1).get(y).nord(ruteArray.get(x).get(y));
                }
            }
        }
        // opprette labyrint basert på fildata og legge den inn i arrayliste
        Labyrint labyrint = new Labyrint(rad, kolonne, ruteArray);
        for (int x = 0; x < rad; x++) {
            for (int y = 0; y < kolonne; y++) {
                ruteArray.get(x).get(y).lab(labyrint);
            }
        }
        return labyrint;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Lager ein horisontal boks med filvelger, filfelt og last inn-knapp
  private HBox lagToppBoks() {
        TextField filFelt = new TextField();
        velgFilKnapp = new Button("Velg fil...");
        Klikkbehandler klikk = new Klikkbehandler(filFelt);
        velgFilKnapp.setOnAction(klikk);
        lastInnKnapp = new Button("Last inn");
        lastInnKnapp.setOnAction(klikk);
        HBox returBoks = new HBox(50, velgFilKnapp, filFelt, lastInnKnapp);
        return returBoks;
      }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//lage scene
      @Override
      public void start(Stage stage){
        pane.getChildren().add(lagToppBoks());
        pane.setPrefSize(500,500);
        Scene scene = new Scene(pane);
        stage.setTitle("Labyrint");
        stage.setScene(scene);
        stage.show();
      }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
      //Klikkbehandler-klasse:
      class Klikkbehandler implements EventHandler<ActionEvent>{
        TextField filFelt;

        public Klikkbehandler(TextField filfelt){
          this.filFelt = filfelt;
        }
        public void handle(ActionEvent e){

          //Viss det er velgFilkKnapp som blir trykka på:
          if((Button)e.getSource() == velgFilKnapp){

            //lager ny filvelger:
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                  new ExtensionFilter("Text Files", "*.txt", "*.in"));
            fileChooser.setTitle("Velg tekstfil");
            File selectedFile = fileChooser.showOpenDialog(null);

            //Setter path'en til valgt fil inn i tekstfeltet
            if(selectedFile != null) {
                filFelt.setText(selectedFile.getPath());

              }
          }
          //Viss det er lastInnKnapp som blir trykka på:
          else if((Button)e.getSource() == lastInnKnapp){
            try{
              lesFraFil(new File(filFelt.getText()));
            }
            catch(FileNotFoundException fne){}
          }
          }
        }
    }

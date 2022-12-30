import java.io.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser.*;
import javafx.stage.*;
import javafx.scene.control.*;

public class Oblig7 extends Application{
  static Pane pane = new Pane();
  final static int PADDINGY = 50;//padding fra toppen slik at det er plass til filvelger
  public static Labyrint l = null;
  Button velgFilKnapp;
  Button lastInnKnapp;

  public static void main(String[] args) {
      launch();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //new Labyrint-objekt 


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//





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

        GridPane rutenett = new GridPane();
        rutenett.setGridLinesVisible(true);
        rutenett.setLayoutX(10);  rutenett.setLayoutY(10);

        pane.getChildren().add(lagToppBoks());
        pane.getChildren().add(rutenett);

        
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
              Labyrint.lesFraFil(new File(filFelt.getText()));
            }
            catch(FileNotFoundException fne){}
          }
          }
        }
    }

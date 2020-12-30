import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class TScoreJFX extends Application {
  //  public static final int MAXIMUM_NUMBER_OF_DATA = 3000;
    private Text txtTitle, txtOffice;
    private TabProgram tbProgram;
    private TabPane tabPane;
    private TabGuide tbGuide;
    private TabAbout tbAbout;

    public static void main (String [] args) {  launch(args);  }
    @Override
    public void init() {

        txtTitle = new Text("Normalized T Score Calculation");
     // txtTitle.setFont(new Font("Times New Roman",24));
        txtTitle.setFont(Font.font("Times New Roman", FontWeight.BOLD,24));
        txtTitle.setFill(Color.BLUE);
        txtOffice = new Text("Presented by Rmutt Physics");
        txtOffice.setFont(new Font("Times New Roman",18));
        txtOffice.setFill(Color.BLUE);

       tabPane = new TabPane();
        tbProgram = new TabProgram();
       tbGuide = new TabGuide();
        tbAbout = new TabAbout();
    }
    @Override
    public void start (Stage primaryStage) {

        //for TabGuide
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(getClass().getResource("helpTScoreE.html").toExternalForm());

        ScrollPane scrollPane = new ScrollPane() ;
        scrollPane.setContent(browser);
        scrollPane.setPadding(new Insets(10,20,10,20));
        tbGuide.setContent(scrollPane);
        //  end for TabGuide
        tabPane.getTabs().addAll(tbProgram, tbGuide, tbAbout);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        VBox vbox = new VBox( 10, txtTitle, txtOffice, tabPane);


   //     VBox vbox = new VBox(10,txtTitle,txtOffice);
        vbox.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(vbox);


        primaryStage.centerOnScreen();
        primaryStage.setTitle ("Normalized T Score Calculation");
        primaryStage.setWidth(500);
        primaryStage.setHeight(700);
        // initStyle -- cause some trouble in javaFX 11 making empty windows
        //    primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}

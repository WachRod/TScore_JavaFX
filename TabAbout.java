import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

class TabAbout extends Tab {
    TabAbout() {
           setGraphic(new Label( "  About  "));
           getGraphic().setStyle("-fx-text-fill: blue;-fx-font-size:16; -fx-font-weight:bold; ");

            VBox vBoxForAuthor = showAuthor();

            VBox vBoxForHistory = showHistory();
            VBox vBoxForAll = new VBox(30, vBoxForAuthor,vBoxForHistory);
            vBoxForAll.setPadding(new Insets(30,30,30,30));
            setContent(vBoxForAll);
    }
    private VBox showAuthor() {

        Text txtAuthor = new Text("Coding by:");
        txtAuthor.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR,18));
        txtAuthor.setFill(Color.BLUE);
      //  txtAuthor.setStrokeWidth(1);
      //  txtAuthor.setStroke(Color.BLACK);
        Text txtAuthor2 = new Text("Wachara R.");
        txtAuthor2.setFont(Font.font("Tahoma", FontWeight.NORMAL, FontPosture.REGULAR,18));
        txtAuthor2.setFill(Color.BLUE);
        Text txtEmail  = new Text("Email: ");
        txtEmail.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR,16));
        txtEmail.setFill(Color.BLUE);
     //   txtEmail.setStrokeWidth(1);
     //   txtEmail.setStroke(Color.BLACK);
        Text txtEmail2 = new Text("wachr0@gmail.com");
        txtEmail2.setFont(Font.font("Tahoma", FontWeight.NORMAL, FontPosture.REGULAR,16));
        txtEmail2.setFill(Color.BLUE);
        Line line = new Line(10,10,220,10);
        line.setStroke(Color.BLUE);
        line.setStrokeWidth(2);

        HBox hBox1 = new HBox(5, txtAuthor,txtAuthor2);
        HBox hBox2 = new HBox(5,txtEmail,txtEmail2);
        return ( new VBox(10,hBox1,hBox2,line));
    }

    private VBox showHistory() {
        VBox vBoxForHistory = new VBox(15);
        Label lblHistory = new Label("History...");
        lblHistory.setStyle("-fx-font-size:18; -fx-font-weight:bold;-fx-border-color: brown;" +
                "-fx-padding:8px;");
        lblHistory.setTextFill(Color.BROWN);
        //lblHistory.setPrefHeight(18);

        Label lbl01 = new Label("- June 18, 2008. First released. Written with Java 5.");
        lbl01.setFont(Font.font(16));
        lbl01.setTextFill(Color.BROWN);
        Label lbl02 = new Label("- 2016 through 2018 Having problems with Java 8 and later, it was " +
                "blocked by java security.");

        lbl02.setWrapText(true);
        lbl02.setFont(Font.font(16));
        lbl02.setTextFill(Color.BROWN);
        Label lbl03 = new Label("- Nov 2018. New look in HTML5, CSS3 and JavaScript.");
        lbl03.setFont(Font.font(16));
        lbl03.setTextFill(Color.BROWN);
        Label lbl04 = new Label("- Feb 2020. Using JavaFX  for  GUI");
        lbl04.setFont(Font.font(16));
        lbl04.setTextFill(Color.BROWN);
        vBoxForHistory.getChildren().addAll(lblHistory,lbl01,lbl02,lbl03,lbl04);
        return vBoxForHistory;
    }

}

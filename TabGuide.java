import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


class TabGuide extends Tab {


    TabGuide(){
     //TextArea userGuide;
   //  VBox vBoxInTabGuide;
    // ScrollPane userGuideScrollPane;
        setGraphic(new Label( "  User Guide  "));
        getGraphic().setStyle("-fx-text-fill: blue;-fx-font-size:16; -fx-font-weight:bold; ");




    //     setContent(new HTMLLoader());







    }
//    public class HTMLLoader extends Region{
//        final WebView browser = new WebView();
//        final WebEngine webEngine = browser.getEngine();
//            public HTMLLoader() {
//                webEngine.load("helpTScoreE.html");
//                getChildren().add(browser);
//            }
 //   }

}

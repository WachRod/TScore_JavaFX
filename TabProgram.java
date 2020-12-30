//import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
//import javafx.print.PageLayout;
//import javafx.print.PageOrientation;
//import javafx.print.Paper;
//import javafx.print.Printer;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
//import javafx.scene.transform.Scale;
//import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;


import java.io.*;
import java.awt.*;
import java.awt.print.*;
import java.util.Date;
import static java.lang.Math.*;


public class TabProgram extends Tab {
  private ScoreTextArea scoreTextArea;
    private Button btnPrintResult;
  private ComboBox cmbGradeBox;
  private TableView table;

  private String fileName = "Data typing by User.";
    private DoingMath math = new DoingMath();
   private double average, sd;  // average value and standard deviation.
   private int numberOfUniqeScore; // number of score after classified by frequency;
   private double[] uniqueScores;
   private int [] frequency;
   private int[] cumulativeFrequency;
   private double[] percentile;
   private double[] zScore;
    private double[] tScore;
   private int gradeChoice=0;
    private String[] grade;
    private Label lblAverage, lblSD;
    private Label lblGradeSummary;

    TabProgram() {

        String[] gradeOptions = {"A, B+, B, C+, C, D+, D, F", "A, B, C, D, F", "A, B+, B, C+, C, D",
                "A, B, C, D", "A, B+, B, C+, C","A, B, C","A, B"};
        //setClosable(false);
        setGraphic(new Label( "  Program  "));
        getGraphic().setStyle("-fx-text-fill: blue;-fx-font-size:16; -fx-font-weight:bold; ");
        scoreTextArea = new ScoreTextArea();
        scoreTextArea.setPrefHeight(300);
        ScrollPane scrollPane = new ScrollPane(scoreTextArea);
        //scrollPane.setContent(scoreTextArea);
        //scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
       // scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnReadFile = new Button("Data from file");
        /*
        btnReadFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                readDataFromFile();
            }
        });
        */
        btnReadFile.setOnAction(e -> readDataFromFile());
        btnReadFile.setPrefWidth(100);
        Button btnCal = new Button("Calculate");
        btnCal.setPrefWidth(100);
        btnCal.setOnAction(e -> calculate());

        btnPrintResult = new Button("Print Result");
        btnPrintResult.setPrefWidth(100);
      //  btnPrintResult.setDisable(true);
        btnPrintResult.setOnAction(e->outputToPrinter());
        cmbGradeBox = new ComboBox<>(FXCollections.observableArrayList(gradeOptions));
        cmbGradeBox.getSelectionModel().selectFirst();
        cmbGradeBox.setOnAction(e ->{gradeChoice =cmbGradeBox.getSelectionModel().getSelectedIndex();
                                    calculate();
        });
        /*
        cmbGradeBox.setOnAction (new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gradeChoice =cmbGradeBox.getSelectionModel().getSelectedIndex();
            }
        });
        */

        HBox hBoxForComboBox = new HBox(20,new Label("Selected Grade:"), cmbGradeBox);
        hBoxForComboBox.setAlignment(Pos.CENTER);
        HBox hBoxForButtons = new HBox(40, btnReadFile, btnCal, btnPrintResult);
        hBoxForButtons.setAlignment(Pos.CENTER);
        // for display result;
        lblAverage = new Label();
        lblSD = new Label();
        lblGradeSummary = new Label();
        lblGradeSummary.setWrapText(true);
        HBox hBox1 = new HBox(10,lblAverage,lblSD);
        hBox1.setAlignment(Pos.CENTER);
        HBox hBox2 = new HBox(10, lblGradeSummary);
        hBox2.setAlignment(Pos.CENTER);
        VBox vBoxForDisplay = new VBox(5,hBox1,hBox2);

        table = new TableView();
        table.setPlaceholder(new Label("No Data to Display."));
        VBox vBoxForTable = new VBox(table);
        vBoxForTable.setPadding(new Insets(0,20,20,20));
        VBox vBoxForProgram = new VBox(15, scrollPane,hBoxForComboBox,hBoxForButtons,vBoxForDisplay,
                vBoxForTable);
        //  vBoxForProgram.setAlignment(Pos.TOP_CENTER);
       // vBoxForProgram.setPadding(new Insets(10,10,10,10));
        VBox.setMargin(scrollPane, new Insets(20,20,0,20));
        setContent(vBoxForProgram);
    }
   private void readDataFromFile() {
        StringBuilder stringBuffer = new StringBuilder();
        FileChooser chooser = new FileChooser();
      //  chooser.setMultiSelectionEnabled(false);
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file  = chooser.showOpenDialog(null);
        if (file != null) {
         //   scoreTextArea.setText(readFile(file));
            try{
                String strLine;
                fileName = file.toString();  // keep file name for printing
                FileInputStream inStream = new FileInputStream(file);
                BufferedReader  bufferedData = new BufferedReader(new InputStreamReader(inStream));
                while ((strLine = bufferedData.readLine()) != null) {
                    stringBuffer.append(strLine).append("\n");
                }
                 scoreTextArea.setText(stringBuffer.toString());

            }catch(Exception e){
                math.displayError("Error ", "Error Occurred.","Cannot read file: "+fileName);

            }

        }

    }
   private void calculate() {
        double [] rawScores;
        double [] sortedScores;


            int numberOfData = scoreTextArea.readScore();
            if (numberOfData < 0) return; // There are some error in data
            if (numberOfData < 2) {
                math.displayError("Error", "Please input more score.", " 2 scores at least for T Score");
                return;
            }
            rawScores = scoreTextArea.getScore();
            average = math.findAverage(rawScores);
            sd     = math.findStandardDeviation(rawScores);
      //      System.out.println(rawScores.length);

        /*   This following command works well. But I hate its complexities.
            sortedScores  = Arrays.stream(rawScores).boxed().sorted(Comparator.reverseOrder())
               .mapToDouble(Double::doubleValue).toArray();
        */
            sortedScores = math.quickSort(rawScores,0,rawScores.length-1,false);
            numberOfUniqeScore = math.findFrequency(sortedScores);
            frequency = math.getFrequency();
            uniqueScores = math.getUniqueData();
            cumulativeFrequency = math.findCumulativeFrequency(frequency);
            percentile = math.findPercentile(frequency, cumulativeFrequency, numberOfData);
            zScore= compute_Z_Score(percentile);
            tScore= compute_T_Score(zScore);
            grading();
            lblGradeSummary.setText(countGrade(grade));
            displayHeaderOfTable();
            displayTable();
            btnPrintResult.setDisable(false);


   //    System.out.printf("average and sd = %.2f, %.2f\n", average,sd);
   //   System.out.println( "number of Unique score = " + numberOfUniqeScore);
//
//       for (int i = 0; i < numberOfUniqeScore; i++) {
//           System.out.printf( "%.2f    %d     %d   %.2f  %.3f   %.2f  %s\n",uniqueScores[i], frequency[i],
//           cumulativeFrequency[i], percentile[i]*100,zScore[i], tScore[i],grade[i]);
//       }
    }
   private double[] compute_Z_Score(double[] areaUnderNormalCurve){
        int n = areaUnderNormalCurve.length;
        double[] z  = new double[n];
        for (int i=0;i < n ;i++ ){
            z[i]=	math.find_Z_AtThisArea (areaUnderNormalCurve[i]);
        }
        return z;
    }
    private double[] compute_T_Score(double[] zScore){
        int n = zScore.length;
        double[] t = new double[n];
        for (int i=0;i < n ;i++ ) {
            t[i] = zScore[i] * 10.0 + 50.0;
            
        }
        return t;
    }
    private void  grading()  {
        double difference, range;

        difference = tScore[0] - tScore[numberOfUniqeScore -1];
        if (difference < 0)   difference = -difference;

        grade = new String[numberOfUniqeScore];
        switch ( gradeChoice)    {
            case 0 : {
                range = difference/5.0;
                for(int i = 0 ; i < numberOfUniqeScore ; i++) {
                    grade[ i ] = "F";
                    if (tScore[i] >= 50.0 -1.5*range)  grade[i] = "D";
                    if (tScore[i] >= 50.0 -1.0*range)  grade[i] = "D+";
                    if (tScore[i] >= 50.0 -0.5*range)  grade[i] = "C";
                    if (tScore[i] >= 50.0)  grade[i] = "C+";
                    if (tScore[i] >= 50.0 +0.5*range)  grade[i] = "B";
                    if (tScore[i] >= 50.0 +1.0*range)  grade[i] = "B+";
                    if (tScore[i] >= 50.0 +1.5*range)  grade[i] = "A";
                }
                break;
            }
            case 1: {
                range = difference/5.0;
                for(int i = 0 ; i < numberOfUniqeScore ; i++) {
                    grade[ i ] = "F";
                    if (tScore[i] >= 50.0 -1.5*range) grade[i] = "D";
                    if (tScore[i] >= 50.0 -0.5*range) grade[i] = "C";
                    if (tScore[i] >= 50.0 +0.5*range) grade[i] = "B";
                    if (tScore[i] >= 50.0 +1.5*range) grade[i] = "A";
                }
                break;
            }
            case 2: {
                range = difference/4.0;
                for(int i = 0 ; i < numberOfUniqeScore ; i++) {
                    grade[ i ] = "D";

                    if (tScore[i] >= 50.0 -1.25*range)  grade[i] = "D+";
                    if (tScore[i] >= 50.0 -1.0*range)  grade[i] = "C";
                    if (tScore[i] >= 50.0 -0.5*range)  grade[i] = "C+";
                    if (tScore[i] >= 50.0)  grade[i] = "B";
                    if (tScore[i] >= 50.0 +0.5*range)  grade[i] = "B+";
                    if (tScore[i] >= 50.0 +1.0*range)  grade[i] = "A";

                }
                break;
            }

            case 3: {
                range = difference/4.0;
                for(int i = 0 ; i < numberOfUniqeScore ; i++) {
                    grade[ i ] = "D";
                    if (tScore[i] >= 50.0 -1.0*range)  grade[i] = "C";
                    if (tScore[i] >= 50.0)  grade[i] = "B";
                    if (tScore[i] >= 50.0 +1.0*range)  grade[i] = "A";
                }
                break;
            }
            case 4: {
                range =difference/3.0;
                for(int i = 0 ; i < numberOfUniqeScore ; i++) {
                    grade[ i ] = "C";

                    if (tScore[i] >= 50.0 -0.75*range)  grade[i] = "C+";
                    if (tScore[i] >= 50.0 -0.5*range)  grade[i] = "B";
                    if (tScore[i] >= 50.0)  grade[i] = "B+";
                    if (tScore[i] >= 50.0 +0.5*range)  grade[i] = "A";
                }
                break;
            }
            case 5 : {
                range = difference/3.0;
                for(int i = 0 ; i < numberOfUniqeScore ; i++) {
                    grade[ i ] = "C";
                    if (tScore[i] >= 50.0 -0.5*range)  grade[i] = "B";
                    if (tScore[i] >= 50.0 +0.5*range)  grade[i] = "A";
                }
                break;
            }
            case 6 : {
            //    range = difference/2.0;
                for(int i = 0 ; i < numberOfUniqeScore ; i++) {
                    grade[ i ] = "B";
                    if (tScore[i] >= 50.0 )  grade[i] = "A";
                }
                break;
            }
        } //  end switch

    }
    private String countGrade(String[] g) {
        // count each grade  and store in here.
        int[] gradeCount = new int[8];
        String[] gradeCharacter = new String[8];
        String[] tempGrade = new String[g.length];
        System.arraycopy(g,0,tempGrade,0,g.length);
        int numberOfGradeCharacter= 0;
        for (int i = 0; i < numberOfUniqeScore; i++){
            if(! tempGrade[i].equals("Z") ) {
                gradeCount[numberOfGradeCharacter] = frequency[i];
                gradeCharacter[numberOfGradeCharacter] = tempGrade[i];
                int j = i;
                while ( (j < numberOfUniqeScore-1) && tempGrade[j+1].equals(tempGrade[i])) {
                    gradeCount[numberOfGradeCharacter] += frequency[j+1];
                    tempGrade[j+1] = "Z";
                    j = j+1;
                }
                numberOfGradeCharacter += 1;
           }
        }
        String summary = "";
        int total=0;
                for ( int i=0 ; i < numberOfGradeCharacter;i++){
                    total = total + gradeCount[i];
                   summary = summary.concat(gradeCharacter[i]+ " = " + gradeCount[i]+ "    ");
                }
                summary = summary.concat("Total = " +total);

    return summary;

    }
  private  void displayHeaderOfTable() {

        lblAverage.setText("Average raw  score: "+ String.format("%.2f",average));
        lblSD.setText("  Standard Deviation.: "+String.format("%.2f", sd));

    }
    private void displayTable(){

        table.setEditable(false);

        table.getItems().clear();
        table.getColumns().clear();
        TableColumn<Double, Exam> column1 = new TableColumn<>("Scores");
        column1.setCellValueFactory(new PropertyValueFactory<>("scores"));
        column1.setSortable(false);
        column1.setMaxWidth(100);
        column1.setStyle("-fx-alignment: CENTER;");

        TableColumn<Integer, Exam> column2 = new TableColumn<>("Freq");
        column2.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        column2.setSortable(false);
        column2.setMaxWidth(80);
        column2.setStyle("-fx-alignment: CENTER;");

        TableColumn<Integer, Exam> column3 = new TableColumn<>("Cum Freq");
        column3.setCellValueFactory(new PropertyValueFactory<>("cumulativeFreq"));
        column3.setSortable(false);
        column3.setMaxWidth(100);
        column3.setStyle("-fx-alignment: CENTER;");
        TableColumn<Double, Exam> column4 = new TableColumn<>("Percentile");
        column4.setCellValueFactory(new PropertyValueFactory<>("percentile"));
        column4.setMaxWidth(100);
        column4.setStyle("-fx-alignment: CENTER;");
        column4.setSortable(false);
        TableColumn<Double, Exam> column5 = new TableColumn<>("Z Score");
        column5.setCellValueFactory(new PropertyValueFactory<>("zScore"));
        column5.setSortable(false);
        column5.setMaxWidth(100);
        column5.setStyle("-fx-alignment: CENTER;");
        TableColumn<Double, Exam> column6 = new TableColumn<>("T Score");
        column6.setCellValueFactory(new PropertyValueFactory<>("tScore"));
        column6.setSortable(false);
        column6.setStyle("-fx-alignment: CENTER;");
        column6.setMaxWidth(100);
        TableColumn<String , Exam> column7 = new TableColumn<>("Grade");
        column7.setCellValueFactory(new PropertyValueFactory<>("grade"));
        column7.setMaxWidth(80);
        column7.setStyle("-fx-alignment: CENTER;");

        table.getColumns().addAll(column1,column2,column3,column4,column5,column6,column7);

        for (int i = 0; i < numberOfUniqeScore; i++) {
            table.getItems().add(new Exam(String.format("%.2f",uniqueScores[i]),frequency[i],
                    cumulativeFrequency[i],String.format("%.2f",percentile[i]*100),
                    String.format("%.3f",zScore[i]), String.format("%.2f",tScore[i]),grade[i]));
        }

    }
    private void outputToPrinter( ) {
        String[] headTitle = { "Raw Score","Frequency","Cum Freq","Percentile","z-score","T-score","Grade"};

       PrinterJob prntJob = PrinterJob.getPrinterJob();
//       PageFormat pageFormat = prntJob.defaultPage();
//       pageFormat.setOrientation(PageFormat.PORTRAIT);
//       Paper paper = new Paper();
//       double margin = 4.5;
//       paper.setSize(595,842); // A4 size
//       paper.setImageableArea(margin,margin,
//               paper.getWidth()-2*margin,paper.getHeight()-2*margin);
//       pageFormat.setPaper(paper);

        prntJob.setPrintable(new Printable() {
              int maxPage=1;
              int rowPerPageForFirstPage;
                 public int print (Graphics g, PageFormat pageFormat, int pageIndex) {
                 int r,c ; // row and column in printable paper
                 int firstCol = 20;
              //   pageFormat.setOrientation(pageFormat.PORTRAIT);
               //  Paper paper = new Paper();
                // paper.setSize(595,842); // A4 size
               //  pageFormat.setPaper(paper);
                 Date today = new Date();
                   if(pageIndex >= maxPage ) {
                       return NO_SUCH_PAGE;
                   }else{
                       Graphics2D g2 = (Graphics2D) g;
                       g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                      //		int widthPage= (int)pageFormat.getImageableWidth();

                       int heightPage =(int)pageFormat.getImageableHeight();
                   //    int heightPage = 842;
                       System.out.printf("Height Of Page = %d\n",heightPage);
                       int y =20;
                       g2.setFont(new Font("Dialog", Font.PLAIN, 8));
                       if ( pageIndex > 0) g2.drawString("page : "+( pageIndex+1)+" / "+maxPage,300, y) ;
                       y+=20;
                       if (pageIndex == 0) {
                           g2.setFont(new Font("Dialog", Font.BOLD, 8));
                           FontMetrics fontMetrics = g2.getFontMetrics();
                           y += fontMetrics.getAscent();
                           g2.drawString("Normalized T Score Calculation Report.", firstCol+80, y);
                           y += 20;
                           g2.drawString("Data from  :  " + fileName, firstCol, y);
                           y += 20;
                           g2.drawString("Printed Date  : " + today, firstCol, y);
                           y += 20;
                           g2.drawString("Mean = " + String.format("%.2f", average), firstCol, y);  // message, column, row
                           g2.drawString("S.D. = " + String.format("%.2f", sd), firstCol+190, y);
                           y += 20;
                           g2.drawString(lblGradeSummary.getText(), firstCol, y);
                           y += 10;
                           g2.drawLine(firstCol, y, firstCol+330, y);
                           y += 10;
                       }
                       g2.setFont( new Font("Dialog", Font.PLAIN,8));
                       int numCols = 7;
                       int [ ] x = new int [ numCols];
                       //     x[0] =120;
                       x[0]= firstCol;
                            y += g2.getFontMetrics().getHeight();
                            for (c=0; c < numCols ; c ++) {
                                if (c+1 < numCols) 	x[c+1] = x[c] + 50;
                                g2.drawString(headTitle[c], x[c], y);
                            }
                       int head = y;
                       int h = g2.getFontMetrics().getHeight();
                       int rowHeight = (int)(h*1.5);
                       System.out.printf("rowHeight = %d",rowHeight);
                       int rowPerPage;
                       int startRow,endRow;
                       int offSet;
                            rowPerPage = heightPage/rowHeight;
                            maxPage = max((int)ceil(numberOfUniqeScore/(double)rowPerPage),1);
                            if (pageIndex ==0) {
                                rowPerPage = (heightPage - head)/rowHeight;
                                rowPerPageForFirstPage = rowPerPage;
                            }

                      System.out.printf("row per page = %d\n  ,maxpage = %d\n",rowPerPage,maxPage);
                            offSet = rowPerPage - rowPerPageForFirstPage;
                            startRow = pageIndex*rowPerPage;
                            if (pageIndex > 0) startRow =pageIndex*rowPerPage - offSet;
                            endRow = min(numberOfUniqeScore, startRow + rowPerPage);
                            for  (r = startRow; r < endRow; r++) {
                                y += h;
                                g2.drawString(String.format("%.2f",uniqueScores[r]),firstCol,y);
                                g2.drawString(String.format("%d",frequency[r]),firstCol+60,y);
                                g2.drawString(String.format("%d",cumulativeFrequency[r]),firstCol+100,y);
                                g2.drawString(String.format("%4.2f",percentile[r]*100),firstCol+150,y);
                                g2.drawString(String.format("%.3f",zScore[r]),firstCol+200,y);
                                g2.drawString(String.format("%.2f",tScore[r]),firstCol+250,y);
                                g2.drawString(grade[r],firstCol+305,y);
                            } // for r
                            if(pageIndex == maxPage-1) {
                                g2.setFont( new Font("Dialog", Font.BOLD,12));
                                g2.drawString("Invite you to visit : www.rmutphysics.com", 100, y+3*h);
                            }
                            return PAGE_EXISTS;
                       }
                   } // method print
        } // new printable
        );
        if (prntJob.printDialog())	{
            try{
                prntJob.print();
            } catch(Exception e) {
                math.displayError("Error", "Printer Error", "Please check your Printer.");
            }
        }

    }

}

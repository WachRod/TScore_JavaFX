
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


class ScoreTextArea extends TextArea {


   private int MaxDataNumber = TScoreConstants.MAXIMUM_NUMBER_OF_DATA;
   private double [ ] scores = new double[MaxDataNumber+1];
   private int numberOfData;
   private DoingMath math = new DoingMath();

    ScoreTextArea ()
    {
        setFont(Font.font("Dialog", FontWeight.NORMAL,14));
        setWrapText(true);
        setPrefHeight(150);
        setPrefWidth(450);
        setStyle("-fx-background-color: blue;");
        setText("* Type or input your scores in here.........");

     // setBackground(Color.WHITE);

      //  setSelectionColor(Color.pink);
      //  setLineWrap(false); // Wrap the line at the container end.
    }
    int readScore()
    {

        String  score = super.getText();
        char[] ch = score.toCharArray();
        StringBuilder temp= new StringBuilder();
        boolean IN_COMMENT=false;
        boolean  IN_DIGIT = false;
        int point = 0;
        int line = 0;
        int n = 0;

        for(int i =0; i < ch.length ; i++)
        {
            switch (ch[i])
            {
                case '*' :
                    IN_COMMENT = true;
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '.':

                    if (!IN_COMMENT)
                    {
                        IN_DIGIT =true;
                        if (ch[i] == '.')  point +=1;
                        temp.append(ch[i]);
                        if (point > 1)   {
                            math.displayError ("ERROR", "Error occurred.","Some score not (a) numbers at line"+(line+1));
                            return -1;
                        }
                    }
                    break;

                case ' ':
                case '\n':
                    if (ch[i] == '\n')  {
                        line = line +1;
                        IN_COMMENT = false;
                    }
                    if (!(IN_COMMENT ) && IN_DIGIT) {
                        point = 0;
                        //	if (temp != "")
                        if  (!temp.toString().equals("") && !temp.toString().equals(" ")) {
                            scores[n] = Double.parseDouble(temp.toString());
                            temp = new StringBuilder();
                            n=n+1;
                            if ( n > (MaxDataNumber)) {
                                math.displayError ("Sorry! ", "Your data exceed  "+(MaxDataNumber),"Please truncate your data" );
                                return -1;
                            } // if (n >=.....

                        }
                        IN_DIGIT = false;
                    }
                    break;
                default :
                    if (!IN_COMMENT) {
                        math.displayError("Error!!" , "Ooop!","Something error at line "+(line+1));
                        return -1;
                    }
                    break;
            } //switch

            if ( i == (ch.length-1) && ch[i] != '\n'  && ch[i] != ' ') {   // does'nt work  --> && ch[i] !=null

               if (!temp.toString().equals("") && !temp.toString().equals(" "))
                    scores[n] = Double.parseDouble(temp.toString());
                else {
                    math.displayError("Error!!", "No Data detected","Please Input at your data");
                    return -1;
                }

                n=n+1;
                if ( n > (MaxDataNumber)) {
                    math.displayError ("Sorry!"," Your data exceed  "+(MaxDataNumber),"Please truncate your data" );
                    return -1;
                } // if (n >=.....
            }

        }// for loop
     //   System.out.println("In ScoreTextClass scores.length = " + scores.length);
     //   System.out.println("In ScoreTExt Class n ="+ n);
        numberOfData = n;
        return n;
    }
    double [ ]  getScore()    {
        // before return scores we must reduce the score (fromm 3000) to actual size
        double[] realScore = new double[numberOfData];
        System.arraycopy(scores, 0, realScore, 0, numberOfData);
        return realScore;
    }


}

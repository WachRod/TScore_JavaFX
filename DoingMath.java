import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import static java.lang.Math.*;
import static java.lang.Double.*;
public class DoingMath {


    private double[] uniqueData;
    private int[] frequency;


    /**
     * quick sort: sort  all data in ascending or descending
     * param item  data to be sorted
     * param left  the first item of data
     * param right the last item of data
     * param ascending  if true , data will be sort in ascending otherwise descending
     */
    double[] quickSort(double[] item, int left, int right, boolean ascending) {
        int i, j;
        double comparand, temp;
        i = left;
        j = right;
        comparand = item[(left + right) / 2];
        do {
            if (ascending) {
                while (item[i] < comparand && i < right) i++;
                while (comparand < item[j] && j > left) j--;
            } else {
                while (item[i] > comparand && i < right) i++;
                while (comparand > item[j] && j > left) j--;
            }
            if (i <= j) {
                temp = item[i];
                item[i] = item[j];
                item[j] = temp;
                i++;
                j--;
            }
        } while (i <= j);
        if (left < j) quickSort(item, left, j, ascending);
        if (i < right) quickSort(item, i, right, ascending);

        return item;

    }

    double findAverage(double[] data) {
        double average = 0;
        double sumOfData;


        if (data.length > 1) {
            //find mean
            sumOfData = 0;
            for (double d : data) {
                sumOfData += d;
            }
            average = sumOfData / data.length;
        }
        return average;
    }

    double findStandardDeviation( double[] data) {
        double sd = 0;
        if (data.length > 1) {
            double average = findAverage(data);
            double sumDifference = 0;
            for (double d : data) {
                sumDifference += (d - average) * (d - average);
            }
            sd = sqrt(sumDifference / (data.length - 1));
        }
        return sd;
    }
    int findFrequency( double[] data) {
        int numberOfFrequency=0;
        int[] tempFrequency = new int[data.length];
        double[]  tempUniqueData= new double[data.length];

        for (int i = 0; i < data.length; i++) {
            if (data[i] != MAX_VALUE) {
                tempFrequency[numberOfFrequency] = 1;
                tempUniqueData[numberOfFrequency] = data[i];
                int j = i;
                while ( ( j < data.length-1) && data[j+1] == data[i]) {
                    tempFrequency[numberOfFrequency] +=1;
                    data[j+1] = MAX_VALUE;
                    j = j+1;
                }
                numberOfFrequency = numberOfFrequency +1;
            }

        }
        uniqueData = new double [numberOfFrequency];
        System.arraycopy(tempUniqueData,0,uniqueData,0,numberOfFrequency);
        frequency  = new int [numberOfFrequency];
        System.arraycopy(tempFrequency,0,frequency,0, numberOfFrequency);
        return numberOfFrequency;
    }
    int[] getFrequency() {
        return frequency;
    }
    double[] getUniqueData() {
        return uniqueData;
    }

    int[] findCumulativeFrequency(int[] frequency) {
        int n = frequency.length;
        int[] cFequency = new int [n]; // Cumulative frequency
            cFequency[n-1] = frequency[n-1];
            for (int i = n-2; i >=0; i--) {
                cFequency[i] = frequency[i] + cFequency[i+1];
            }
        return cFequency;
    }
    double[] findPercentile( int[] frequency, int[] cfrequency, int numberOfData) {
        int n = frequency.length;
        double[] percentile= new double[n];
            for (int i = n-1; i >=0; i--){
                if (i == n-1)
                    percentile[i] = 0.5*frequency[i]/numberOfData;
                else
                    percentile[i] = (cfrequency[i+1] + 0.5*frequency[i])/numberOfData;
            }
            return percentile;
    }

    /** Compute the area between 2 statistic z value
	* @param lower_z lower limit of area
	* @param upper_z upper limit of area
	* @ return area under normal curve between 2 value of z
	*/

    double findAreaUnderNormalCurve(double lower_z, double upper_z) {

        int intervals = 1000;
//        Function normalFunction = new Function() {
//            public double Of(double x) {
//                return (1/sqrt(2.0*PI)*exp(-(x*x)/2.0));}
//        };
        Function normalFunction = x -> 1/sqrt(2.0*PI)*exp(-(x*x)/2.0);
        return  computeSimpson1_3Integration( normalFunction,lower_z,upper_z,intervals);

    }
    /** Integration of given function from lower limit
     to upper limit using the Simpson 1/3 method.
     */

  private   double computeSimpson1_3Integration(Function function, double lower, double upper, int intervals) {
        double area = 0;
        if(intervals > 0) {
            double h = (upper - lower) / (2 * intervals); // half of interval width
            double dA; // Small area under parabolic curve element

            for (int i = 0; i < intervals; i++) {
                double x1 = lower + 2 * i * h;
                // area of the parabolic region = h/3(f(-h) +4f(0) +f(h))
                double x2 = x1 + h; // middle point
                double x3 = x2 + h; // rightmost point
                double y1 = function.Of(x1);
                double y2 = function.Of(x2);
                double y3 = function.Of(x3);
                dA =  h*(y1 + 4*y2 +y3)/3;  // area under parabolic curve element
                area += dA;
            }
        }
        return area;

    }

    /** finding statistic Z value at known area under normal curve
     * @param area  the area under normal curve should be between 0 to 1.
     * @return z value at known area
     */
    double find_Z_AtThisArea (double area) {
        double DEFAULT_TOLERANCE  = TScoreConstants.DEFAULT_TOLERANCE;
        boolean isAreaGreaterThanHalf = false;
        double lowerLimit=0;
        double newArea;
        double deltaX;
        double z , sumArea=0;
        double dA;
        if ( area < 0 || area >1 ) {
            displayError("Error!!", "Invalid Area",
                    "An area must greater than 0 and less than 1.");
            return area;
        }
        if (area > 0.5){
            newArea = area - 0.5;
            isAreaGreaterThanHalf = true;
        } else {
            newArea = 0.5 - area;
        }

        if (newArea >= 0.01993880583837) {
            lowerLimit = 0.05;
            sumArea = 0.01993880583837;
        }
        if (newArea >= 0.09870632568292) {
            lowerLimit = 0.25;
            sumArea = 0.09870632568292;
        }
        if (newArea >= 0.19146246127401) {
            lowerLimit = 0.5;
            sumArea = 0.19146246127401;
        }
        if (newArea >= 0.34134474606854 ) {
            lowerLimit = 1.0;
            sumArea = 0.34134474606854;
        }
        if (newArea >= 0.43319279873114) {
            lowerLimit = 1.5;
            sumArea = 0.43319279873114;
        }
        if (newArea >= 0.47724986805182) {
            lowerLimit = 2;
            sumArea = 0.47724986805182;
        }

        if (newArea >= 0.48609655248650) {
            lowerLimit = 2.2;
            sumArea = 0.48609655248650;
        }
        if (newArea >= 0.48927588997832) {
            lowerLimit = 2.3;
            sumArea = 0.48927588997832;
        }
        if (newArea >= 0.49180246407540) {
            lowerLimit = 2.4;
            sumArea = 0.49180246407540;
        }
        if (newArea >= 0.49379033467422) {

            lowerLimit = 2.5;
            sumArea = 0.49379033467422;
        }
        if (newArea >=0.49653302619696) {
            lowerLimit = 2.7;
            sumArea = 0.49653302619696;
        }
        if (newArea >=0.49744486966957) {
            lowerLimit = 2.8;
            sumArea =0.49744486966957 ;
        }
        if (newArea >=0.49813418669962) {
            lowerLimit = 2.9;
            sumArea =0.49813418669962 ;
        }
        if (newArea >= 0.49865010196837) {
            lowerLimit = 3;
            sumArea = 0.49865010196837;
        }
        if (newArea >=0.49903239678678 ) {
            lowerLimit = 3.1;
            sumArea = 0.49903239678678;
        }
        if (newArea >= 0.49931286206208) {
            lowerLimit = 3.2;
            sumArea = 0.49931286206208;
        }
        if (newArea >= 0.49951657585762) {
            lowerLimit = 3.3;
            sumArea = 0.49951657585762;
        }
        z = lowerLimit;
        deltaX = 0.00001;
        while( newArea - sumArea > DEFAULT_TOLERANCE) {
            dA = (1/sqrt(2*PI))*0.5*deltaX*(exp(-0.5*z*z) + exp(-0.5*(z+deltaX)*(z+deltaX)));
            sumArea += dA;
            z += deltaX;
        }
        //	System.out.println(" z in method = " + z);
        //			System.out.println(" Sumarea = " + sumArea);

        if (isAreaGreaterThanHalf)  return z+deltaX;
        else return -(z+deltaX);
    }

    void displayError(String titleBar, String headerMsg, String contentMsg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, contentMsg, ButtonType.OK);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMsg);
        alert.showAndWait();

    }
/*
  abstract static class Function {
        double Of (double x);
    //    abstract double  derivativeOf ( double x);
    //    abstract double secondDerivativeOf ( double x);
  }

 */
@FunctionalInterface
interface Function {
    double Of (double x);
}
}

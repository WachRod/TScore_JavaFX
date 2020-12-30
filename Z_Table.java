
class Z_Table {
    // for testing function find area under normal curve at specific lower limit and upper limit
    // The result is corresponding to the table of any statistical books.

        public static void main (String[] args) {
            double z, area ;

            DoingMath math = new DoingMath();
            double stepColumn = 0.01;
            System.out.println("\n\t Find the Area under Normal curve between 0 to 1.99 step 0.01.\n\n");
            System.out.println("        0      1      2      3      4      5      6      7      8       9");
            int i = 0;
            for( z = 0; z<= 2; z = z+stepColumn) {
                if ( i % 10 ==0) {
                    System.out.printf("\n%2.1f ",z);
                    i =0;
                }
                i +=1;
                area = math.findAreaUnderNormalCurve(0,z);
                System.out.printf(" %4.4f",area+0.5);
            }
            System.out.println("\n\t Find the Area under Normal curve between 0 to -1.99 step 0.01.\n\n");
            System.out.println("        0      1      2      3      4      5      6      7      8       9");
            double stepRow = 0.1;
            double tempz;
            for( z = -1.9; z <= 0; z = z+stepRow) {
                System.out.printf("\n%2.1f ",z);
                tempz = z;
                for(int k = 0; k < 10 ; k++) {
                    area = math.findAreaUnderNormalCurve(tempz,0);
                    System.out.printf(" %4.4f", 0.5 - area);
                    tempz = tempz - 0.01;
                }

            }

        }



}

public class Exam {
    private String scores;
    private int frequency;
    private int cumulativeFreq;
    private String percentile;
    private String zScore;
    private String tScore;
    private String grade;

    Exam (String scores, int frequency, int cFreq, String percentile, String zScore, String tScore, String grade) {
        this.scores = scores;
        this.frequency = frequency;
        this.cumulativeFreq = cFreq;
        this.percentile = percentile;
        this.zScore = zScore;
        this.tScore= tScore;
        this.grade = grade;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getCumulativeFreq() {
        return cumulativeFreq;
    }

    public void setCumulativeFreq(int cumulativeFreq) {
        this.cumulativeFreq = cumulativeFreq;
    }

    public String getPercentile() {
        return  percentile;
    }

    public void setPercentile(String percentile) {
        this.percentile = percentile;
    }

    public String getZScore() {
        return zScore;
    }
    public void setZScore(String zScore) {
        this.zScore = zScore;
    }

    public String getTScore() {
        return tScore;
    }

    public void settScore(String tScore) {
        this.tScore = tScore;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


}

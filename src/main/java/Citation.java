package main.java;

public class Citation {
    private String rawText;
    private String author;
    private String year;
    private String title;
    private String journal;
    private String style;
    private String doi;
    private boolean doiValid;
    private int qualityScore;

    public Citation(String rawText) {
        this.rawText = rawText.trim();
        this.author = "";
        this.year = "";
        this.title = "";
        this.journal = "";
        this.style = "Unknown";
        this.doi = "";
        this.doiValid = false;
        this.qualityScore = 0;
    }

    public String getRawText() { return rawText; }
    public String getAuthor() { return author; }
    public String getYear() { return year; }
    public String getTitle() { return title; }
    public String getJournal() { return journal; }
    public String getStyle() { return style; }
    public String getDoi() { return doi; }
    public boolean isDoiValid() { return doiValid; }
    public int getQualityScore() { return qualityScore; }

    public void setAuthor(String author) { this.author = author; }
    public void setYear(String year) { this.year = year; }
    public void setTitle(String title) { this.title = title; }
    public void setJournal(String journal) { this.journal = journal; }
    public void setStyle(String style) { this.style = style; }
    public void setDoi(String doi) { this.doi = doi; }
    public void setDoiValid(boolean doiValid) { this.doiValid = doiValid; }
    public void setQualityScore(int qualityScore) { this.qualityScore = qualityScore; }

    public boolean isMissingAuthor() { return author.isEmpty(); }
    public boolean isMissingYear() { return year.isEmpty(); }
    public boolean isMissingTitle() { return title.isEmpty(); }
    public boolean isMissingJournal() { return journal.isEmpty(); }

    public String getQualityGrade() {
        if (qualityScore >= 90) return "Excellent";
        if (qualityScore >= 75) return "Good";
        if (qualityScore >= 50) return "Fair";
        return "Poor";
    }
}
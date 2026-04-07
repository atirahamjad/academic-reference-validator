package main.java;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    public void generateReport(List<Citation> citations,
                                Map<Citation, List<String>> validationResults,
                                Map<String, Integer> duplicates,
                                String styleConsistency,
                                String outputPath) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {

            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            writer.println("=".repeat(80));
            writer.println("ACADEMIC REFERENCE VALIDATOR — REPORT");
            writer.println("Generated: " + timestamp);
            writer.println("=".repeat(80));
            writer.println();

            // Summary
            long validCount = validationResults.values().stream().filter(List::isEmpty).count();
            long invalidCount = citations.size() - validCount;
            double avgScore = citations.stream().mapToInt(Citation::getQualityScore).average().orElse(0);

            writer.println("SUMMARY");
            writer.println("-".repeat(40));
            writer.println("Total references analysed : " + citations.size());
            writer.println("Valid references          : " + validCount);
            writer.println("References with issues    : " + invalidCount);
            writer.println("Duplicate references      : " + duplicates.size());
            writer.printf ("Average quality score     : %.1f / 100%n", avgScore);
            writer.println("Style consistency         : " + styleConsistency);
            writer.println();

            // Quality distribution
            writer.println("QUALITY DISTRIBUTION");
            writer.println("-".repeat(40));
            long excellent = citations.stream().filter(c -> c.getQualityGrade().equals("Excellent")).count();
            long good = citations.stream().filter(c -> c.getQualityGrade().equals("Good")).count();
            long fair = citations.stream().filter(c -> c.getQualityGrade().equals("Fair")).count();
            long poor = citations.stream().filter(c -> c.getQualityGrade().equals("Poor")).count();
            writer.println("  Excellent (90-100) : " + excellent);
            writer.println("  Good      (75-89)  : " + good);
            writer.println("  Fair      (50-74)  : " + fair);
            writer.println("  Poor      (0-49)   : " + poor);
            writer.println();

            // Style distribution
            writer.println("CITATION STYLE DISTRIBUTION");
            writer.println("-".repeat(40));
            Map<String, Long> styleCount = new java.util.HashMap<>();
            for (Citation c : citations) {
                styleCount.merge(c.getStyle(), 1L, Long::sum);
            }
            for (Map.Entry<String, Long> entry : styleCount.entrySet()) {
                writer.println("  " + entry.getKey() + ": " + entry.getValue());
            }
            writer.println();

            // Valid references with scores
            writer.println("VALID REFERENCES");
            writer.println("-".repeat(40));
            int validIdx = 1;
            for (Map.Entry<Citation, List<String>> entry : validationResults.entrySet()) {
                if (entry.getValue().isEmpty()) {
                    Citation c = entry.getKey();
                    writer.println(validIdx++ + ". [" + c.getStyle() + "] [Score: " 
                        + c.getQualityScore() + "/100 - " + c.getQualityGrade() + "]");
                    writer.println("   " + c.getRawText());
                    if (!c.getDoi().isEmpty()) writer.println("   DOI: " + c.getDoi());
                    writer.println();
                }
            }

            // Issues
            writer.println("REFERENCES WITH ISSUES");
            writer.println("-".repeat(40));
            int issueIdx = 1;
            for (Map.Entry<Citation, List<String>> entry : validationResults.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    Citation c = entry.getKey();
                    writer.println(issueIdx++ + ". [Score: " + c.getQualityScore() 
                        + "/100 - " + c.getQualityGrade() + "] " + c.getRawText());
                    for (String issue : entry.getValue()) {
                        writer.println("   >> " + issue);
                    }
                    writer.println();
                }
            }

            // Duplicates
            if (!duplicates.isEmpty()) {
                writer.println("DUPLICATE REFERENCES DETECTED");
                writer.println("-".repeat(40));
                for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
                    writer.println("  [x" + entry.getValue() + "] " + entry.getKey());
                }
                writer.println();
            }

            // DOI summary
            long withDoi = citations.stream().filter(c -> !c.getDoi().isEmpty()).count();
            long withValidDoi = citations.stream().filter(c -> !c.getDoi().isEmpty() && c.isDoiValid()).count();
            writer.println("DOI SUMMARY");
            writer.println("-".repeat(40));
            writer.println("  References with DOI       : " + withDoi);
            writer.println("  Valid DOI format          : " + withValidDoi);
            writer.println("  Missing or invalid DOI    : " + (citations.size() - withValidDoi));
            writer.println();

            writer.println("=".repeat(80));
            writer.println("END OF REPORT");
            writer.println("=".repeat(80));

            System.out.println("Report generated: " + outputPath);

        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }
}
package main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        System.out.println("=".repeat(80));
        System.out.println("ACADEMIC REFERENCE VALIDATOR v2.0");
        System.out.println("Developed by Atirah Amjad");
        System.out.println("MSc Business Analytics (Distinction), Sunway University");
        System.out.println("=".repeat(80));
        System.out.println();

        String inputFile = args.length > 0 ? args[0] : "sample_references.csv";
        String outputFile = args.length > 1 ? args[1] : "output/validation_report.txt";

        System.out.println("Input file  : " + inputFile);
        System.out.println("Output file : " + outputFile);
        System.out.println();

        List<String> rawReferences = loadReferences(inputFile);

        if (rawReferences.isEmpty()) {
            System.out.println("No references found in input file.");
            return;
        }

        System.out.println("Loaded " + rawReferences.size() + " references.");
        System.out.println("Validating...");
        System.out.println();

        Validator validator = new Validator();
        ReportGenerator reporter = new ReportGenerator();

        List<Citation> citations = new ArrayList<>();
        Map<Citation, List<String>> validationResults = new LinkedHashMap<>();

        for (String raw : rawReferences) {
            if (raw.trim().isEmpty()) continue;
            Citation citation = validator.parse(raw);
            List<String> issues = validator.validate(citation);
            citations.add(citation);
            validationResults.put(citation, issues);

            String status = issues.isEmpty() ? "OK    " : "ISSUES";
            System.out.println("[" + status + "] [Score: " + citation.getQualityScore() 
                + "/100] [" + citation.getStyle() + "] " 
                + raw.substring(0, Math.min(55, raw.length())) + "...");
        }

        Map<String, Integer> duplicates = validator.detectDuplicates(citations);
        String styleConsistency = validator.checkStyleConsistency(citations);

        System.out.println();
        System.out.println("Style consistency : " + styleConsistency);

        if (!duplicates.isEmpty()) {
            System.out.println("Duplicates found  : " + duplicates.size());
        }

        double avgScore = citations.stream()
            .mapToInt(Citation::getQualityScore)
            .average().orElse(0);

        System.out.println();
        reporter.generateReport(citations, validationResults, duplicates, styleConsistency, outputFile);

        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("VALIDATION COMPLETE");
        System.out.println("=".repeat(80));
        System.out.printf("Total references  : %d%n", citations.size());
        System.out.printf("Valid             : %d%n", validationResults.values().stream().filter(List::isEmpty).count());
        System.out.printf("Issues found      : %d%n", validationResults.values().stream().filter(v -> !v.isEmpty()).count());
        System.out.printf("Duplicates        : %d%n", duplicates.size());
        System.out.printf("Average score     : %.1f / 100%n", avgScore);
        System.out.println("=".repeat(80));
    }

    private static List<String> loadReferences(String filePath) {
        List<String> references = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                if (!line.trim().isEmpty()) references.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return references;
    }
}
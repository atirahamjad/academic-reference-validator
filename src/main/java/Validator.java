package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final Pattern YEAR_PATTERN = Pattern.compile("\\b(19|20)\\d{2}\\b");
    private static final Pattern DOI_PATTERN = Pattern.compile("10\\.\\d{4,}/\\S+");
    private static final Pattern VALID_DOI_PATTERN = Pattern.compile("^10\\.\\d{4,}/[\\S]{3,}$");

    public Citation parse(String rawText) {
        Citation citation = new Citation(rawText);

        Matcher yearMatcher = YEAR_PATTERN.matcher(rawText);
        if (yearMatcher.find()) {
            citation.setYear(yearMatcher.group());
        }

        Matcher doiMatcher = DOI_PATTERN.matcher(rawText);
        if (doiMatcher.find()) {
            String doi = doiMatcher.group();
            citation.setDoi(doi);
            citation.setDoiValid(VALID_DOI_PATTERN.matcher(doi).matches());
        }

        if (rawText.matches(".*\\([12]\\d{3}\\).*")) {
            citation.setStyle("APA");

            String authorPart = rawText.replaceAll("\\s*\\([12]\\d{3}\\).*", "").trim();
            if (!authorPart.isEmpty()) citation.setAuthor(authorPart);

            String afterYear = rawText.replaceAll(".*?\\([12]\\d{3}\\)\\.?\\s*", "").trim();
            String[] parts = afterYear.split("\\. ");
            if (parts.length >= 1 && !parts[0].trim().isEmpty()) citation.setTitle(parts[0].trim());
            if (parts.length >= 2 && !parts[1].trim().isEmpty()) {
                String journal = parts[1].replaceAll(",.*", "").trim();
                citation.setJournal(journal);
            }

        } else if (rawText.contains("\"")) {
            citation.setStyle("IEEE");

            int firstQuote = rawText.indexOf("\"");
            if (firstQuote > 0) citation.setAuthor(rawText.substring(0, firstQuote).trim());

            int lastQuote = rawText.lastIndexOf("\"");
            if (firstQuote != lastQuote && lastQuote > firstQuote) {
                citation.setTitle(rawText.substring(firstQuote + 1, lastQuote).trim());
            }

            String afterTitle = rawText.substring(lastQuote + 1).trim();
            if (afterTitle.startsWith(",")) afterTitle = afterTitle.substring(1).trim();
            String[] ieeeParts = afterTitle.split(",");
            if (ieeeParts.length >= 1 && !ieeeParts[0].trim().isEmpty()) {
                citation.setJournal(ieeeParts[0].trim());
            }

        } else {
            citation.setStyle("Chicago/Harvard");
            String[] parts = rawText.split(",", 2);
            if (parts.length >= 1) citation.setAuthor(parts[0].trim());
            if (parts.length >= 2) citation.setTitle(parts[1].trim());
        }

        citation.setQualityScore(calculateScore(citation));
        return citation;
    }

    public int calculateScore(Citation citation) {
        int score = 0;
        if (!citation.isMissingAuthor()) score += 25;
        if (!citation.isMissingYear()) score += 25;
        if (!citation.isMissingTitle()) score += 25;
        if (!citation.isMissingJournal()) score += 15;
        if (!citation.getDoi().isEmpty()) score += 10;
        return score;
    }

    public List<String> validate(Citation citation) {
        List<String> issues = new ArrayList<>();

        if (citation.isMissingAuthor()) issues.add("Missing author");
        if (citation.isMissingYear()) issues.add("Missing year");
        if (citation.isMissingTitle()) issues.add("Missing title");
        if (citation.isMissingJournal()) issues.add("Missing journal/source");

        if (!citation.getYear().isEmpty()) {
            int year = Integer.parseInt(citation.getYear());
            if (year < 1900 || year > 2026) issues.add("Suspicious year: " + citation.getYear());
        }

        if (!citation.getDoi().isEmpty() && !citation.isDoiValid()) {
            issues.add("Invalid DOI format: " + citation.getDoi());
        }

        return issues;
    }

    public Map<String, Integer> detectDuplicates(List<Citation> citations) {
        Map<String, Integer> seen = new HashMap<>();
        Map<String, Integer> duplicates = new HashMap<>();

        for (Citation c : citations) {
            String key = (c.getAuthor() + c.getYear()).toLowerCase().replaceAll("\\s+", "");
            if (key.length() > 3) {
                seen.put(key, seen.getOrDefault(key, 0) + 1);
                if (seen.get(key) > 1) duplicates.put(c.getRawText(), seen.get(key));
            }
        }
        return duplicates;
    }

    public String checkStyleConsistency(List<Citation> citations) {
        Map<String, Integer> styleCounts = new HashMap<>();
        for (Citation c : citations) {
            styleCounts.merge(c.getStyle(), 1, Integer::sum);
        }
        if (styleCounts.size() > 1) {
            StringBuilder sb = new StringBuilder("Mixed citation styles detected: ");
            styleCounts.forEach((style, count) ->
                sb.append(style).append("(").append(count).append(") "));
            return sb.toString().trim();
        }
        return "Consistent";
    }
}
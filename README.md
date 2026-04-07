# Academic Reference Validator

![Java](https://img.shields.io/badge/Java-25-orange) ![Status](https://img.shields.io/badge/Status-Active-brightgreen) ![License](https://img.shields.io/badge/License-MIT-lightgrey)

A command-line tool built in Java that validates academic references for completeness, style consistency, and duplicates. Supports APA, IEEE, and Chicago/Harvard citation formats.

---

## The problem

Manual reference checking in academic work is tedious and error-prone. Missing authors, incorrect years, duplicate citations, and inconsistent styles frequently slip through. This tool automates that validation process — parsing each reference, identifying issues, and generating a structured report.

> Companion project to my [Citation Quality Assessment Pipeline](https://github.com/atirahamjad/citation-quality-pipeline) — which solves the same problem at ML scale in Python. This tool demonstrates the same domain knowledge implemented in Java using rule-based validation.

---

## What it does

- Parses references in APA, IEEE, and Chicago/Harvard formats
- Detects missing fields — author, year, title, journal
- Flags suspicious years outside 1900-2026
- Detects duplicate references
- Scores each reference out of 100 — Excellent, Good, Fair, Poor
- Checks citation style consistency across the entire reference list
- Validates DOI format
- Generates a structured validation report as a text file

---

## Sample output

---

## Project structure

---

## How to run

**Compile:**
```bash
javac -d . src/main/java/Citation.java src/main/java/Validator.java src/main/java/ReportGenerator.java src/main/java/Main.java
```

**Run with sample data:**
```bash
java main.java.Main sample_references.csv output/validation_report.txt
```

**Run with your own file:**
```bash
java main.java.Main your_references.csv output/your_report.txt
```

---

## Input format

A CSV file with one reference per line, first line is header:

---

## Tech stack

| Component | Detail |
|---|---|
| Language | Java 25 LTS |
| Parsing | Regex-based multi-style parser |
| Validation | Rule-based field completeness checks |
| Scoring | Quality grading system — Excellent, Good, Fair, Poor |
| Output | Plain text report with structured sections |
| OOP Design | Citation, Validator, ReportGenerator classes |

---

## Author

**Atirah Amjad** — MSc Business Analytics (Distinction), Sunway University

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=flat-square&logo=linkedin&logoColor=white)](https://linkedin.com/in/atirah-amjad)
[![Citation Pipeline](https://img.shields.io/badge/Python%20ML%20Project-citation--quality--pipeline-blue?style=flat-square)](https://github.com/atirahamjad/citation-quality-pipeline)
[![Live Demo](https://img.shields.io/badge/Live%20Demo-Streamlit-FF4B4B?style=flat-square&logo=streamlit)](https://citation-quality-pipeline.streamlit.app)

\# Academic Reference Validator



!\[Java](https://img.shields.io/badge/Java-25-orange) !\[Status](https://img.shields.io/badge/Status-Active-brightgreen) !\[License](https://img.shields.io/badge/License-MIT-lightgrey)



A command-line tool built in Java that validates academic references for completeness, style consistency, and duplicates. Supports APA, IEEE, and Chicago/Harvard citation formats.



\---



\## The problem



Manual reference checking in academic work is tedious and error-prone. Missing authors, incorrect years, duplicate citations, and inconsistent styles frequently slip through. This tool automates that validation process — parsing each reference, identifying issues, and generating a structured report.



> Companion project to my \[Citation Quality Assessment Pipeline](https://github.com/atirahamjad/citation-quality-pipeline) — which solves the same problem at ML scale in Python. This tool demonstrates the same domain knowledge implemented in Java using rule-based validation.



\---



\## What it does



\- Parses references in APA, IEEE, and Chicago/Harvard formats

\- Detects missing fields — author, year, title, journal

\- Flags suspicious years (outside 1900-2026)

\- Detects duplicate references

\- Generates a structured validation report as a text file

\- Displays real-time validation status in the console



\---



\## Sample output

ACADEMIC REFERENCE VALIDATOR v1.0

Total     : 10

Valid     : 8

Issues    : 2

Duplicate : 1



Report includes: summary, style distribution, valid references, flagged issues, and duplicates detected.



\---



\## Project structure

ReferenceValidator/

├── src/main/java/

│   ├── Citation.java          # Data model for a single reference

│   ├── Validator.java         # Parsing and validation logic

│   ├── ReportGenerator.java   # Report output

│   └── Main.java              # Entry point

├── sample\_references.csv      # Sample input file

├── output/                    # Generated reports

└── README.md



\---



\## How to run



Compile:

javac -d . src/main/java/Citation.java src/main/java/Validator.java src/main/java/ReportGenerator.java src/main/java/Main.java



Run with sample data:

java main.java.Main sample\_references.csv output/validation\_report.txt



Run with your own file:

java main.java.Main your\_references.csv output/your\_report.txt



\---



\## Input format



A CSV file with one reference per line, first line is header:



reference

Smith, J. (2020). The impact of machine learning on healthcare. Journal of Medical Informatics, 45(2), 123-145.

M. Brown, "Natural language processing," IEEE Transactions, vol. 31, pp. 456-478, 2021.



\---



\## Tech stack



Language        : Java 25 LTS

Parsing         : Regex-based multi-style parser

Validation      : Rule-based field completeness checks

Output          : Plain text report with structured sections

OOP Design      : Citation, Validator, ReportGenerator classes



\---



\## Author



Atirah Amjad — MSc Business Analytics (Distinction), Sunway University

LinkedIn: https://linkedin.com/in/atirah-amjad

Citation Pipeline (Python/ML): https://github.com/atirahamjad/citation-quality-pipeline

Live Demo: https://citation-quality-pipeline.streamlit.app


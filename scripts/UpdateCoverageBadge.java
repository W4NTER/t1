package scripts;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Set;

public class UpdateCoverageBadge {

    private static final String REPORT_PATH = "target/site/jacoco/jacoco.xml";
    private static final String README_PATH = "README.md";
    private static final String BADGE_MARKER = "<!-- COVERAGE_BADGE -->";
    private static final String BADGE_TEMPLATE = "![Coverage](https://img.shields.io/badge/Coverage-%.2f%%25-brightgreen.svg)";
    private static final Set<String> SET_OF_COVERED_TYPES = Set.of("INSTRUCTION", "LINE", "COMPLEXITY", "METHOD", "CLASS");
    private static final double DECIMAL_PLACES = 100.0;

    public static void main(String[] args) throws Exception {
        double coverage = getCoveragePercentage();
        updateReadmeBadge(coverage);
    }

    private static double getCoveragePercentage() throws Exception {
        int missed = 0;
        int covered = 0;
        File xmlFile = new File(REPORT_PATH);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setFeature("http://xml.org/sax/features/validation", false);

        Document doc = factory.newDocumentBuilder().parse(xmlFile);

        NodeList counters = doc.getElementsByTagName("counter");

        for (int i = 0; i < counters.getLength(); i++) {
            var node = counters.item(i);
            var type = node.getAttributes().getNamedItem("type").getTextContent();

            if (SET_OF_COVERED_TYPES.contains(type)) {
                missed += Integer.parseInt(node.getAttributes().getNamedItem("missed").getTextContent());
                covered += Integer.parseInt(node.getAttributes().getNamedItem("covered").getTextContent());
            }
        }

        if (missed + covered == 0) {
            return 0.0;
        }
        return Math.round(100.0 * covered / (missed + covered) * DECIMAL_PLACES) / DECIMAL_PLACES;
    }

    private static void updateReadmeBadge(double coverage) throws IOException {
        Path readmePath = Path.of(README_PATH);
        String content = Files.readString(readmePath);

        int markerIndex = content.indexOf(BADGE_MARKER);
        if (markerIndex == -1) {
            return;
        }

        int lineEnd = content.indexOf('\n', markerIndex);
        String newLine = BADGE_MARKER + " " + BADGE_TEMPLATE.formatted(coverage);
        String updated = content.substring(0, markerIndex) + newLine + content.substring(lineEnd);

        Files.writeString(readmePath, updated);
    }
}

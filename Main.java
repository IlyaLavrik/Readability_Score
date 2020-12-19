package readability;

        import java.io.IOException;
        import java.nio.file.Files;
        import java.nio.file.Paths;
        import java.util.Scanner;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

public class Main {
    public static int TextChar(String text) {
        return text.replaceAll("\\s","").split("").length;
    }
    public static int TextWords (String text) {
        return text.split("\\s*[\\s]\\s*").length;
    }
    public static int TextBid (String text) {
        return text.split("\\s*[.?!]\\s*").length;
    }
    public static int Syllables(String text) {
        text =  text.toLowerCase().replaceAll("e\\b", "")
                .replaceAll("[aeiouy]{2}", "a")
                .replaceAll("[^aeiouy\\W]", "");
        String pat = "[aeiouy]";
        Matcher m = Pattern.compile(pat).matcher(text);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }
    public static int PolySyllables(String text) {
        text =  text.toLowerCase().replaceAll("e\\b", "")
                .replaceAll("[aeiouy]{2}", "a")
                .replaceAll("[^aeiouy\\W]", "");
        String pat = "[aeiouy]{3,}";
        Matcher m = Pattern.compile(pat).matcher(text);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }
    public static int TestScore(double score) {
        double sc = Math.rint(score);
        switch ((int) sc) {
            case 1:
                score = 6;
                break;
            case 2:
                score = 7;
                break;
            case 3:
                score = 9;
                break;
            case 4:
                score = 10;
                break;
            case 5:
                score = 11;
                break;
            case 6:
                score = 12;
                break;
            case 7:
                score = 13;
                break;
            case 8:
                score = 14;
                break;
            case 9:
                score = 15;
                break;
            case 10:
                score = 16;
                break;
            case 11:
                score = 17;
                break;
            case 12:
                score = 18;
                break;
            case 13:
                score = 24;
                break;
            case 14:
                score = 50;
                break;
            default:
                break;
        }
        return (int) score;
    }
    public static double AutomatedIndex(int ch, int w, int b) {
        return (4.71 * ch/w) + (0.5 * w / b) - 21.43;
    }
    public static double FleschKincaid(int w, int b, int syl) {
        return (0.39 * w / b) + (11.8 * syl / w) - 15.59;
    }
    public static double SimpleMeasure(int polys, int b) {
        return 1.043 * Math.sqrt((polys * 30 / b)) + 3.1291;
    }
    public static double ColemanIndex(int ch, int w, int b) {
        return 0.0588 * ch / w * 100 - 0.296 * b / w * 100 - 15.8;
    }
    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
    public static void main(String[] args) {
        String file = args[0];
        String pater = "The text is:";
        try {
            System.out.println(readFileAsString(file));
            int ch = TextChar(readFileAsString(file).replace(pater, ""));
            int w = TextWords(readFileAsString(file).replace(pater, ""));
            int polys = PolySyllables(readFileAsString(file).replace(pater, ""));
            int syl = Syllables(readFileAsString(file).replace(pater, ""));
            int b = TextBid(readFileAsString(file));
            System.out.printf("Words: %d%n" +
                    "Sentences: %d%n" +
                    "Characters: %d%n" +
                    "Syllables: %d%n" +
                    "Polysyllables: %d%n", w, b, ch, syl, polys);
            System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            Scanner scanner = new Scanner(System.in);
            switch (scanner.nextLine()) {
                case "ARI":
                    System.out.printf("Automated Readability Index: %.2f (about %d year olds).%n", AutomatedIndex(ch, w, b), TestScore(AutomatedIndex(ch, w, b)));
                    break;
                case "FK":
                    System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d year olds).%n", FleschKincaid(w, b, syl), TestScore(FleschKincaid(w, b, syl)));
                    break;
                case "SMOG":
                    System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year olds).%n", SimpleMeasure(polys, b), TestScore(SimpleMeasure(polys, b)));
                    break;
                case "CL":
                    System.out.printf("Coleman–Liau index: %.2f (about %d year olds).%n", ColemanIndex(ch, w, b), TestScore(ColemanIndex(ch, w, b)));
                    break;
                case "all":
                    System.out.printf("Automated Readability Index: %.2f (about %d year olds).%n", AutomatedIndex(ch, w, b), TestScore(AutomatedIndex(ch, w, b)));
                    System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d year olds).%n", FleschKincaid(w, b, syl), TestScore(FleschKincaid(w, b, syl)));
                    System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year olds).%n", SimpleMeasure(polys, b), TestScore(SimpleMeasure(polys, b)));
                    System.out.printf("Coleman–Liau index: %.2f (about %d year olds).%n", ColemanIndex(ch, w, b), TestScore(ColemanIndex(ch, w, b)));
                    break;
                default:
                    System.out.println("Invalid parameter");
                    break;
            }
            double aver = (double) (TestScore(AutomatedIndex(ch, w, b)) + TestScore(FleschKincaid(w, b, syl)) + TestScore(SimpleMeasure(polys, b)) + TestScore(ColemanIndex(ch, w, b))) / 4;
            System.out.printf("This text should be understood in average by %.2f year olds.",aver);
        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }
    }
}

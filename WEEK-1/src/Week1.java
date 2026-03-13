
import java.util.*;

class Document {
    String id;
    String text;

    Document(String id, String text) {
        this.id = id;
        this.text = text;
    }
}

public class Week1 {

    private int n;
    private HashMap<String, Set<String>> index;
    private HashMap<String, Integer> docNgramCount;

    public Week1(int n) {
        this.n = n;
        index = new HashMap<>();
        docNgramCount = new HashMap<>();
    }

    private List<String> preprocess(String text) {
        text = text.toLowerCase().replaceAll("[^a-z0-9\\s]", " ");
        return Arrays.asList(text.trim().split("\\s+"));
    }

    private List<String> generateNgrams(List<String> words) {
        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.size() - n; i++) {
            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < n; j++) {
                if (j > 0) sb.append(" ");
                sb.append(words.get(i + j));
            }

            ngrams.add(sb.toString());
        }

        return ngrams;
    }

    public void addDocument(Document doc) {

        List<String> words = preprocess(doc.text);
        List<String> ngrams = generateNgrams(words);

        docNgramCount.put(doc.id, ngrams.size());

        for (String ng : ngrams) {
            index.computeIfAbsent(ng, k -> new HashSet<>()).add(doc.id);
        }
    }

    public void analyzeDocument(Document doc) {

        List<String> words = preprocess(doc.text);
        List<String> ngrams = generateNgrams(words);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String ng : ngrams) {
            if (index.containsKey(ng)) {
                for (String docId : index.get(ng)) {
                    matchCount.put(docId, matchCount.getOrDefault(docId, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {

            String otherDoc = entry.getKey();
            int matches = entry.getValue();

            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches + " matching n-grams with \"" + otherDoc + "\"");

            System.out.printf("Similarity: %.1f%% ", similarity);

            if (similarity > 50) {
                System.out.println("(PLAGIARISM DETECTED)");
            } else if (similarity > 15) {
                System.out.println("(suspicious)");
            } else {
                System.out.println("(low similarity)");
            }
        }
    }

    public static void main(String[] args) {

        Week1 detector = new Week1(5);

        Document d1 = new Document("essay_089.txt",
                "Artificial intelligence is transforming modern education systems across the world");

        Document d2 = new Document("essay_092.txt",
                "Artificial intelligence is transforming modern education and learning systems globally");

        detector.addDocument(d1);
        detector.addDocument(d2);

        Document newEssay = new Document("essay_123.txt",
                "Artificial intelligence is transforming modern education systems in universities");

        detector.analyzeDocument(newEssay);
    }
}

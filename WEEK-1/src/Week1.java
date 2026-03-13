
import java.util.*;

class PageEvent {
    String url;
    String userId;
    String source;

    PageEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class Week1 {

    private HashMap<String, Integer> pageViews;
    private HashMap<String, HashSet<String>> uniqueVisitors;
    private HashMap<String, Integer> trafficSources;

    public Week1() {
        pageViews = new HashMap<>();
        uniqueVisitors = new HashMap<>();
        trafficSources = new HashMap<>();
    }

    public void processEvent(PageEvent event) {

        pageViews.put(event.url, pageViews.getOrDefault(event.url, 0) + 1);

        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

        trafficSources.put(event.source, trafficSources.getOrDefault(event.source, 0) + 1);
    }

    public void getDashboard() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        System.out.println("Top Pages:");

        int rank = 1;

        while (!pq.isEmpty() && rank <= 10) {

            Map.Entry<String, Integer> entry = pq.poll();
            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println(rank + ". " + page + " - " + views + " views (" + unique + " unique)");
            rank++;
        }

        System.out.println();
        System.out.println("Traffic Sources:");

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue() + " visits");
        }
    }

    public static void main(String[] args) throws Exception {

        Week1 analytics = new Week1();

        analytics.processEvent(new PageEvent("/article/breaking-news", "user_123", "google"));
        analytics.processEvent(new PageEvent("/article/breaking-news", "user_456", "facebook"));
        analytics.processEvent(new PageEvent("/sports/championship", "user_789", "google"));
        analytics.processEvent(new PageEvent("/sports/championship", "user_111", "direct"));
        analytics.processEvent(new PageEvent("/sports/championship", "user_222", "google"));
        analytics.processEvent(new PageEvent("/article/breaking-news", "user_123", "google"));
        analytics.processEvent(new PageEvent("/tech/ai-future", "user_333", "twitter"));
        analytics.processEvent(new PageEvent("/tech/ai-future", "user_444", "google"));

        Thread.sleep(5000);

        analytics.getDashboard();
    }
}

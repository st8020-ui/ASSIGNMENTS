import java.util.*;

class DNSEntry {
    String ipAddress;
    long expiryTime;

    DNSEntry(String ipAddress, long ttl) {
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttl * 1000;
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class Week1 {

    private HashMap<String, DNSEntry> cache;
    private int hits = 0;
    private int misses = 0;

    public Week1() {
        cache = new HashMap<>();
    }

    public String resolve(String domain) {

        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                return "Cache HIT → " + entry.ipAddress;
            } else {
                cache.remove(domain);
            }
        }

        misses++;
        String ip = queryUpstreamDNS(domain);
        cache.put(domain, new DNSEntry(ip, 300));
        return "Cache MISS → " + ip;
    }

    private String queryUpstreamDNS(String domain) {
        Random r = new Random();
        return "172.217.14." + (200 + r.nextInt(50));
    }

    public String getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);
        return "Hit Rate: " + hitRate + "%";
    }

    public static void main(String[] args) throws Exception {

        Week1 dns = new Week1();

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        Thread.sleep(2000);

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.getCacheStats());
    }
}
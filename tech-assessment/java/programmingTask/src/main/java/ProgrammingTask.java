import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toMap;


public class ProgrammingTask {
    //Improvements: have the filename as an argument - just keeping it hardcoded for this implementation.
    public static void main(String[] args) {
        String filename = "programming-task-example-data.log";
        LinkedHashMap<String, Integer> urls = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> ipAddresses = new LinkedHashMap<>();
        readFileAndGetIpAddressesAndUrls(filename, urls, ipAddresses);

        LinkedHashMap<String, Integer> sortedIpAddressesDescending = sortMapByValueDescending(ipAddresses);
        LinkedHashMap<String, Integer> sortedURLsDescending = sortMapByValueDescending(urls);

        System.out.println("No. of unique IP addresses = " + ipAddresses.size());
        System.out.println("Top 3 most active IP addresses = " + getTopNResults(sortedIpAddressesDescending, 3));
        System.out.println("Top 3 most active URLs = " + getTopNResults(sortedURLsDescending, 3));
    }

    //naming all good?
    //Single Responsibility Principal violation? Considered pulling out the readFile seperately and returning a BufferReader - then the while loop has to be moved into the main function. I think it's cleaner to do it like this,
    //but open to suggestions. Pulling it out made testing annoying.
    //Improvement / Variation: decoupling. Increase in performance time, but then could return the hashmaps individually and avoid having a void method.
    public static void readFileAndGetIpAddressesAndUrls(String filename, LinkedHashMap<String, Integer> urls, LinkedHashMap<String, Integer> ipAddresses) {
        try {
            FileInputStream fstream = new FileInputStream(filename);
            String strLine;
            BufferedReader reader = new BufferedReader(new InputStreamReader(fstream));
            while ((strLine = reader.readLine()) != null) {
                extractUrls(urls, strLine);
                extractIpAddresses(ipAddresses, strLine);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static LinkedHashMap<String, Integer> extractIpAddresses(LinkedHashMap<String, Integer>ipAddresses, String strLine) {
        String validIpAddressRegex = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
        Pattern pattern = Pattern.compile(validIpAddressRegex);
        Matcher matcher = pattern.matcher(strLine);
        if (matcher.find()) {
            String address = matcher.group();
            ipAddresses.put(address, ipAddresses.getOrDefault(address, 0) + 1);
        }
        return ipAddresses;
    }

    public static LinkedHashMap<String, Integer> extractUrls(LinkedHashMap<String, Integer> urls, String strLine) {
        int indexOfGET = strLine.indexOf("GET");
        int indexOfSpace = strLine.indexOf(" ", indexOfGET + 4);
        if (strLine.contains("GET") && indexOfSpace >= 0) {
            String url = strLine.substring(indexOfGET + 4, indexOfSpace);
            urls.put(url, urls.getOrDefault(url, 0) + 1);
        }
        return urls;
    }

    //Improvements / changes:
    // 1. Could have this implementation as a priority queue (which I had originally). But this doesn't list the results in decscending order. Which isn't a huge deal, but it's not very intuitive.
    // Considerations for client:
    // - I have only returned 3 addresses / ip addresses, regardless of whether there are multiple that are active the same amount.
    // - How do we want multiple of the same ip addresses / urls returned if there are multiple that are active the same amount? (i.e. alphabetical order / ascending order?)
        // - do we want all of these returned? I have assumed only 3 required to be returned.
    public static LinkedHashMap<String, Integer> sortMapByValueDescending(Map<String, Integer> map) {
        return map.entrySet().stream()
                .sorted(reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static List<String> getTopNResults(LinkedHashMap<String, Integer> sortedMapDescending, int numOfResults) {
        List<String> keyList = new ArrayList<>();
        for (String key : sortedMapDescending.keySet()) {
            if (keyList.size() < numOfResults)
                keyList.add(key);
        }
        return keyList;
    }
}

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProgrammingTaskTest {

    @Mock
    private ProgrammingTask programmingTask;

    @Test
    @DisplayName("Verify methods are called")
    public void testMainCallsThenVerify(){
//      This isn't correct and I can't seem to reference the method from the mock - I am missing something.
        var mine = mock(ProgrammingTask.class);
        ProgrammingTask.main(new String[]{""});
        verify(mine);
    }

    @Test
    @DisplayName("Test that readFileAndGetIpAddressesAndUrls returns updates hashmaps")
    public void testReadFileAndGetIpAddressesAndUrlsExceutes(){
//        ToDo
    }

    @Test
    @DisplayName("")
    public void testReadFileAndGetIpAddressesAndUrlsThrowsIOException() throws IOException {
//        This isn't working, I can't get it to mock correctly :(.
//        doThrow(new IOException("error")).when(programmingTask).readFileAndGetIpAddressesAndUrls(any(), any(), any());
    }

    @ParameterizedTest
    @CsvSource({"168.41.191.9, 1",
            "168.41.191.9 - - [72.44.32.10], 1",
            "nullValues={\"null\"}, 0",
            "'', 0"})
    @DisplayName("Test that IP addresses are identified based on pattern and added to map")
    public void testExtractIpAddressesReturnsCorrectAddresses(String strLine, int noOfIpAddresses) {
        LinkedHashMap<String, Integer> ipAddresses = new LinkedHashMap<>();

        LinkedHashMap<String, Integer> ipAddressMap = ProgrammingTask.extractIpAddresses(ipAddresses, strLine);

        assertThat(ipAddressMap.size()).isEqualTo(noOfIpAddresses);
    }

    @ParameterizedTest
    @CsvSource({"GET /madison GET /hello, 1",
            "168.41.191.9 - - [72.44.32.10], 0",
            "GET test, 0",
            "nullValues={\"null\"}, 0",
            "'', 0"})
    @DisplayName("Test that correct valid urls are added to map")
    public void testExtractUrlsReturnsCorrectUrls(String strLine, int noOfUrls) {
        LinkedHashMap<String, Integer> urls = new LinkedHashMap<>();

        LinkedHashMap<String, Integer> urlMap = ProgrammingTask.extractUrls(urls, strLine);

        assertThat(urlMap.size()).isEqualTo(noOfUrls);
    }

    @Test
    @DisplayName("Test map is sorted by descending value")
    public void testSortMapByValueDescendingReturnsSortedDescendingOrderMap() {
        Map<String, Integer> unsortedMap = new HashMap<>();
        unsortedMap.put("one", 1);
        unsortedMap.put("three", 3);
        unsortedMap.put("five", 5);
        unsortedMap.put("two", 2);
        unsortedMap.put("four", 4);

        Map<String, Integer> sortedMap = ProgrammingTask.sortMapByValueDescending(unsortedMap);

        final Iterator<Integer> iterator = sortedMap.values().iterator();

        assertAll (
                () -> assertThat(iterator.next()).isEqualTo(5),
                () -> assertThat(iterator.next()).isEqualTo(4),
                () -> assertThat(iterator.next()).isEqualTo(3),
                () -> assertThat(iterator.next()).isEqualTo(2),
                () -> assertThat(iterator.next()).isEqualTo(1),

                () -> assertThat(sortedMap.size()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("Test returned list holds correct elements and is of the correct size")
    public void testGetTopNResultsReturnsCorrectOrderAndNumberOfResults() {
        LinkedHashMap<String, Integer> sortedMapDescending = new LinkedHashMap<>();
        sortedMapDescending.put("five", 5);
        sortedMapDescending.put("four", 4);
        sortedMapDescending.put("three", 3);
        sortedMapDescending.put("threeAdditional", 4);
        sortedMapDescending.put("two", 2);

        List<String> topNResults = ProgrammingTask.getTopNResults(sortedMapDescending, 3);

        assertAll (
                () -> assertThat(topNResults.get(0)).isEqualTo("five"),
                () -> assertThat(topNResults.get(1)).isEqualTo("four"),
                () -> assertThat(topNResults.get(2)).isEqualTo("three"),

                () -> assertThat(topNResults.size()).isEqualTo(3)
        );
    }
}
package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> treeMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> smallestScore = treeMap.firstEntry();
        if (smallestScore == null) {
            return null;
        }
        return Map.entry(new Customer(smallestScore.getKey()), smallestScore.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextScore = treeMap.higherEntry(customer);
        if (nextScore == null) {
            return null;
        }
        return Map.entry(new Customer(nextScore.getKey()), nextScore.getValue());
    }

    public void add(Customer customer, String data) {
        treeMap.put(customer, data);
    }
}

package src;

import java.util.*;
import java.util.stream.Collectors;

/**
 * topN算法 快速统计排名前几数据(优化后)
 *
 * @author Ryan.Yi
 */
public class OptimizeTopN {
    public List<String> topNFrequent(List<String> words, int n) {
        Map<String, Integer> count = words.stream()
                .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));

        PriorityQueue<Map.Entry<String, Integer>> heap =
                new PriorityQueue<>((a, b) -> Objects.equals(a.getValue(), b.getValue()) ? b.getKey().compareTo(a.getKey()) : a.getValue() - b.getValue());

        count.entrySet().forEach(entry -> {
            heap.offer(entry);
            if (heap.size() > n) {
                heap.poll();
            }
        });

        List<String> topN = new ArrayList<>();
        while (!heap.isEmpty())
            topN.add(0, heap.poll().getKey());
        return topN;
    }
}

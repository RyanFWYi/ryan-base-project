package src;

import java.util.*;

/**
 * topN算法 快速统计排名前几数据
 *
 * @author Ryan.Yi
 */
public class TopN {

    public List<String> topNFrequent(List<String> words, int n) {
        Map<String, Integer> count = new HashMap<>();
        for (String word : words) {
            count.put(word, count.getOrDefault(word, 0) + 1);
        }

        PriorityQueue<Map.Entry<String, Integer>> heap =
                new PriorityQueue<>((a, b) -> a.getValue() == b.getValue() ? b.getKey().compareTo(a.getKey()) : a.getValue() - b.getValue());

        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            heap.offer(entry);
            if (heap.size() > n) heap.poll();
        }

        List<String> topN = new ArrayList<>();
        while (!heap.isEmpty())
            topN.add(0, heap.poll().getKey());
        return topN;
    }

}

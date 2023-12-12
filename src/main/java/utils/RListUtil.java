package utils;

import cn.hutool.core.collection.ListUtil;

import java.util.*;
import java.util.stream.Collectors;

public class RListUtil extends ListUtil {

    /**
     * topN算法(优化)
     *
     * @param items 运算对象
     * @param n     topN
     * @return topN results
     * @author Ryan.Yi
     */
    public <T extends Comparable<T>> List<T> topNFrequent(List<T> items, int n) {
        Map<T, Integer> count = items.stream()
                .collect(Collectors.toMap(item -> item, item -> 1, Integer::sum));
        PriorityQueue<Map.Entry<T, Integer>> heap =
                new PriorityQueue<>((a, b) ->
                        Objects.equals(a.getValue(), b.getValue()) ?
                                b.getKey().compareTo(a.getKey()) :
                                a.getValue() - b.getValue());

        count.entrySet().forEach(entry -> {
            heap.offer(entry);
            if (heap.size() > n) {
                heap.poll();
            }
        });
        List<T> topN = new ArrayList<>();
        while (!heap.isEmpty())
            topN.add(0, heap.poll().getKey());
        return topN;
    }

}

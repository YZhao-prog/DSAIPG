package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;


public class FourAryHeap<K> implements Iterable<K> {
    private final boolean max;
    private final int first;
    private final Comparator<K> comparator;
    private final K[] binHeap;
    private int last;
    private final boolean floyd;

    public FourAryHeap(boolean max, Object[] binHeap, int first, int last, Comparator<K> comparator, boolean floyd) {
        this.max = max;
        this.first = first;
        this.comparator = comparator;
        this.last = last;
        //noinspection unchecked
        this.binHeap = (K[]) binHeap;
        this.floyd = floyd;
    }

    public FourAryHeap(int n, int first, boolean max, Comparator<K> comparator, boolean floyd) {
        this(max, new Object[n + first], first, 0, comparator, floyd);
    }

    public FourAryHeap(int n, boolean max, Comparator<K> comparator, boolean floyd) {
        this(n, 1, max, comparator, floyd);
    }

    public boolean isEmpty() {
        return last == 0;
    }

    public int size() {
        return last;
    }

    public void give(K key) {
        if (last == binHeap.length - first)
            last--;
        binHeap[++last + first - 1] = key;
        swimUp(last + first - 1);
    }

    public K take() throws PQException {
        if (isEmpty()) throw new PQException("Priority queue is empty");
        if (floyd) return doTake(this::snake);
        else return doTake(this::sink);
    }

    K doTake(Consumer<Integer> f) {
        K result = binHeap[first];
        swap(first, last-- + first - 1);
        f.accept(first);
        binHeap[last + first] = null;
        return result;
    }

    void sink(int k) {
        doHeapify(k, (a, b) -> !unordered(a, b));
    }

    void snake(int k) {
        swimUp(doHeapify(k, (a, b) -> !unordered(a, b)));
    }

    void swimUp(int k) {
        int i = k;
        while (i > first && unordered(parent(i), i)) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    boolean unordered(int i, int j) {
        return (comparator.compare(binHeap[i], binHeap[j]) > 0) ^ max;
    }

    private int doHeapify(int k, BiPredicate<Integer, Integer> p) {
        int i = k;
        int child = firstChild(i);
        while (child <= last + first - 1) {
            int bestChild = child;
            int lastChild = Math.min(last + first - 1, child + 3);
            for (int j = child + 1; j <= lastChild; j++) {
                if (unordered(bestChild, j)) {
                    bestChild = j;
                }
            }
            if (p.test(i, bestChild)) break;
            swap(i, bestChild);
            i = bestChild;
            child = firstChild(i);
        }
        return i;
    }

    private void swap(int i, int j) {
        K tmp = binHeap[i];
        binHeap[i] = binHeap[j];
        binHeap[j] = tmp;
    }

    private int parent(int k) {
        return (k - first - 1) / 4 + first;
    }

    private int firstChild(int k) {
        return (k - first) * 4 + first + 1;
    }

    public Iterator<K> iterator() {
        Collection<K> copy = new ArrayList<>(Arrays.asList(Arrays.copyOf(binHeap, last + first)));
        Iterator<K> result = copy.iterator();
        if (first > 0 && result.hasNext()) result.next();
        return result;
    }

    public static void main(String[] args) {
        FourAryHeap<Integer> heapBasic = new FourAryHeap<>(10, true, Comparator.comparing(Integer::intValue), false);
        FourAryHeap<Integer> heapFloyd = new FourAryHeap<>(10, true, Comparator.comparing(Integer::intValue), true);

        int[] data = {15, 10, 20, 17, 25, 8, 5, 30, 12, 3};

        System.out.println("Basic 4-ary Heap:");
        for (int d : data) {
            heapBasic.give(d);
        }
        System.out.println("Heap content: " + Arrays.toString(heapBasic.binHeap));
        try {
            while (!heapBasic.isEmpty()) {
                System.out.print(heapBasic.take() + " ");
            }
        } catch (PQException e) {
            e.printStackTrace();
        }
        System.out.println("\n----------------------");

        System.out.println("Floyd 4-ary Heap:");
        for (int d : data) {
            heapFloyd.give(d);
        }
        System.out.println("Heap content: " + Arrays.toString(heapFloyd.binHeap));
        try {
            while (!heapFloyd.isEmpty()) {
                System.out.print(heapFloyd.take() + " ");
            }
        } catch (PQException e) {
            e.printStackTrace();
        }
    }
}
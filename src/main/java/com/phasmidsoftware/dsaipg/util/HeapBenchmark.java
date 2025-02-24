package com.phasmidsoftware.dsaipg.util;

import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;
import com.phasmidsoftware.dsaipg.adt.pq.PQException;
import com.phasmidsoftware.dsaipg.adt.pq.PriorityQueue;
import com.phasmidsoftware.dsaipg.adt.pq.FourAryHeap;
import com.phasmidsoftware.dsaipg.adt.pq.FibonacciHeap;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Supplier;

public class HeapBenchmark {
    public static final int M = 4095;
    public static final int NUM_INSERTS = 16000;
    public static final int NUM_REMOVALS = 4000;

    public static final Comparator<Integer> maxHeapComparator = Comparator.comparingInt(Integer::intValue);

    public static void main(String[] args) {

        int numRuns = 5;

        System.out.println("========== Binary Heap (no Floyd) ==========");
        double timeBinary = new Benchmark_Timer<PriorityQueue<Integer>>(
                "Binary Heap (no Floyd)",
                null,
                (heap) -> runBinaryHeapExperiment(heap),
                null
        ).runFromSupplier(binaryHeapSupplier(false), numRuns);
        System.out.println("Average time (ms): " + timeBinary);
        System.out.println();

        System.out.println("========== Binary Heap (Floyd) ==========");
        double timeBinaryFloyd = new Benchmark_Timer<PriorityQueue<Integer>>(
                "Binary Heap (Floyd)",
                null,
                (heap) -> runBinaryHeapExperiment(heap),
                null
        ).runFromSupplier(binaryHeapSupplier(true), numRuns);
        System.out.println("Average time (ms): " + timeBinaryFloyd);
        System.out.println();

        System.out.println("========== 4-ary Heap (no Floyd) ==========");
        double timeFourAry = new Benchmark_Timer<FourAryHeap<Integer>>(
                "4-ary Heap (no Floyd)",
                null,
                (heap) -> runFourAryHeapExperiment(heap),
                null
        ).runFromSupplier(fourAryHeapSupplier(false), numRuns);
        System.out.println("Average time (ms): " + timeFourAry);
        System.out.println();

        System.out.println("========== 4-ary Heap (Floyd) ==========");
        double timeFourAryFloyd = new Benchmark_Timer<FourAryHeap<Integer>>(
                "4-ary Heap (Floyd)",
                null,
                (heap) -> runFourAryHeapExperiment(heap),
                null
        ).runFromSupplier(fourAryHeapSupplier(true), numRuns);
        System.out.println("Average time (ms): " + timeFourAryFloyd);
        System.out.println();

        System.out.println("========== Fibonacci Heap ==========");
        double timeFib = new Benchmark_Timer<FibonacciHeap<Integer>>(
                "Fibonacci Heap",
                null,
                (heap) -> runFibonacciHeapExperiment(heap),
                null
        ).runFromSupplier(fibonacciHeapSupplier(), numRuns);
        System.out.println("Average time (ms): " + timeFib);
        System.out.println();
    }

    private static Supplier<PriorityQueue<Integer>> binaryHeapSupplier(boolean floyd) {
        return () -> new PriorityQueue<>(M, true, maxHeapComparator, floyd);
    }

    private static Supplier<FourAryHeap<Integer>> fourAryHeapSupplier(boolean floyd) {
        return () -> new FourAryHeap<>(M, true, maxHeapComparator, floyd);
    }

    private static Supplier<FibonacciHeap<Integer>> fibonacciHeapSupplier() {
        return () -> new FibonacciHeap<>(true, maxHeapComparator);
    }

    private static Integer runBinaryHeapExperiment(PriorityQueue<Integer> heap) {
        Random rand = new Random();
        Integer spilledCandidate = null;

        for (int i = 0; i < NUM_INSERTS; i++) {
            int x = rand.nextInt();
            if (heap.size() < M) {
                heap.give(x);
            } else {
                int currentMin = Integer.MAX_VALUE;
                for (Integer y : heap) {
                    if (y < currentMin) {
                        currentMin = y;
                    }
                }
                int spilled = (x > currentMin ? currentMin : x);
                if (spilledCandidate == null || spilled > spilledCandidate) {
                    spilledCandidate = spilled;
                }
                heap.give(x);
            }
        }

        for (int j = 0; j < NUM_REMOVALS; j++) {
            try {
                heap.take();
            } catch (PQException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Spilled candidate (Binary Heap " + (heap.toString().contains("Floyd") ? "Floyd" : "Basic") + "): " + spilledCandidate);
        return spilledCandidate;
    }

    private static Integer runFourAryHeapExperiment(FourAryHeap<Integer> heap) {
        Random rand = new Random();
        Integer spilledCandidate = null;

        for (int i = 0; i < NUM_INSERTS; i++) {
            int x = rand.nextInt();
            if (heap.size() < M) {
                heap.give(x);
            } else {
                int currentMin = Integer.MAX_VALUE;
                for (Integer y : heap) {
                    if (y < currentMin) {
                        currentMin = y;
                    }
                }
                int spilled = (x > currentMin ? currentMin : x);
                if (spilledCandidate == null || spilled > spilledCandidate) {
                    spilledCandidate = spilled;
                }
                heap.give(x);
            }
        }

        for (int j = 0; j < NUM_REMOVALS; j++) {
            try {
                heap.take();
            } catch (PQException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Spilled candidate (4-ary Heap " + (heap.toString().contains("Floyd") ? "Floyd" : "Basic") + "): " + spilledCandidate);
        return spilledCandidate;
    }

    private static Integer runFibonacciHeapExperiment(FibonacciHeap<Integer> heap) {
        Random rand = new Random();
        Integer spilledCandidate = null;

        for (int i = 0; i < NUM_INSERTS; i++) {
            int x = rand.nextInt();
            if (heap.size() < M) {
                heap.give(x);
            } else {
                int currentMin = Integer.MAX_VALUE;
                for (Integer y : heap) {
                    if (y < currentMin) {
                        currentMin = y;
                    }
                }
                int spilled = (x > currentMin ? currentMin : x);
                if (spilledCandidate == null || spilled > spilledCandidate) {
                    spilledCandidate = spilled;
                }
                heap.give(x);
            }
        }

        for (int j = 0; j < NUM_REMOVALS; j++) {
            try {
                heap.take();
            } catch (PQException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Spilled candidate (Fibonacci Heap): " + spilledCandidate);
        return spilledCandidate;
    }
}
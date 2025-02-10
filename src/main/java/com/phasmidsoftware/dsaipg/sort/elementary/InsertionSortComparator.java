/*
 * Copyright (c) 2024. Robin Hillyard
 */
package com.phasmidsoftware.dsaipg.sort.elementary;

import com.phasmidsoftware.dsaipg.sort.Helper;
import com.phasmidsoftware.dsaipg.sort.Sort;
import com.phasmidsoftware.dsaipg.sort.SortWithHelper;
import com.phasmidsoftware.dsaipg.util.Config;
import com.phasmidsoftware.dsaipg.util.Config_Benchmark;

import java.io.IOException;
import java.util.Comparator;
import java.util.Random;

import static com.phasmidsoftware.dsaipg.sort.InstrumentedComparatorHelper.getRunsConfig;

/**
 * A class for performing insertion sort using a comparator, extending functionality from SortWithHelper.
 * This includes methods for initialization and invocation of insertion sort,
 * along with specific utilities like counting inversions.
 *
 * @param <X> the type of elements to be sorted, which can be compared using a provided comparator.
 */
public class InsertionSortComparator<X> extends SortWithHelper<X> {
    /**
     * Constructor for InsertionSortComparator, which initializes the comparator with the provided helper.
     *
     * @param helper the Helper object to be used for managing the sorting process.
     */
    public InsertionSortComparator(Helper<X> helper) {
        super(helper);
    }

    /**
     * Constructor for any subclasses to use.
     *
     * @param description the description.
     * @param comparator  the comparator to use.
     * @param N           the number of elements expected.
     * @param nRuns       the number of runs to be expected (this is only significant when instrumenting).
     * @param config      the configuration.
     */
    protected InsertionSortComparator(String description, Comparator<X> comparator, int N, int nRuns, Config config) {
        super(description, comparator, N, nRuns, config);
    }

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param nRuns  the number of runs to be expected (this is only significant when instrumenting).
     * @param config the configuration.
     */
    public InsertionSortComparator(Comparator<X> comparator, int N, int nRuns, Config config) {
        this(DESCRIPTION, comparator, N, nRuns, config);
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    public void sort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();
        // TO BE IMPLEMENTED
        for (int i = from + 1; i < to; i++) {
            int j = i;
            while (j > from && helper.getComparator().compare(xs[j], xs[j - 1]) < 0) {
                helper.swap(xs, j - 1, j);
                --j;
            }
        }

    }

    public static final String DESCRIPTION = "Insertion sort";

    /**
     * Sorts the given array in-place using the provided insertion sort comparator.
     *
     * @param <T> the generic type parameter that extends Comparable.
     * @param ts  the array of elements to be sorted, where elements must implement {@code Comparable}.
     *            The method modifies this array directly to produce the sorted order.
     * @throws RuntimeException if an IOException occurs during the sorting process.
     */
    public static <T extends Comparable<T>> void sort(T[] ts) {
        try (InsertionSortComparator<T> sort = new InsertionSortComparator<>(DESCRIPTION, Comparable::compareTo, ts.length, 1, Config.load(InsertionSortComparator.class))) {
            sort.mutatingSort(ts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a case-insensitive string sorter using an insertion sort comparator.
     *
     * @param n      the expected number of elements to be sorted.
     * @param config the configuration object containing necessary settings.
     * @return a {@code SortWithHelper<String>} instance configured for case-insensitive string sorting.
     */
    public static Sort<String> stringSorterCaseInsensitive(int n, Config config) {
        return new InsertionSortComparator<>(DESCRIPTION, String.CASE_INSENSITIVE_ORDER, n, getRunsConfig(config), config);
    }

    /**
     * This method is designed to count inversions in quadratic time, using insertion sort.
     *
     * @param ts  an array of comparable T elements.
     * @param <T> the underlying type of the elements.
     * @return the number of inversions in ts, which remains unchanged.
     */
    public static <T> long countInversions(T[] ts, Comparator<T> comparator) {
        final Config config = Config_Benchmark.setupConfigFixes();
        try (InsertionSortComparator<T> sorter = new InsertionSortComparator<>(comparator, ts.length, getRunsConfig(config), config)) {
            Helper<T> helper = sorter.getHelper();
            sorter.sort(ts, true);
            return helper.getFixes();
        }
    }

    // Implement a main program (or you could do it via your own unit tests) to actually run the following benchmarks:
    // measure the running times of this sort, using four different initial array ordering situations: random, ordered, partially-ordered and reverse-ordered.
    // I suggest that your arrays to be sorted are of type Integer. Use the doubling method for choosing n and test for at least five values of n.
    public static void main(String[] args) {
        final int initialSize = 1000; // Starting array size
        final int maxDoublings = 5;   // Number of times to double the array size
        final Random random = new Random();

        // Benchmark different array types
        String[] orderTypes = {"Random", "Ordered", "Partially-Ordered", "Reverse-Ordered"};

        for (String orderType : orderTypes) {
            System.out.println("\nBenchmarking Insertion Sort - " + orderType + " Arrays");

            for (int i = 0; i < maxDoublings; i++) {
                int size = initialSize * (1 << i);
                Integer[] array = new Integer[size];

                // Generate arrays based on the type
                switch (orderType) {
                    case "Random":
                        for (int j = 0; j < size; j++) array[j] = random.nextInt(size);
                        break;

                    case "Ordered":
                        for (int j = 0; j < size; j++) array[j] = j;
                        break;

                    case "Partially-Ordered":
                        for (int j = 0; j < size; j++) array[j] = j;
                        for (int j = 0; j < size / 10; j++) {
                            int idx1 = random.nextInt(size);
                            int idx2 = random.nextInt(size);
                            int temp = array[idx1];
                            array[idx1] = array[idx2];
                            array[idx2] = temp;
                        }
                        break;

                    case "Reverse-Ordered":
                        for (int j = 0; j < size; j++) array[j] = size - j;
                        break;
                }

                // Benchmarking
                Config config = Config_Benchmark.setupConfigFixes();
                InsertionSortComparator<Integer> sorter = new InsertionSortComparator<>(
                        InsertionSortComparator.DESCRIPTION,
                        Integer::compareTo,
                        size,
                        1,
                        config
                );

                long startTime = System.nanoTime();
                sorter.mutatingSort(array);
                long endTime = System.nanoTime();

                double elapsedMillis = (endTime - startTime) / 1_000_000.0;
                System.out.printf("Array Size: %d, Time Taken: %.3f ms%n", size, elapsedMillis);
            }
        }
    }

}
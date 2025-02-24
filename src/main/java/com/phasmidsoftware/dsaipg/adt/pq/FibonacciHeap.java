package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.*;
import java.util.function.Consumer;

public class FibonacciHeap<K> implements Iterable<K> {

    private Node<K> max;
    private int n;
    private final Comparator<K> comparator;
    private final boolean isMax;

    private static class Node<K> {
        K key;
        int degree;
        Node<K> parent;
        Node<K> child;
        Node<K> left;
        Node<K> right;
        boolean mark;

        Node(K key) {
            this.key = key;
            this.degree = 0;
            this.parent = null;
            this.child = null;
            this.left = this;
            this.right = this;
            this.mark = false;
        }
    }

    public FibonacciHeap(boolean isMax, Comparator<K> comparator) {
        this.comparator = comparator;
        this.isMax = isMax;
        this.max = null;
        this.n = 0;
    }

    public void give(K key) {
        insert(key);
    }

    public void insert(K key) {
        Node<K> x = new Node<>(key);
        if (max == null) {
            max = x;
        } else {
            x.left = max;
            x.right = max.right;
            max.right.left = x;
            max.right = x;
            if (comparator.compare(x.key, max.key) > 0) {
                max = x;
            }
        }
        n++;
    }

    public K take() throws PQException {
        if (max == null) throw new PQException("Fibonacci Heap is empty");
        Node<K> z = max;
        if (z != null) {
            if (z.child != null) {
                List<Node<K>> children = new ArrayList<>();
                Node<K> x = z.child;
                do {
                    children.add(x);
                    x = x.right;
                } while (x != z.child);
                for (Node<K> child : children) {
                    child.left.right = child.right;
                    child.right.left = child.left;
                    child.left = max;
                    child.right = max.right;
                    max.right.left = child;
                    max.right = child;
                    child.parent = null;
                }
            }
            z.left.right = z.right;
            z.right.left = z.left;
            if (z == z.right) {
                max = null;
            } else {
                max = z.right;
                consolidate();
            }
            n--;
        }
        return z.key;
    }

    private void consolidate() {
        int D = ((int) Math.floor(Math.log(n) / Math.log(2))) + 1;
        List<Node<K>> A = new ArrayList<>(Collections.nCopies(D + 1, null));

        List<Node<K>> rootList = new ArrayList<>();
        if (max != null) {
            Node<K> x = max;
            do {
                rootList.add(x);
                x = x.right;
            } while (x != max);
        }

        for (Node<K> w : rootList) {
            Node<K> x = w;
            int d = x.degree;
            while (A.get(d) != null) {
                Node<K> y = A.get(d);
                if (comparator.compare(x.key, y.key) < 0) {
                    Node<K> temp = x;
                    x = y;
                    y = temp;
                }
                link(y, x);
                A.set(d, null);
                d++;
            }
            A.set(d, x);
        }

        max = null;
        for (Node<K> y : A) {
            if (y != null) {
                if (max == null) {
                    y.left = y;
                    y.right = y;
                    max = y;
                } else {
                    y.left = max;
                    y.right = max.right;
                    max.right.left = y;
                    max.right = y;
                    if (comparator.compare(y.key, max.key) > 0) {
                        max = y;
                    }
                }
            }
        }
    }

    private void link(Node<K> y, Node<K> x) {
        y.left.right = y.right;
        y.right.left = y.left;
        y.parent = x;
        if (x.child == null) {
            x.child = y;
            y.left = y;
            y.right = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right.left = y;
            x.child.right = y;
        }
        x.degree++;
        y.mark = false;
    }

    public int size() {
        return n;
    }

    @Override
    public Iterator<K> iterator() {
        List<K> keys = new ArrayList<>();
        if (max != null) {
            Set<Node<K>> visited = new HashSet<>();
            Node<K> start = max;
            Node<K> curr = start;
            do {
                traverse(curr, visited, keys);
                curr = curr.right;
            } while (curr != start);
        }
        return keys.iterator();
    }

    private void traverse(Node<K> node, Set<Node<K>> visited, List<K> keys) {
        if (!visited.contains(node)) {
            visited.add(node);
            keys.add(node.key);
            if (node.child != null) {
                Node<K> child = node.child;
                Node<K> curr = child;
                do {
                    traverse(curr, visited, keys);
                    curr = curr.right;
                } while (curr != child);
            }
        }
    }
}
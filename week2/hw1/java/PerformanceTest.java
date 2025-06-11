package week2.hw1.java;

import java.util.Random;

public class PerformanceTest {
    public static void main(String[] args) {
        HashTable ht = new HashTable();
        int n = 10000;
        int iterations = 100;
        Random rand = new Random();
        for (int iter = 0; iter < iterations; iter++) {
            long begin = System.nanoTime();
            rand.setSeed(0);
            // System.out.println("-------------- "+iter+" --------------");
            for (int i = 0; i < n; i++) {
                // System.out.println("-------------- "+i+" --------------");
                // int r = i;
                int r = rand.nextInt(100_000_001);
                ht.put(String.valueOf(r), r,0);
            }
            rand.setSeed(0);
            for (int i = 0; i < n; i++) {
                // int r = i;
                int r = rand.nextInt(100_000_001);
                ht.get(String.valueOf(r));
            }
            long end = System.nanoTime();
            System.out.printf("%d %.6f\n", iter, (end - begin) / 1e9);
            System.out.flush();
        }
        for (int iter = 0; iter < iterations; iter++) {
            rand.setSeed(0);
            for (int i = 0; i < n; i++) {
                // int r = i;
                int r = rand.nextInt(100_000_001);
                ht.delete(String.valueOf(r),0);
            }
            // System.out.println(ht.size());
        }
        if (ht.size() != 0) {
            throw new AssertionError("size should be 0 but was " + ht.size());
        }
        System.out.println("Performance tests passed!");
    }
}

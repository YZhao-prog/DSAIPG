/*
 * Copyright (c) 2017-2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.misc;

//class NewtonApproximation {
//    public static void main(String[] args) {
//        // Newton's Approximation to solve cos(x) = x
//        double x = 1.0;
//        int left = 200;
//        for (; left > 0; left--) {
//            final double y = Math.cos(x) - x;
//            if (Math.abs(y) < 1E-7) {
//                System.out.println("the solution to cos(x)=x is: " + x);
//                System.exit(0);
//            }
//            x = x + y / (Math.sin(x) + 1);
//        }
//    }
//}

class NewtonApproximation {
    public static void main(String[] args) {
        // Newton's Approximation to solve x^3 - 2x - 5 = 0
        double x = 2.0;
        int iterations = 200;
        for (; iterations > 0; iterations--) {
            final double y = Math.pow(x, 3) - 2 * x - 5;
            if (Math.abs(y) < 1E-7) {
                System.out.println("the solution to x^3 - 2x - 5 = 0 is: " + x);
                System.exit(0);
            }
            final double derivative = 3 * Math.pow(x, 2) - 2;
            x = x - y / derivative;
        }
    }
}
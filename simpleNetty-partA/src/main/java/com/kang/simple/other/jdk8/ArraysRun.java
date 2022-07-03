package com.kang.simple.other.jdk8;

import java.util.ArrayList;

/**
 * User:
 * Description:
 * Date: 2022-06-04
 * Time: 9:36
 */
public class ArraysRun {

    public static void main(String[] args) {

         ArrayList arrayList = new ArrayList();
        arrayList.add(1);
        arrayList.toString();
        int[] ints = new int[16];
        for (int j = 1; j <16 ; j++) {
            ints[j-1]=j;
        }
        System.out.println(ints.toString());
    }
}

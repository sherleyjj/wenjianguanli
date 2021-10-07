package com.example.common.until;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public abstract class Heap{
    abstract List getSortedListWithNum(int num);
    abstract void constructHeap();

}

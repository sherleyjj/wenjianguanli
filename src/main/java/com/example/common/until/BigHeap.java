package com.example.common.until;

import java.util.*;

public class BigHeap<T> extends Heap{
//    private final Node [] defaultValue = new Node[DefaultSize];
    private Node<T>[] value ;
    private int length;
    private int capacity;
    private static final int DefaultSize = 101;
    private Node topValue;
    private float factors = 0.6f;



    /*
            public methods:
     */
    public void heapAdjust(int index){
        value[0] = value[index];
        while (index <= length / 2){
            int k = 2*index;
            if (value[k + 1]!=null && value[k+1].compareTo(value[k])>0){//右子节点大
               k++;
            }
            if (value[0].compareTo(value[k])>= 0)break;
            else {//子节点更大
                value[index] = value[k];
                index = k;
            }
        }
        value[index] = value[0];
    }
    public List getSortedList(){
        return getSortedListWithNum(length);
    }
    @Override
    public List getSortedListWithNum(int num) {
        if (num > length){
            throw new RuntimeException("num is so large,please reduce this number and to ensure less than 'lenth'");
        }
        List<Node> reslut = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            reslut.add(pop());
        }
        return reslut;
    }

    /**
     * 有bug
     * @param key
     * @param data
     */
    public void add(Integer key , T data){
        AbstractMap.SimpleEntry node = new AbstractMap.SimpleEntry(key,data);
        add(node);
    }
    public void add(Map.Entry<Integer,T> node){
        if (length > capacity /2){
            broadenSize();
        }
        value[++length]=new Node<>(node.getKey(),node.getValue());
        constructHeap();
    }

    @Override
     public void constructHeap() {
        for (int i = length / 2; i > 0; --i) {
            heapAdjust(i);
        }
     }

    /**
     * 优化外部直接输入
     * @param nodes
     */
    public void buildHeapWithHashMap(Map<Integer,T> nodes){
        if (nodes.size()<DefaultSize){
            value = new Node[DefaultSize];
            capacity = DefaultSize - 1;
        }else {
            value = new Node[nodes.size()+DefaultSize];
            capacity = DefaultSize -1 +nodes.size();
        }
        Iterator<Map.Entry<Integer,T>> iterator= nodes.entrySet().iterator();
        int index = 1;
        while (iterator.hasNext()){
            Map.Entry<Integer,T> nowValue =  iterator.next();
            value[index++] = new Node<T>(nowValue.getKey(),nowValue.getValue());
            length++;
            broadenSize();
        }
        constructHeap();
    }

    public Node pop(){
        if (empty()){
            throw new RuntimeException("Heap in empty");
        }
        topValue = value[1];
        value[1] = value[length];
        value[length]=null;
        length--;
        constructHeap();
        return topValue;
    }
    public Node getTopValue(){
        if (empty()){
            throw new RuntimeException("Heap in empty");
        }
        return value[1];
    }
    public boolean empty(){
        return length == 0;
    }
    public BigHeap(){

    }



/*
      private methods:
 */
    private void broadenSize(){
        if (capacity *factors <= length){
            int newCapcity = length * 2;
            Node<T> [] newNodes = new Node[newCapcity +1];
            capacity = newCapcity;
            for (int i = 1; i <= length; i++) {
                newNodes[i]=value[i];
            }
            value = newNodes;
        }
    }





    class Node <T> implements Comparable, Map.Entry {
        private Integer keyValue;
        private T data;

        public Node(Integer key , T data){
            keyValue = key;
            this.data=data;
        }
        @Override
        public int compareTo(Object o) {
            Node node = (Node)o;
            return keyValue - node.keyValue;
        }
        @Override
        public Object getKey() {
            return keyValue;
        }

        @Override
        public Object getValue() {
            return data;
        }

        @Override
        public Object setValue(Object value) {
            return keyValue = (Integer)value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return keyValue == node.keyValue;
        }

        @Override
        public int hashCode() {
            return Objects.hash(keyValue);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "keyValue=" + keyValue +
                    ", data=" + data +
                    '}';
        }
    }
}

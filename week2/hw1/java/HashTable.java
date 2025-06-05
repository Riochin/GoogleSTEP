package week2.hw1.java;

public class HashTable {
    private int bucketSize;
    private Item[] buckets;
    private int itemCount;

    public int calculateHash(String key){
        assert (key instanceof String);
        int hash = 1;
        for(char c : key.toCharArray()){
            hash = (int)(((long)hash * (long)c) % 1000000009L);
        }
        return hash;
    }

    public HashTable(){
        this.bucketSize = 97;
        this.buckets = new Item[bucketSize];
        this.itemCount = 0;
    }

    public void rehash(int newSize){
        Item[] oldBuckets = this.buckets;
        this.buckets = new Item[newSize];
        this.bucketSize = newSize;
        this.itemCount = 0;

        for(Item item :oldBuckets){
            while(item != null){
                this.put(item.getKey(), item.getValue());
                item = item.getNext();
            }
        }

        return;
    }

    public void checkSize(){
        if(this.itemCount <= this.bucketSize * 0.3){
            this.rehash(this.bucketSize / 2);
        } else if (this.itemCount >= this.bucketSize * 0.7){
            this.rehash(this.bucketSize * 2);
        }

        return;
    }

    public boolean put(String key, int value){
        assert key instanceof String; //カッコなしでもいけるんかーい
        this.checkSize();
        int bucketIdx = Math.floorMod(calculateHash(key), this.bucketSize);
        Item item = this.buckets[bucketIdx];

        while (item != null){
            if(item.getKey() == key){
                item.setValue(value);
                return false;
            }
            item = item.getNext();
        }
        Item newItem = new Item(key, value, this.buckets[bucketIdx]);
        this.buckets[bucketIdx] = newItem;
        this.itemCount ++;

        return true;
    }

    public int get(String key){
        assert key instanceof String;
        int bucketIdx = Math.floorMod(calculateHash(key), this.bucketSize);
        Item item = this.buckets[bucketIdx];

        while (item != null){
            return (item.getValue());
        }

        return -1;
    }

    public boolean delete(String key){
        assert key instanceof String;

        this.checkSize();
        int bucketIdx = Math.floorMod(calculateHash(key), this.bucketSize);
        Item item = this.buckets[bucketIdx];
        Item prev = null;

        while (item != null){
            if(item.getKey() == key){
                if(prev == null){
                    this.buckets[bucketIdx] = item.getNext();
                } else {
                    prev.setNext(item.getNext());
                }
                this.itemCount --;

                return true;
            }
            prev = item;
            item = item.getNext();
        }
        return false;
    }

    public int size(){
        return this.itemCount;
    }

    public static void main(String[] args) {
        
    }
}

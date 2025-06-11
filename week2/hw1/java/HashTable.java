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

    public void rehash(int newSize, int depth){
        Item[] oldBuckets = this.buckets;
        this.buckets = new Item[newSize];
        this.bucketSize = newSize;
        this.itemCount = 0;

        for(Item item :oldBuckets){
            while(item != null){
                this.put(item.getKey(), item.getValue(),depth+1 );
                item = item.getNext();
            }
        }

        return;
    }

    public void checkSize(int depth){
        if(this.itemCount <= this.bucketSize * 0.3){
            this.rehash(this.bucketSize / 2, depth+1);
        } else if (this.itemCount >= this.bucketSize * 0.7){
            this.rehash(this.bucketSize * 2,depth+1);
        }

        return;
    }

    public boolean put(String key, int value, int depth){
        // System.out.println(depth);
        // System.out.println("bucketSize"+this.bucketSize);
        // System.out.println("itemCnt"+this.itemCount);
        assert key instanceof String; //カッコなしでもいけるんかーい
       
        int bucketIdx = calculateHash(key) % this.bucketSize;
        Item item = this.buckets[bucketIdx];

        // System.out.println("Whileの前");
        while (item != null){
            // System.out.println(item + "をチェック中");
            if(item.getKey() == key){
                item.setValue(value);
                return false;
            }
            item = item.getNext();
        }
        // System.out.println("whileの後");
        Item newItem = new Item(key, value, this.buckets[bucketIdx]);
        this.buckets[bucketIdx] = newItem;
        this.itemCount ++;

        if (this.itemCount >= this.bucketSize * 0.7){
            this.rehash(this.bucketSize*2,depth+1);
        }
        

        return true;
    }

    public int get(String key){
        assert key instanceof String;
        int bucketIdx = calculateHash(key) % this.bucketSize;
        Item item = this.buckets[bucketIdx];

        while (item != null){
            return (item.getValue());
        }

        return -1;
    }

    public boolean delete(String key, int depth){
        assert key instanceof String;

        int bucketIdx = calculateHash(key) % this.bucketSize;
        Item item = this.buckets[bucketIdx];
        Item prev = null;

        while (item != null){
            // System.out.println("います");
            if(item.getKey().equals(key)){
                // System.out.println("います");
                if(prev == null){
                    this.buckets[bucketIdx] = item.getNext();
                } else {
                    prev.setNext(item.getNext());
                }
                this.itemCount --;

                if(this.itemCount <= this.bucketSize * 0.3){
                    this.rehash(this.bucketSize/2,depth+1);
                }
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

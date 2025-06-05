package week2.hw1.java;

public class Item {
    private String key;
    private int value;
    private Item next;

    public Item(String key, int value, Item next){
        assert (key instanceof String);
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public Item getNext(){
        return this.next;
    }

    public String getKey(){
        return this.key;
    }

    public int getValue(){
        return this.value;
    }

    public void setValue(int value){
        this.value = value;
        return;
    }

    public void setNext(Item item){
        this.next = item;
        return;
    }
}

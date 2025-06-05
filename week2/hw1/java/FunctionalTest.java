package week2.hw1.java;

public class FunctionalTest {
    public static void main(String[] args) {
        HashTable ht = new HashTable();
        // put
        assert ht.put("a", 1) : "put a failed";
        assert !ht.put("a", 2) : "put a (update) should return false";
        assert ht.put("b", 3) : "put b failed";
        // get
        assert ht.get("a") == 2 : "get a should be 2";
        assert ht.get("b") == 3 : "get b should be 3";
        assert ht.get("c") == -1 : "get c should be -1 (not found)";
        // delete
        assert ht.delete("a") : "delete a failed";
        assert !ht.delete("a") : "delete a (again) should return false";
        assert ht.get("a") == -1 : "get a after delete should be -1";
        // size
        assert ht.size() == 1 : "size should be 1";
        System.out.println("FunctionalTest passed");
    }
}

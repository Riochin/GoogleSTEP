package week1.hw2;

import java.util.HashMap;

public class MakeAlphaDict {
    public static void main(String[] args) {
        java.util.ArrayList<String> input = new java.util.ArrayList<>();
        input.add("aa");
        input.add("bb");
        HashMap<String, int[]> a = Main.makeAlphaDict(input);
        System.out.println(a.get("aa")['z'-'a']);
    }
}

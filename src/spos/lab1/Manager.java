package spos.lab1;

import spos.lab1.demo.*;
import java.util.Scanner;

public class Manager {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter x");
        int x = in.nextInt();
        try {
            DoubleOps.funcG(x);
            DoubleOps.funcF(x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

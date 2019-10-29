package spos.lab1;


import java.io.IOException;
import java.nio.channels.Pipe;
import java.util.Scanner;

import static spos.lab1.Pipe.ReceivePipe;
import static spos.lab1.Pipe.SendPipe;

public class Manager {
    public static void main(String[] args){
        Manager manager = new Manager();
        manager.start();
    }

    public void start(){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter x");
        int x = in.nextInt();

        Pipe pipe1 = null;
        Pipe pipe2 = null;
        try {
            pipe1 = Pipe.open();
            pipe2 = Pipe.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SendPipe(x, pipe1);
        SendPipe(x, pipe2);

        funcGThread dltask1 = new funcGThread(pipe1);
        Thread GTread = new Thread(dltask1);
        GTread.start();

        funcFThread dltask2 = new funcFThread(pipe2);
        Thread FTread = new Thread(dltask2);
        FTread.start();

        Print(FTread, GTread, pipe1, pipe2);
    }

    public void Print(Thread FTread, Thread GTread, Pipe pipe1, Pipe pipe2){

        boolean flag1 = false;
        boolean flag2 = false;

        while (flag1 == false || flag2 == false) {
            if (!GTread.isAlive() && flag1 == false){
                System.out.println("funcG: " + ReceivePipe(pipe1));
                flag1 = true;
            }
            if (!FTread.isAlive() && flag2 == false){
                System.out.println("funcF: " + ReceivePipe(pipe2));
                flag2 = true;
            }
        }
    }
}

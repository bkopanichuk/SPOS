package spos.lab1;


import java.io.IOException;
import java.nio.channels.Pipe;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static spos.lab1.Pipe.ReceivePipe;
import static spos.lab1.Pipe.SendPipe;

public class Manager {
    static Thread GTread;
    static Thread FTread;
    static boolean flagPrompt = false;

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
        GTread = new Thread(dltask1);
        GTread.start();

        funcFThread dltask2 = new funcFThread(pipe2);
        FTread = new Thread(dltask2);
        FTread.start();

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                PeriodicPrompt prompt = new PeriodicPrompt();
                prompt.periodicprompt();
            }
        }, 0, 10000);

        Print(FTread, GTread, pipe1, pipe2);
    }

    public void Print(Thread FTread, Thread GTread, Pipe pipe1, Pipe pipe2){

        boolean flag1 = false;
        boolean flag2 = false;

        while (flag1 == false || flag2 == false) {
            if (!GTread.isAlive() && flag1 == false && flagPrompt == false){
                System.out.println("funcG: " + ReceivePipe(pipe1));
                flag1 = true;
            }
            if (!FTread.isAlive() && flag2 == false && flagPrompt == false){
                System.out.println("funcF: " + ReceivePipe(pipe2));
                flag2 = true;
            }
        }
    }
}

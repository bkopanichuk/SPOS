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
        }, 10000, 10000);

        getResult(FTread, GTread, pipe1, pipe2);
    }

    public void getResult(Thread FTread, Thread GTread, Pipe pipe1, Pipe pipe2){
        String res_g = "";
        String res_f = "";

        while (res_g.length() == 0 || res_f.length() == 0) {
            if (!GTread.isAlive() && res_g.length() == 0 && flagPrompt == false){
                res_g += ReceivePipe(pipe1);
                if (res_g == "0.0"){
                    System.out.println("funcG: " + res_g);
                    System.exit(0);
                }
                System.out.println("funcG: " + res_g);
            }
            if (!FTread.isAlive() && res_f.length() == 0 && flagPrompt == false){
                res_f += ReceivePipe(pipe2);
                if (res_f == "0.0"){
                    System.out.println("funcF: " + res_g);
                    System.exit(0);
                }
                System.out.println("funcF: " + res_f);
            }
        }

        System.out.println("Answer: " + String.valueOf(Double.valueOf(res_g) * Double.valueOf(res_f)));

        System.exit(0);
    }
}

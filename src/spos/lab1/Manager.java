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
    private boolean flagPrompt = false;

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
                periodicprompt();
            }
        }, 0, 10000);

        getResult(FTread, GTread, pipe1, pipe2);
    }

    public void getResult(Thread FTread, Thread GTread, Pipe pipe1, Pipe pipe2){
        String res_g = "";
        String res_f = "";

        while (res_g.length() == 0 || res_f.length() == 0) {
            if (!GTread.isAlive() && res_g.length() == 0 && flagPrompt == false){
                res_g += ReceivePipe(pipe1);
                System.out.println("funcG: " + res_g);
                if (Double.valueOf(res_g) == 0.0){
                    System.out.println("Answer: " + res_g);
                    System.exit(0);
                }
            }
            if (!FTread.isAlive() && res_f.length() == 0 && flagPrompt == false){
                res_f += ReceivePipe(pipe2);
                System.out.println("funcF: " + res_f);

                if (Double.valueOf(res_f) == 0.0){
                    System.out.println("Answer: " + res_f);
                    System.exit(0);
                }
            }
        }

        System.out.println("Answer: " + String.valueOf(Double.valueOf(res_g) * Double.valueOf(res_f)));

        System.exit(0);
    }

    public void periodicprompt() {
        flagPrompt = true;
        System.out.println("Choose:\n1 - continue\n2 - continue without prompt\n3 - cancel");
        Scanner in = new Scanner(System.in);
        int x = in.nextInt();
        switch (x){
            case 1:
                flagPrompt = false;
                break;
            case 2:
                flagPrompt = false;
                break;
            case 3:
                flagPrompt = false;
                System.exit(0);
                break;
        }
    }
}

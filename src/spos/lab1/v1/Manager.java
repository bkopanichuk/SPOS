package spos.lab1.v1;


import java.io.IOException;
import java.nio.channels.Pipe;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static spos.lab1.v1.Pipe.ReceivePipe;
import static spos.lab1.v1.Pipe.SendPipe;

public class Manager {
    static Thread GTread;
    static Thread FTread;
    private boolean flagPrompt = false;
    private boolean flagFuncG = false;
    private boolean flagFuncF = false;
    private Timer promptTimer;
    private String res_g = "";
    private String res_f = "";

    public static void main(String[] args){
        Manager manager = new Manager();
        manager.start();
    }

    public void start(){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter function`s argument x: ");
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

        startTimer(4000);

        getResult(FTread, GTread, pipe1, pipe2);
    }

    public void getResult(Thread FTread, Thread GTread, Pipe pipe1, Pipe pipe2){
        while (res_g.length() == 0 || res_f.length() == 0) {
            if (!GTread.isAlive() && res_g.length() == 0){
                res_g += ReceivePipe(pipe1);
                if (flagPrompt == false && flagFuncG == false) {
                    printResFuncG();
                    flagFuncG = true;
                }
                if (Double.valueOf(res_g) == 0.0 && flagPrompt == false){
                    System.out.println("Answer: " + res_g);
                    System.exit(0);
                }
            }
            if (!FTread.isAlive() && res_f.length() == 0){
                res_f += ReceivePipe(pipe2);
                if (flagPrompt == false && flagFuncF == false) {
                    printResFuncF();
                    flagFuncF = true;
                }
                if (Double.valueOf(res_f) == 0.0 && flagPrompt == false){
                    System.out.println("Answer: " + res_f);
                    System.exit(0);
                }
            }
        }

        if (flagPrompt == false) {
            System.out.println("Answer: " + String.valueOf(Double.valueOf(res_g) * Double.valueOf(res_f)));
            System.exit(0);
        }
    }

    public void periodicprompt() {
        stopTimer();
        flagPrompt = true;
        System.out.println("Choose:\n1 - continue computation\n2 - continue computation without prompt\n3 - cancel computation");
        Scanner in = new Scanner(System.in);
        int x = in.nextInt();
        switch (x){
            case 1:
                continueCase();
                startTimer(5000);
                flagPrompt = false;
                break;
            case 2:
                continueCase();
                flagPrompt = false;
                break;
            case 3:
                cancelCase();
                System.exit(0);
                break;
        }
    }

    public void cancelCase() {
        if (res_f == "" && res_g == "") {
            System.out.println("Answer is undefind because f and g hasn't been counted");
        } else if (res_f == "" && res_g != "") {
            checkShortCircuitFuncG();
            printResFuncG();
            System.out.println("Answer is undefind because f hasn't been counted");
        } else if (res_f != "" && res_g == "") {
            checkShortCircuitFuncF();
            printResFuncF();
            System.out.println("Answer is undefind because g hasn't been counted");
        } else if (res_f != "" && res_g != "") {
            printResFuncG();
            printResFuncF();
            System.out.println("Answer: " + String.valueOf(Double.valueOf(res_g) * Double.valueOf(res_f)));
        }
    }

    public void continueCase(){
        if (res_g != ""){
            checkShortCircuitFuncG();
            printResFuncG();
        }
        if (res_f != "") {
            checkShortCircuitFuncF();
            printResFuncF();
        }
        if (res_f != "" && res_g != "") {
            System.out.println("Answer: " + String.valueOf(Double.valueOf(res_g) * Double.valueOf(res_f)));
            System.exit(0);
        }
    }

    public void checkShortCircuitFuncG(){
        if (Double.valueOf(res_g) == 0.0){
            printResFuncG();
            System.out.println("Answer: " + res_g);
            System.exit(0);
        }
    }

    public void checkShortCircuitFuncF(){
        if (Double.valueOf(res_f) == 0.0){
            System.out.println("funcF: " + res_f);
            System.out.println("Answer: " + res_f);
            System.exit(0);
        }
    }

    public void printResFuncG(){
        if (flagFuncG == false) {
            System.out.println("funcG: " + res_g);
            flagFuncG = true;
        }
    }

    public void printResFuncF(){
        if (flagFuncF == false) {
            System.out.println("funcF: " + res_f);
            flagFuncF = true;
        }
    }

    public void startTimer(int delay) {
        promptTimer = new Timer();
        promptTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                periodicprompt();
            }
        }, delay);
    }

    public void stopTimer() {
        promptTimer.cancel();
    }
}

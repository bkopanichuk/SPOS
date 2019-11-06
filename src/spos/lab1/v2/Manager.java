package spos.lab1.v2;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.IOException;
import java.nio.channels.Pipe;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.jnativehook.GlobalScreen.unregisterNativeHook;
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

        KeyListener EcapseKeyListener = new KeyListener();
        EcapseKeyListener.Start();

        getResult(FTread, GTread, pipe1, pipe2);
    }

    public void getResult(Thread FTread, Thread GTread, Pipe pipe1, Pipe pipe2){
        while (res_g.length() == 0 || res_f.length() == 0) {
            res_g = ReceivePipe(pipe1);
            if (res_g != ""){
                if (flagPrompt == false && flagFuncG == false) {
                    printResFuncG();
                    flagFuncG = true;
                }
                if (Double.valueOf(res_g) == 0.0 && flagPrompt == false){
                    System.out.println("Answer: " + res_g);
                    System.exit(0);
                }
            }
            res_f = ReceivePipe(pipe2);
            if (res_f != ""){
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

    public void cancelCase() {
        if (res_f == "" && res_g == "") {
            System.out.println("Answer is undefind because f and g hasn't been counted");
            System.exit(0);
        } else if (res_f == "" && res_g != "") {
            checkShortCircuitFuncG();
            printResFuncG();
            System.out.println("Answer is undefind because f hasn't been counted");
            System.exit(0);
        } else if (res_f != "" && res_g == "") {
            checkShortCircuitFuncF();
            printResFuncF();
            System.out.println("Answer is undefind because g hasn't been counted");
            System.exit(0);
        } else if (res_f != "" && res_g != "") {
            printResFuncG();
            printResFuncF();
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

    public class KeyListener implements NativeKeyListener {
        public void nativeKeyPressed(NativeKeyEvent e) {
            if (NativeKeyEvent.getKeyText(e.getKeyCode()) == "Escape"){
                cancelCase();
            }

            if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                try {
                    unregisterNativeHook();
                } catch (NativeHookException ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void nativeKeyReleased(NativeKeyEvent e) {
        }

        public void nativeKeyTyped(NativeKeyEvent e) {
        }

        public void Start() {
            Handler[] handlers = Logger.getLogger("").getHandlers();
            for (int i = 0; i < handlers.length; i++) {
                handlers[i].setLevel(Level.OFF);
            }
            try {
                GlobalScreen.registerNativeHook();
            }
            catch (NativeHookException ex) {
                System.err.println("There was a problem registering the native hook.");
                System.err.println(ex.getMessage());

                System.exit(1);
            }

            GlobalScreen.addNativeKeyListener(new KeyListener());
        }
    }
}

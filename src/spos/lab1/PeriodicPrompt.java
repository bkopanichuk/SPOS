package spos.lab1;

import java.util.Scanner;
import java.util.TimerTask;

public class PeriodicPrompt{
    public void periodicprompt() {
        Manager.flagPrompt = true;
        System.out.println("(Pause) Choose:\n1 - continue\n2 - continue without prompt\n3 - cancel");
        Scanner in = new Scanner(System.in);
        int x = in.nextInt();
        switch (x){
            case 1:
                Manager.flagPrompt = false;
                break;
            case 2:
                Manager.flagPrompt = false;
                break;
            case 3:
                Manager.flagPrompt = false;
                break;
        }
    }
}

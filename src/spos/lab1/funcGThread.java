package spos.lab1;

import spos.lab1.demo.*;
import java.io.IOException;
import java.nio.channels.Pipe;

public class funcGThread implements Runnable{
    @Override
    public void run() {

    }

    public void StartFunc() throws IOException {
        try {
            System.out.println(DoubleOps.funcG(x));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

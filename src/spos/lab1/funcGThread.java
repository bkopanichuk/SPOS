package spos.lab1;

import spos.lab1.demo.*;

import java.nio.channels.Pipe;

public class funcGThread implements Runnable{
    Pipe pipe;

    public funcGThread(Pipe pipe){
        this.pipe = pipe;
    }

    @Override
    public void run() {
        StartFunc(pipe);
    }

    public void StartFunc(Pipe pipe){
        String res = Manager.ReceivePipe(pipe);
        int x = Integer.parseInt(res);

        try {
            System.out.println(DoubleOps.funcG(x));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

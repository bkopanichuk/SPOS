package spos.lab1;

import spos.lab1.demo.DoubleOps;

import java.nio.channels.Pipe;

public class funcFThread implements Runnable{
    Pipe pipe;

    public funcFThread(Pipe pipe){
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
            System.out.println(DoubleOps.funcF(x));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


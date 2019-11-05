package spos.lab1.v2;

import spos.lab1.demo.DoubleOps;

import java.nio.channels.Pipe;

public class funcGThread implements Runnable{
    Pipe pipe = null;

    public funcGThread(Pipe pipe){
        this.pipe = pipe;
    }

    @Override
    public void run() {
        StartFunc(pipe);
    }

    public void StartFunc(Pipe pipe){
        String res = spos.lab1.v2.Pipe.ReceivePipe(pipe);
        int x = Integer.parseInt(res);

        try {
            spos.lab1.v2.Pipe.SendPipe(DoubleOps.funcG(x), pipe);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

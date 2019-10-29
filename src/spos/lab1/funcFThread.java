package spos.lab1;

import spos.lab1.demo.DoubleOps;
import java.nio.channels.Pipe;

public class funcFThread implements Runnable{
    Pipe pipe = null;

    public funcFThread(Pipe pipe){
        this.pipe = pipe;
    }

    @Override
    public void run() {
        StartFunc(pipe);
    }

    public void StartFunc(Pipe pipe){
        String res = spos.lab1.Pipe.ReceivePipe(pipe);
        int x = Integer.parseInt(res);
        try {
            Thread.sleep(100);     // requirement 4
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            spos.lab1.Pipe.SendPipe(DoubleOps.funcF(x), pipe);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

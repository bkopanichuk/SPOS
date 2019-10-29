package spos.lab1;

import com.sun.org.apache.xpath.internal.functions.FuncGenerateId;
import spos.lab1.demo.DoubleOps;

import spos.lab1.demo.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.Scanner;

public class Manager {
    public static void main(String[] args){
        Manager manager = new Manager();
        manager.start();
    }

    public void start(){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter x");
        int x = in.nextInt();
        Pipe pipe1 = SendPipe(x);
        Pipe pipe2 = SendPipe(x);

        funcFThread dltask2 = new funcFThread(pipe2);
        Thread FTread = new Thread(dltask2);
        FTread.start();

        funcGThread dltask1 = new funcGThread(pipe1);
        Thread GTread = new Thread(dltask1);
        GTread.start();
    }

    public static <T> Pipe SendPipe(T x) {
        Pipe pipe = null;
        try {
            pipe = Pipe.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Pipe.SinkChannel sinkChannel = pipe.sink();

        ByteBuffer buf = ByteBuffer.allocate(255);
        buf.clear();
        String y = new String(String.valueOf(x));
        buf.put(y.getBytes());

        buf.flip();

        while(buf.hasRemaining()) {
            try {
                sinkChannel.write(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pipe;
    }

    public static String ReceivePipe(Pipe pipe){
        Pipe.SourceChannel sourceChannel = pipe.source();
        ByteBuffer buf = ByteBuffer.allocate(255);
        String res = "";
        try {
            while(sourceChannel.read(buf) > 0){
                //limit is set to current position and position is set to zero
                buf.flip();
                while(buf.hasRemaining()){
                    char ch = (char) buf.get();
                    res = res + String.valueOf(ch);
                }
                //position is set to zero and limit is set to capacity to clear the buffer.
                buf.clear();
                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}

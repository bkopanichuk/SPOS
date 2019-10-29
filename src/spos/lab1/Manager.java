package spos.lab1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.Scanner;

public class Manager {
    public void SendPipe(int x) {
        Pipe pipe = null;
        try {
            pipe = Pipe.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Pipe.SinkChannel sinkChannel = pipe.sink();

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put((byte) x);

        buf.flip();

        while(buf.hasRemaining()) {
            try {
                sinkChannel.write(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter x");
        int x = in.nextInt();

        Thread funcGT = new Thread(new funcGThread());
        funcGT.start();
    }
}

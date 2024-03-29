package spos.lab1.v2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class Pipe {
    public static <T> java.nio.channels.Pipe SendPipe(T x, java.nio.channels.Pipe pipe) {
        java.nio.channels.Pipe.SinkChannel sinkChannel = pipe.sink();

        try {
            sinkChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteBuffer buf = ByteBuffer.allocate(255);
        buf.clear();
        String y = new String(String.valueOf(x));
        buf.put(y.getBytes());

        buf.flip();

        while (buf.hasRemaining()) {
            try {
                sinkChannel.write(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pipe;
    }

    public static String ReceivePipe(java.nio.channels.Pipe pipe){
        java.nio.channels.Pipe.SourceChannel sourceChannel = pipe.source();

        try {
            sourceChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

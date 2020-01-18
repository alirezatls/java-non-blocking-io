package input;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NioInputOperation {

    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final ByteBuffer BUFFER = ByteBuffer.allocate(2048);
    private static final Path PATH = Paths.get("src/main/resources/input.txt");


    //--------------------------------------------------------
    // read content of input.txt and keep it in ByteBuffer
    //--------------------------------------------------------
    public static void readFromFile() throws IOException {
        FileChannel channel = FileChannel.open(PATH, StandardOpenOption.READ, StandardOpenOption.WRITE);
        channel.read(BUFFER);
        BUFFER.flip();
        CharBuffer charBuffer = UTF8.decode(BUFFER);
        System.out.println(charBuffer.toString());
    }
}

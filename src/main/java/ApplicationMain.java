import input.NioInputOperation;
import output.NioOutputOperation;
import support.FileOperation;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApplicationMain {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/sample.txt");
        FileOperation.createNewChannel(path);
    }

    private static void asyncFileChannel() throws IOException {
        FileSystemProvider provider = FileSystems.getDefault().provider();
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("async io in java is awesome !!!");
        Path path = Paths.get("test1.txt");
        Set<StandardOpenOption> options = Set.of(StandardOpenOption.WRITE,
                StandardOpenOption.READ,
                StandardOpenOption.CREATE);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        AsynchronousFileChannel asynchronousFileChannel = provider.newAsynchronousFileChannel(path, options, executorService);
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);
        Future<Integer> future = asynchronousFileChannel.write(byteBuffer, 0);
    }

    private static void providerInputStream() throws IOException {
        FileSystemProvider provider = FileSystems.getDefault().provider();
        InputStream inputStream = provider.newInputStream(Paths.get("sample.txt"), StandardOpenOption.READ);
        int i = 0;
        while ((i = inputStream.read()) != -1) {
            System.out.print((char) i);
        }
    }

    private static void copyWithOption() throws IOException {
        Files.copy(Paths.get("sample.txt"), Paths.get("test1.txt"),
                StandardCopyOption.COPY_ATTRIBUTES);
    }

    private static void copyWithOutputStream() throws IOException {
        Files.copy(Paths.get("sample.txt"),
                new FileOutputStream(new File("test.txt")));
    }

    private static void usageOfRewind() {
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        buffer.putInt(10);
        buffer.putInt(20);
        System.out.println("BEFORE");
        System.out.println("position : " + buffer.position());
        System.out.println("limit : " + buffer.limit());
        System.out.println("capacity : " + buffer.capacity());
        buffer.rewind();
        System.out.println("AFTER");
        System.out.println("position : " + buffer.position());
        System.out.println("limit : " + buffer.limit());
        System.out.println("capacity : " + buffer.capacity());
    }

    private static void usageOfFlip() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.putInt(10);
        byteBuffer.putInt(20);
        System.out.println("BEFORE");
        System.out.println("position : " + byteBuffer.position());
        System.out.println("limit : " + byteBuffer.limit());
        byteBuffer.flip();
        System.out.println("AFTER");
        System.out.println("position : " + byteBuffer.position());
        System.out.println("limit : " + byteBuffer.limit());
    }

    private static void readFromFile() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        Charset utf8 = StandardCharsets.UTF_8;
        FileChannel channel = FileChannel.open(Paths.get("sample.txt"), StandardOpenOption.READ);
        channel.read(byteBuffer);
        byteBuffer.flip();
        CharBuffer charBuffer = utf8.decode(byteBuffer);
        System.out.println(charBuffer.toString());
    }

    private static void writeToFile() throws IOException {
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("this is a test");
        Charset charset = StandardCharsets.UTF_8;
        FileChannel channel = FileChannel.open(Paths.get("sample.txt"),
                StandardOpenOption.READ,
                StandardOpenOption.WRITE);
        charBuffer.flip();
        ByteBuffer byteBuffer = charset.encode(charBuffer);
        channel.write(byteBuffer);
    }
}

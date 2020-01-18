package output;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioOutputOperation {

    private static final String TEXT = "this is a simple utf-8 text";
    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final CharBuffer BUFFER = CharBuffer.allocate(2048);

    //--------------------------------------------------------
    // write text to file that exist and overwrite content every time and create file every time
    // throw FileNotFoundException if file exist
    //--------------------------------------------------------
    public static void writeTextToExistFile(String text, Path path) throws IOException {
        BUFFER.put(text);
        FileChannel fileChannel = FileChannel.open(path,
                StandardOpenOption.WRITE,
                StandardOpenOption.READ);
        BUFFER.flip();
        fileChannel.write(UTF8.encode(BUFFER));
        BUFFER.clear();
    }

    //--------------------------------------------------------
    // write text to file that exist and append to previous content(new line)
    // READ + APPEND NOT ALLOWED
    //--------------------------------------------------------
    public static void appendTextToFile(Path newPath) throws IOException {
        BUFFER.put(TEXT);
        BUFFER.put("\n");
        FileChannel fileChannel = FileChannel.open(newPath,
                StandardOpenOption.READ,
                StandardOpenOption.WRITE,
                StandardOpenOption.APPEND);
        BUFFER.flip();
        fileChannel.write(UTF8.encode(BUFFER));
        BUFFER.clear();
    }

    //--------------------------------------------------------
    // write text to file that not exist
    // throw FileAlreadyExistsException if file exist
    //--------------------------------------------------------
    public static void writeTextToNewFile(Path newPath) throws IOException {
        BUFFER.put(TEXT);
        BUFFER.put("\n");
        FileChannel fileChannel = FileChannel.open(newPath,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE_NEW);
        BUFFER.flip();
        fileChannel.write(UTF8.encode(BUFFER));
        BUFFER.clear();
    }

    //--------------------------------------------------------
    // create a new Async File Channel
    // a Set for Options
    // an executor for Thread pool
    //--------------------------------------------------------
    public static void writeToFileAsync(Path path) throws IOException {
        BUFFER.put(TEXT);
        BUFFER.put("\n");
        FileSystemProvider provider = FileSystems.getDefault().provider();
        Set<StandardOpenOption> options = Set.of(StandardOpenOption.READ,
                StandardOpenOption.WRITE);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        AsynchronousFileChannel asyncChannel = provider.newAsynchronousFileChannel(path, options, executorService);
        asyncChannel.write(UTF8.encode(BUFFER),0);
    }

}

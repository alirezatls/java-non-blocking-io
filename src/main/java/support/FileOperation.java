package support;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class FileOperation {

    private static final Path TEMP = Paths.get(System.getProperty("java.io.tmpdir"));

    //--------------------------------------------------------
    // not follow symbolic links in the file system to determine if the path exists
    //--------------------------------------------------------
    public static void checkPath(Path path) {
        boolean exists = Files.exists(path, LinkOption.NOFOLLOW_LINKS);
        if (exists) {
            System.out.printf("file with path ==> %s exist", path.toString());
        } else {
            System.out.printf("file with path ==> %s not exist", path.toString());
        }
    }

    public static void createFile(Path path) throws IOException {
        boolean status = Files.notExists(path);
        if (status) {
            Files.createFile(path);
        } else {
            throw new RuntimeException();
        }
    }

    //--------------------------------------------------------
    // Path is your custom path
    // create temp file with this pattern: prefix + random string + suffix
    // like test_10415814082305280873.png
    //--------------------------------------------------------
    public static void createTempFile(String prefix, String suffix) throws IOException {
        Files.createTempFile(TEMP, prefix, suffix);
    }

    //--------------------------------------------------------
    // create temp file in default TEMP directory
    // if you dont provide prefix or suffix file is like 1235676.tmp
    //--------------------------------------------------------
    public static void createTempFileDefault(String prefix, String suffix) throws IOException {
        Files.createTempFile(prefix, suffix);
    }

    //--------------------------------------------------------
    // show file content type
    //--------------------------------------------------------
    public static void getContentType(Path path) throws IOException {
        String contentType = Files.probeContentType(path);
        System.out.println(contentType);
    }

    //--------------------------------------------------------
    // create directory in TEMP  with pattern prefix + random string
    // sample123456787
    //--------------------------------------------------------
    public static void createTempDir() throws IOException {
        Files.createTempDirectory("sample");
    }

    public static void createNewBufferRead(Path path) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            System.out.println(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createNewInputStream(Path path) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            inputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createNewChannel(Path path) {
        try(SeekableByteChannel seekableByteChannel = Files.newByteChannel(path, StandardOpenOption.READ)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            seekableByteChannel.read(byteBuffer);
            byteBuffer.flip();
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
            System.out.println(charBuffer.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


}

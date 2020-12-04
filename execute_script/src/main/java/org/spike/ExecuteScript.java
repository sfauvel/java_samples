package org.spike;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

/**
 * How to execute a shell command from Java.
 * http://www.enseignement.polytechnique.fr/informatique/Java/1.8/java/lang/ProcessBuilder.html
 */
public class ExecuteScript {
    public static void main(String... args) throws IOException, InterruptedException {
        final ExecuteScript ExecuteScript = new ExecuteScript();
        ExecuteScript.run("echo Hello world!!!");

    }

    public ExecuteScript run(String command) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase()
                .startsWith("windows");

        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", "dir");
        } else {
            builder.command("bash", "-c", command);
        }
        builder.redirectErrorStream(true);
        final File tempFile = File.createTempFile("log", ".txt");
        builder.redirectOutput(ProcessBuilder.Redirect.appendTo(tempFile));
        builder.directory(new java.io.File(System.getProperty("user.dir")));
        Process process = builder.start();

//        StreamGobbler streamGobbler =
//                new StreamGobbler(process.getInputStream(), System.out::println);
//        Executors.newSingleThreadExecutor().submit(streamGobbler);

        int exitCode = process.waitFor();
        assert exitCode == 0;

        final Stream<String> lines = Files.lines(tempFile.toPath());
        lines.forEach(System.out::println);
        return this;
    }


//    private static class StreamGobbler implements Runnable {
//        private InputStream inputStream;
//        private Consumer<String> consumer;
//
//        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
//            this.inputStream = inputStream;
//            this.consumer = consumer;
//        }
//
//        @Override
//        public void run() {
//            new BufferedReader(new InputStreamReader(inputStream)).lines()
//                    .forEach(consumer);
//        }
//    }

}

package org.spike.generator;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import org.spike.domain.Person;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class GenerateDoc {


    public static void main(String[] args) {
        GenerateDoc generateDoc = new GenerateDoc();
        generateDoc.generate();
    }

    private void generate() {
        JavaDocBuilder builder = new JavaDocBuilder();

        // Add a sourcefolder;
        String pathname = "src/main/java/org/spike/domain";
        builder.addSourceTree(new File(pathname));


        println("All classes in " + pathname + ":");
        JavaSource[] sources = builder.getSources();
        Arrays.stream(sources).forEach(s -> println("\t"+s.getClasses()[0].asType().getValue()));

        Optional<JavaSource> optionalSource = Arrays.stream(sources)
                .filter(s -> s.getURL().toString().endsWith("/" + Person.class.getSimpleName() + ".java"))
                .findFirst();


        Class<?> classToDocument = Person.class;
        println("Details of " + classToDocument + ":");
        JavaClass javaClass = getClassByClass(builder, classToDocument);

        println("\tName:" + javaClass.getName());
        println("\tComment: " + javaClass.getComment());
        println("\tFirst line of class:" + String.valueOf(javaClass.getLineNumber()));

        println("Methods of " + classToDocument + ":");
        Arrays.stream(javaClass.getMethods()).forEach(method -> {
                    println("\t" + method.getCallSignature() + ":" + method.getComment());
                }
        );

        println("Source of class " + classToDocument + ":");
        println("\t" + javaClass.getSource().toString().replaceAll("\n", "\n\t"));

    }

    private <T> JavaSource getSourceByClass(JavaDocBuilder builder, Class<T> clazz) {
        return getClassByClass(builder, clazz).getSource();
    }

    private <T> JavaClass getClassByClass(JavaDocBuilder builder, Class<T> clazz) {
        return builder.getClassByName(clazz.getName());
    }

    private void println(String text) {
        System.out.println(text);
    }
}

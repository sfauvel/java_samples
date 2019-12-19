package org.sfvl.replacecode;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.utils.SourceRoot;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicTransformationTest {

    final JavaParser javaParser = new JavaParser();
    private CompilationUnit cu;

    @BeforeEach
    public void init() {
        SourceRoot sourceRoot = getSourceRoot(Paths.get("src", "main", "java"));

        final Class<SimpleClass> aClass = SimpleClass.class;

        cu = sourceRoot.parse(
                aClass.getPackage().getName(),
                aClass.getSimpleName() + ".java");

    }

    private SourceRoot getSourceRoot(Path root) {
        try {
            return new SourceRoot(root);
        } catch(Exception e) {
            throw new RuntimeException("If there is an exception, is probably because program is not launch from project root path.\n" +
                    "Working space must be replace-code", e);
        }
    }

    @Test
    public void should_add_import_in_class() {

        cu.getImports().add(new ImportDeclaration("java.util.Map", false, false));

        final String code = cu.toString();
        assertTrue(code.contains("import java.util.Map;"), code);
    }


    @Test
    public void should_add_trace_on_all_methods() {

        cu.getPrimaryType().stream()
                .flatMap(type -> type.getMethods().stream())
                .flatMap(method -> method.getBody().stream())
                .forEach(body -> body.addStatement("System.out.println(\"???\");"))
        ;

        final String code = cu.toString();
        assertTrue(codeInOneLine(code).contains("doNothing() { System.out.println(\"???\");"), code);
    }

    static class BodyMethod {


        private final String methodName;
        private final BlockStmt body;

        public BodyMethod(String methodName, BlockStmt body) {
            this.methodName = methodName;
            this.body = body;
        }

        public String getMethodName() {
            return methodName;
        }

        public BlockStmt getBody() {
            return body;
        }
    }

    @Test
    public void should_add_trace_on_all_methods_with_method_name_using_tuple() {

        cu.getPrimaryType().stream()
                .flatMap(type -> type.getMethods().stream())
                .flatMap(method -> method.getBody().map(body -> Pair.with(method.getNameAsString(), body)).stream())
                .forEach(body -> body.getValue1().addStatement("System.out.println(\""+body.getValue0()+"\");"))
        ;

        final String code = cu.toString();
        assertTrue(codeInOneLine(code).contains("doNothing() { System.out.println(\"doNothing\");"), code);
    }

    @Test
    public void should_add_trace_on_all_methods_with_method_name_using_dto_class() {

        cu.getPrimaryType().stream()
                .flatMap(type -> type.getMethods().stream())
                .flatMap(method -> method.getBody().map(body -> new BodyMethod(method.getNameAsString(), body)).stream())
                .forEach(bodyMethod -> bodyMethod.getBody().addStatement("System.out.println(\""+bodyMethod.getMethodName()+"\");"))
        ;

        final String code = cu.toString();
        assertTrue(codeInOneLine(code).contains("doNothing() { System.out.println(\"doNothing\");"), code);
    }

    private String codeInOneLine(String code) {
        return code.replaceAll("\n", "").replaceAll(" +", " ");
    }
}

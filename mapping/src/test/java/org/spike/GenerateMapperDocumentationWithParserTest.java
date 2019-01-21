package org.spike;


import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.GenericVisitorWithDefaults;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.spike.mapper.Mapper;
import org.spike.mapper.MapperPredicate;
import org.spike.model.Person;
import org.spike.model.PersonDao;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.spike.mapper.Mapper.mapGeneric;
import static org.spike.mapper.MapperPredicate.NotNull;
import static org.spike.mapper.MapperPredicate.mapGeneric;

public class GenerateMapperDocumentationWithParserTest {

    @Test
    public void should_return_getter_not_matching_predicate() throws Exception {

        List<MapperPredicate> mappers = Arrays.asList(
                //mapGeneric(attribut, /***/with, /***/when),
                mapGeneric(Person::setName, /********/PersonDao::getNm, /***/NotNull),
                mapGeneric(Person::setFirstName, /***/PersonDao::getFn, /***/NotNull),
                mapGeneric(Person::setCity, /********/PersonDao::getCi, /***/NotNull),
                mapGeneric(Person::setAge, /*********/PersonDao::getA, /****/age -> age != -1)
        );

        PersonDao dao = new PersonDao("Moran", "Bob", null, -1);

        CompilationUnit compilationUnit = JavaParser.parse(Paths.get("src/test/java/org/spike/GenerateMapperDocumentationWithParserTest.java"));
        Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName("GenerateMapperDocumentationWithParserTest");

        assertTrue(classA.isPresent());

        GenericVisitorWithDefaults<String, Void> visitorGenericString = new GenericVisitorWithDefaults<String, Void>() {
            @Override
            public String defaultAction(Node n, Void arg) {
                if (n instanceof MethodReferenceExpr) {
                    MethodReferenceExpr ref = (MethodReferenceExpr)n;
                    return (ref.getScope() + "::" + ref.getIdentifier());
                } else if (n instanceof LambdaExpr) {
                    LambdaExpr ref = (LambdaExpr)n;
                    return ref.getParameters().stream().map(c -> c.toString()).collect(Collectors.joining(","))
                                    + " -> "
                                    + ref.getBody().toString();
                } else   if (n instanceof NameExpr) {
                    NameExpr ref = (NameExpr)n;
                    return ref.getName().toString();
                } else {
//                    n.getChildNodes().forEach(child -> child.accept(this, arg));
                }
                return "";
            }
        };

        VoidVisitorWithDefaults<List<String>> visitor = new VoidVisitorWithDefaults<List<String>>() {
            @Override
            public void defaultAction(Node n, List<String> arg) {
                if (n instanceof MethodCallExpr) {
                    if (((MethodCallExpr)n).getName().toString().equals("mapGeneric")) {
                        NodeList<Expression> arguments = ((MethodCallExpr)n).getArguments();
                        String argumentsText = arguments.stream().map(a -> a.accept(visitorGenericString, null) ).collect(Collectors.joining(","));
//                        System.out.println(argumentsText);
                        arg.add(argumentsText);
                    }
                }

                n.getChildNodes().forEach(child -> child.accept(this, arg));
            }
        };


        ArrayList<String> results = new ArrayList<>();
        classA.get().accept(visitor, results);

        assertTrue(results.stream()
                .map(r -> format(r))
//                .peek(c -> System.out.println(c))
                .collect(Collectors.toList())
                .containsAll(Arrays.asList(
                "PersonDao::getFn -> Person::setFirstName",
                "PersonDao::getNm -> Person::setName",
                "PersonDao::getCi -> Person::setCity",
                "PersonDao::getA -> Person::setAge"
        )));

    }


    public String format(String text) {
        String[] split = text.split(",");
        return split[1] + " -> " + split[0];
    }
}
package org.spike.multiinterface;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class BuilderTest {

    @Test
    public void build_article() {
        assertEquals("# My article\n", new ArticleBuilder().withTitle("My article").build());
    }

    @Test
    public void build_glossary_can_use_definition() {
        assertEquals("# MyGlossary\n- Keyword: My definition\n", new GlossaryBuilder()
                .withTitle("MyGlossary")
                .withDefinition("Keyword", "My definition")
                .build());
    }

    @Test
    public void build_article_could_not_add_definition() {
        final Class<ArticleBuilder> classToCheck = ArticleBuilder.class;
        final Set<String> methods = Arrays.stream(classToCheck.getMethods()).map(m -> m.getName()).collect(Collectors.toSet());
        assertTrue(methods.contains("withTitle"));
        assertFalse(methods.contains("withDefinition"));
    }

}

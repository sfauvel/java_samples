package org.spike.multiinterface;

public interface GlossaryInterface<T> extends ContentWriter<T> {

    /**
     * Method could not be on DocumentBuilder if we don't want make it available for every one.
     * @param keyword
     * @param definition
     * @return
     */
    default T withDefinition(String keyword, String definition) {
        return addContent(String.format("- %s: %s\n", keyword, definition));
    }
}

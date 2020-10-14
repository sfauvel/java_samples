package org.spike.multiinterface;

/**
 * The interface that allow specific interfaces to interact with global object.
 */
public interface ContentWriter<T> {
    T addContent(String format);
}

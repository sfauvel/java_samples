package org.spike.multiinterface;

public class DocumentBuilder<T> implements ContentWriter<T> {

    private final T myself;
    private StringBuffer content = new StringBuffer();

    public DocumentBuilder(Class<T> selfType) {
        this.myself = selfType.cast(this);
    }

    /**
     * All methods in this class will be available for subclasses.
     * @param title
     * @return
     */
    public T withTitle(String title) {
        addContent(String.format("# %s\n", title));
        return myself;
    }

    /**
     * This method is accessible by specific interface.
     * It's not completly safe because subclasses could use it and may be a problem
     * if too many methods are exposed.
     * It could be limited by having an interface returning an internal object with all methods.
     * Then, caller can easly see methods to use.
     * @param format
     * @return
     */
    @Override
    public T addContent(String format) {
        content.append(format);
        return myself;
    }

    public String build() {
        return content.toString();
    }

}

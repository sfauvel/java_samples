package org.spike.multiinterface;

public class GlossaryBuilder extends DocumentBuilder<GlossaryBuilder>
        implements GlossaryInterface<GlossaryBuilder>
{
    public GlossaryBuilder() {
        super(GlossaryBuilder.class);
    }

}

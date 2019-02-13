package org.samples;

import java.util.List;

public class FrontEnd {

    public static void main(String args[]) {
        BackEnd backEnd = new BackEnd();
        List<BackEnd.MappingTypeField> values = backEnd.getValues();
        values.stream().forEach(m -> {
            System.out.println("Type=" + m.type);
            System.out.println("Fields="+m.fields.toString());
        });
    }
}

package org.samples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BackEnd {

    class MappingTypeField {
        String type;
        List<String> fields;
    }

    // Datas with it's storage format.
    private String datas = "A=a,b,c;B=c,d,e";

    // If we want to change order into datas (invert association between key and value), front end should not have to change
    // If we change Frontend, there is a dependencies between 2 concepts: service and storage.
    // To keep independency, we have to change getValues implementation.
    private String datasRefacto = "a=A;b=A;c=A,B;d=B;e=B";

    public List<MappingTypeField> getValues() {
        List<MappingTypeField> mapping = new ArrayList<>();

        String[] lines = datas.split(";");
        for (String line : lines) {
            String[] keyValue = line.split("=");
            MappingTypeField mappingTypeField = new MappingTypeField();
            mappingTypeField.type = keyValue[0];
            mappingTypeField.fields = Arrays.asList(keyValue[1].split(","));
            mapping.add(mappingTypeField);
        }
        return mapping;
    }
}

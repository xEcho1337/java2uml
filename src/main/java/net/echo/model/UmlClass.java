package net.echo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UmlClass {
    
    private final String name;
    private final List<UmlMember> fields = new ArrayList<>();
    private final List<UmlMember> methods = new ArrayList<>();
    private final Set<String> usedTypes = new HashSet<>();

    public UmlClass(String name) {
        this.name = name;
    }

    public void addField(String name, String type, String visibility) {
        fields.add(new UmlMember(name, type, visibility, List.of()));
    }
    
    public void addMethod(String name, String ret, String visibility, List<UmlParameter> params) {
        methods.add(new UmlMember(name, ret, visibility, params));
    }
    
    public void uses(String type) {
        usedTypes.add(type);
    }
    
    public String getName() {
        return name;
    }
    
    public List<UmlMember> getFields() {
        return fields;
    }
    
    public List<UmlMember> getMethods() {
        return methods;
    }
    
    public Set<String> getUsedTypes() {
        return usedTypes;
    }
}

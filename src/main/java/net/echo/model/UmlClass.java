package net.echo.model;

import java.util.*;

public class UmlClass {

    private final String name;

    private final boolean isInterface;
    private final boolean isAbstract;
    private final boolean isFinal;
    private final boolean isEnum;
    private final boolean isRecord;

    private final List<UmlMember> fields = new ArrayList<>();
    private final List<UmlMember> methods = new ArrayList<>();
    private final Set<String> usedTypes = new HashSet<>();
    private final Set<String> interfaces = new HashSet<>();
    private String superClass;

    public UmlClass(String name,
                    boolean isInterface,
                    boolean isAbstract,
                    boolean isFinal,
                    boolean isEnum,
                    boolean isRecord) {
        this.name = name;
        this.isInterface = isInterface;
        this.isAbstract = isAbstract;
        this.isFinal = isFinal;
        this.isEnum = isEnum;
        this.isRecord = isRecord;
    }

    public void addField(String name, String type, String visibility, boolean isStatic) {
        fields.add(new UmlMember(name, type, visibility, isStatic, false, false, List.of()));
    }

    public void addMethod(String name,
                          String ret,
                          String visibility,
                          boolean isStatic,
                          boolean isAbstract,
                          List<UmlParameter> params) {
        methods.add(new UmlMember(name, ret, visibility, isStatic, isAbstract, false, params));
    }

    public void addConstructor(String name,
                               String visibility,
                               List<UmlParameter> params) {
        methods.add(new UmlMember(name, "", visibility, false, false, true, params));
    }

    public void addInterface(String iface) {
        interfaces.add(iface);
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

    public Set<String> getInterfaces() {
        return interfaces;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isEnum() {
        return isEnum;
    }

    public boolean isRecord() {
        return isRecord;
    }
}

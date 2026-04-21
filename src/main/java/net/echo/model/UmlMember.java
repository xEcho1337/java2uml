package net.echo.model;

import java.util.List;

public record UmlMember(
    String name,
    String type,
    String visibility,
    boolean isStatic,
    boolean isAbstract,
    boolean isConstructor,
    List<UmlParameter> parameters
) {}

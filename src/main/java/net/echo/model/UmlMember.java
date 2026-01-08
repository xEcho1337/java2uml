package net.echo.model;

import java.util.List;

public record UmlMember(String name, String type, String visibility, List<UmlParameter> parameters) {}

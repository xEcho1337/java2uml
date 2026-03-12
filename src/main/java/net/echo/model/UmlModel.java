package net.echo.model;

import java.util.*;

public class UmlModel {

    private final List<UmlClass> classes = new ArrayList<>();
    private final Map<String, Integer> indexByName = new HashMap<>();

    private final Map<Integer, Set<Integer>> dependencyGraph = new HashMap<>();
    private final Map<Integer, Set<Integer>> inheritanceGraph = new HashMap<>();
    private final Map<Integer, Set<Integer>> implementationGraph = new HashMap<>();

    public void addClass(UmlClass c) {
        indexByName.put(c.getName(), classes.size());
        classes.add(c);
    }

    public Integer indexOf(String className) {
        return indexByName.get(className);
    }

    public void addDependency(int fromIdx, int toIdx) {
        dependencyGraph
            .computeIfAbsent(fromIdx, k -> new HashSet<>())
            .add(toIdx);
    }

    public void addInheritance(int fromIdx, int toIdx) {
        inheritanceGraph
            .computeIfAbsent(fromIdx, k -> new HashSet<>())
            .add(toIdx);
    }

    public void addImplementation(int fromIdx, int toIdx) {
        implementationGraph
            .computeIfAbsent(fromIdx, k -> new HashSet<>())
            .add(toIdx);
    }

    public List<UmlClass> getClasses() {
        return classes;
    }

    public Map<Integer, Set<Integer>> getDependencyGraph() {
        return dependencyGraph;
    }

    public Map<Integer, Set<Integer>> getInheritanceGraph() {
        return inheritanceGraph;
    }

    public Map<Integer, Set<Integer>> getImplementationGraph() {
        return implementationGraph;
    }
}

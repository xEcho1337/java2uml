package net.echo.model;

import java.util.*;

public class UmlModel {
    
    private final List<UmlClass> classes = new ArrayList<>();
    private final Map<String, Integer> indexByName = new HashMap<>();
    private final Map<Integer, Set<Integer>> dependencyGraph = new HashMap<>();
    
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
    
    public List<UmlClass> getClasses() {
        return classes;
    }
    
    public Map<String, Integer> getIndexByName() {
        return indexByName;
    }
    
    public Map<Integer, Set<Integer>> getDependencyGraph() {
        return dependencyGraph;
    }
}

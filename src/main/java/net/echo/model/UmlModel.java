package net.echo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UmlModel {
    
    private final List<UmlClass> classes = new ArrayList<>();
    private final Map<String, Integer> indexByName = new HashMap<>();
    private final Map<Integer, Integer> dependencyGraph = new HashMap<>();
    
    public void addClass(UmlClass c) {
        indexByName.put(c.getName(), classes.size());
        classes.add(c);
    }
    
    public Integer indexOf(String className) {
        return indexByName.get(className);
    }
    
    public void addDependency(int fromIdx, int toIdx) {
        dependencyGraph.put(fromIdx, toIdx);
    }
    
    public List<UmlClass> getClasses() {
        return classes;
    }
    
    public Map<String, Integer> getIndexByName() {
        return indexByName;
    }
    
    public Map<Integer, Integer> getDependencyGraph() {
        return dependencyGraph;
    }
}

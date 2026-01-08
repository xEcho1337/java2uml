package net.echo.convert;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import net.echo.model.UmlClass;
import net.echo.model.UmlModel;
import net.echo.model.UmlParameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UmlExtractor {

    public UmlModel extract(Path srcDir) {
        UmlModel model = new UmlModel();

        try {
            Files.walk(srcDir)
                 .filter(p -> p.toString().endsWith(".java"))
                 .forEach(p -> parseFile(p, model));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        for (UmlClass from : model.getClasses()) {
            int fromIdx = model.indexOf(from.getName());
            
            for (String used : from.getUsedTypes()) {
                Integer toIdx = model.indexOf(used);
                
                if (toIdx != null && fromIdx != toIdx) {
                    model.addDependency(fromIdx, toIdx);
                }
            }
        }

        return model;
    }

    private void parseFile(Path path, UmlModel model) {
        try {
            CompilationUnit cu = StaticJavaParser.parse(path);

            cu.findAll(ClassOrInterfaceDeclaration.class)
              .forEach(c -> model.addClass(parseClass(c)));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UmlClass parseClass(ClassOrInterfaceDeclaration c) {
        UmlClass uc = new UmlClass(c.getNameAsString());

        c.getFields().forEach(f -> {
            AccessSpecifier visibility = f.getAccessSpecifier();
            String fieldType = f.getElementType().asString();
            
            uc.uses(stripGeneric(fieldType));
            
            f.getVariables().forEach(v -> {
                String name = v.getNameAsString();
                String type = v.getTypeAsString();
                uc.addField(name, type, visibility.name());
            });
        });

        c.getMethods().forEach(m -> {
            uc.uses(stripGeneric(m.getTypeAsString()));
            
            List<UmlParameter> params = new ArrayList<>();
            
            m.getParameters().forEach(p -> {
                String pName = p.getNameAsString();
                String pType = p.getTypeAsString();
                
                params.add(new UmlParameter(pName, pType));
                uc.uses(stripGeneric(pType));
            });
            
            String name = m.getNameAsString();
            String type = m.getTypeAsString();
            String visibility = m.getAccessSpecifier().name();
            
            uc.addMethod(name, type, visibility, params);
        });

        return uc;
    }
    
    private String stripGeneric(String t) {
        int i = t.indexOf('<');
        return i == -1 ? t : t.substring(0, i);
    }
}

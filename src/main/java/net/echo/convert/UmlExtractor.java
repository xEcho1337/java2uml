package net.echo.convert;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import net.echo.model.UmlClass;
import net.echo.model.UmlModel;
import net.echo.model.UmlParameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

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
            Integer fromIdx = model.indexOf(from.getName());
            if (fromIdx == null) continue;

            // INHERITANCE
            String superClass = from.getSuperClass();
            if (superClass != null) {
                Integer toIdx = model.indexOf(superClass);
                if (toIdx != null && !toIdx.equals(fromIdx)) {
                    model.addInheritance(fromIdx, toIdx);
                }
            }

            // IMPLEMENTATION
            for (String iface : from.getInterfaces()) {
                Integer toIdx = model.indexOf(iface);
                if (toIdx != null && !toIdx.equals(fromIdx)) {
                    model.addImplementation(fromIdx, toIdx);
                }
            }

            // DEPENDENCIES
            for (String used : from.getUsedTypes()) {
                Integer toIdx = model.indexOf(used);
                if (toIdx != null && !toIdx.equals(fromIdx)) {
                    model.addDependency(fromIdx, toIdx);
                }
            }
        }

        return model;
    }

    private void parseFile(Path path, UmlModel model) {
        try {
            ParserConfiguration config = new ParserConfiguration()
                .setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_21);

            JavaParser parser = new JavaParser(config);
            Optional<CompilationUnit> result = parser.parse(path).getResult();

            CompilationUnit unit = result.orElseThrow();
            unit.findAll(ClassOrInterfaceDeclaration.class)
              .forEach(c -> model.addClass(parseClass(c)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UmlClass parseClass(ClassOrInterfaceDeclaration c) {
        UmlClass uc = new UmlClass(
            c.getNameAsString(),
            c.isInterface(),
            c.isAbstract(),
            c.isFinal(),
            false,
            false
        );

        // EXTENDS
        c.getExtendedTypes().forEach(ext -> {
            String superName = stripGeneric(ext.getNameAsString());
            uc.setSuperClass(superName);
        });

        // IMPLEMENTS
        c.getImplementedTypes().forEach(impl -> {
            uc.addInterface(stripGeneric(impl.getNameAsString()));
        });

        // FIELDS
        c.getFields().forEach(f -> {
            f.getVariables().forEach(v -> {
                String name = v.getNameAsString();
                String type = v.getTypeAsString();
                String accessor = f.getAccessSpecifier().name();

                uc.addField(name, type, accessor, f.isStatic());
            });
        });

        // METHODS
        c.getMethods().forEach(m -> {
            List<UmlParameter> params = m.getParameters().stream()
                .map(p -> new UmlParameter(p.getNameAsString(), p.getTypeAsString()))
                .toList();

            String name = m.getNameAsString();
            String type = m.getTypeAsString();
            String accessor = m.getAccessSpecifier().name();

            uc.addMethod(name, type, accessor, m.isStatic(), m.isAbstract(), params);
        });

        return uc;
    }

    private String stripGeneric(String t) {
        int i = t.indexOf('<');
        return i == -1 ? t : t.substring(0, i);
    }
}

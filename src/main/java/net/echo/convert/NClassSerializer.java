package net.echo.convert;

import net.echo.CliConfig;
import net.echo.model.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NClassSerializer {

    private final UmlModel model;
    private final CliConfig config;
    
    public NClassSerializer(UmlModel model, CliConfig config) {
        this.model = model;
        this.config = config;
    }
    
    public void serialize() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element project = document.createElement("Project");
            document.appendChild(project);

            appendText(document, project, "Name", config.projectName());

            Element item = document.createElement("ProjectItem");
            String assembly = "NClass.DiagramEditor, Version=2.4.1823.0, Culture=neutral, PublicKeyToken=null";
            
            item.setAttribute("type", "NClass.DiagramEditor.ClassDiagram.Diagram");
            item.setAttribute("assembly", assembly);
            project.appendChild(item);

            appendText(document, item, "Name", config.diagram());
            appendText(document, item, "Language", "Java");

            Element entities = document.createElement("Entities");
            item.appendChild(entities);

            for (UmlClass c : model.getClasses()) {
                entities.appendChild(serializeClass(document, c));
            }

            Element relationships = document.createElement("Relationships");
            item.appendChild(relationships);

            model.getInheritanceGraph().forEach((from, targets) -> {
                for (int to : targets) {
                    Element relationship = document.createElement("Relationship");
                    relationship.setAttribute("type", "Generalization");
                    relationship.setAttribute("first", String.valueOf(from));
                    relationship.setAttribute("second", String.valueOf(to));
                    relationships.appendChild(relationship);
                }
            });

            model.getImplementationGraph().forEach((from, targets) -> {
                for (int to : targets) {
                    Element relationship = document.createElement("Relationship");
                    relationship.setAttribute("type", "Realization");
                    relationship.setAttribute("first", String.valueOf(from));
                    relationship.setAttribute("second", String.valueOf(to));
                    relationships.appendChild(relationship);
                }
            });

            model.getDependencyGraph().forEach((from, targets) -> {
                for (int to : targets) {
                    Element relationship = document.createElement("Relationship");
                    relationship.setAttribute("type", "Dependency");
                    relationship.setAttribute("first", String.valueOf(from));
                    relationship.setAttribute("second", String.valueOf(to));
                    
                    relationship.appendChild(document.createElement("Label"));
                    
                    Element startOrientation = document.createElement("StartOrientation");
                    Element endOrientation = document.createElement("EndOrientation");
                    
                    startOrientation.setTextContent("Horizontal");
                    endOrientation.setTextContent("Horizontal");
                    
                    relationship.appendChild(startOrientation);
                    relationship.appendChild(endOrientation);
                    
                    relationships.appendChild(relationship);
                }
            });
            
            write(document, config.output());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Element serializeClass(Document doc, UmlClass c) {
        Element entity = doc.createElement("Entity");

        if (c.isInterface()) {
            entity.setAttribute("type", "Interface");
        } else if (c.isEnum()) {
            entity.setAttribute("type", "Enum");
        } else {
            entity.setAttribute("type", "Class");
        }

        appendText(doc, entity, "Name", c.getName());
        appendText(doc, entity, "Access", "Public");

        // Modifier class
        if (c.isAbstract()) {
            appendText(doc, entity, "Modifier", "Abstract");
        } else if (c.isFinal()) {
            appendText(doc, entity, "Modifier", "Sealed");
        } else {
            appendText(doc, entity, "Modifier", "None");
        }

        for (UmlMember field : c.getFields()) {
            Element m = doc.createElement("Member");
            m.setAttribute("type", "Field");
            m.setTextContent(formatField(field));
            entity.appendChild(m);
        }

        for (UmlMember method : c.getMethods()) {
            Element m = doc.createElement("Member");
            m.setAttribute("type", "Method");
            m.setTextContent(formatMethod(method));
            entity.appendChild(m);
        }

        return entity;
    }

    private static String formatField(UmlMember f) {
        String prefix = f.visibility().toLowerCase();

        if (f.isStatic()) {
            prefix += " static";
        }

        return String.format("%s %s %s", prefix, f.type(), f.name());
    }

    private static String formatMethod(UmlMember m) {
        String params = m.parameters().stream()
            .map(p -> p.type() + " " + p.name())
            .collect(Collectors.joining(", "));

        String prefix = m.visibility().toLowerCase();

        if (m.isStatic()) prefix += " static";
        if (m.isAbstract()) prefix += " abstract";

        return String.format("%s %s %s(%s)",
            prefix,
            m.type(),
            m.name(),
            params
        );
    }

    
    private static void appendText(Document doc, Element parent, String tag, String text) {
        Element e = doc.createElement(tag);
        e.setTextContent(text);
        parent.appendChild(e);
    }

    private static void write(Document doc, Path out) throws Exception {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        t.transform(new DOMSource(doc), new StreamResult(out.toFile()));
    }
}

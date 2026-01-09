package net.echo;

import net.echo.convert.NClassSerializer;
import net.echo.convert.UmlExtractor;
import net.echo.model.UmlModel;

public class Main {
    public static void main(String[] args) {
        try {
            CliConfig config = CliConfig.parse(args);

            UmlModel model = new UmlExtractor().extract(config.srcDir());
            NClassSerializer serializer = new NClassSerializer(model, config);

            serializer.serialize();
            System.out.println("Generated UML file to " + config.output());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
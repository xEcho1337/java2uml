package net.echo;

import java.nio.file.Path;
import java.nio.file.Paths;

public record CliConfig(String projectName, String diagram, Path srcDir, Path output) {
    
    public static CliConfig parse(String[] args) {
        String projectName = null;
        String diagram = null;
        Path srcDir = null;
        Path output = null;
        
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--project" -> projectName = args[++i];
                case "--diagram" -> diagram = args[++i];
                case "--src" -> srcDir = Paths.get(args[++i]);
                case "--out" -> output = Paths.get(args[++i]);
            }
        }
        
        if (projectName == null || srcDir == null || output == null) {
            String message = """
                Usage is: java2uml [options]
                Runtime:
                    --project <name>       Specifies the output program name
                    --diagram <name>       Specifies the output diagram name
                    --src <dir_path>       Specifies the directory to scrape the files from
                    --out <name>           Specifies the name of the output file

                Example: java2uml --project school --diagram library --src . --out uml.ncp
                """;
            throw new IllegalArgumentException(message);
        }
        
        return new CliConfig(projectName, diagram, srcDir, output);
    }
}

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
                Missing arguments! Options:
                --project <NAME> Specifies the output program name
                --diagram <NAME> Specifies the output diagram name
                --src <DIR> Specifies the directory to scrape the files from
                --out <NAME> Specifies the name of the output file
                """;
            throw new IllegalArgumentException(message);
        }
        
        return new CliConfig(projectName, diagram, srcDir, output);
    }
}

package cn.y.generator.file;

import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class FileGenerator {

    public static void doGenerate(Object model) throws IOException, TemplateException {
        String projectPath = System.getProperty("user.dir");

        File parentFile = new File(projectPath);
        String inputFilePath = new File(parentFile, "y-generator-demo-project/acm-template").getAbsolutePath();
//        System.out.println(inputFilePath);
        String outputFilePath = projectPath;
//        System.out.println(outputFilePath);

        StaticFileGenertor.copyFilesByHutools(inputFilePath, outputFilePath);

        String inputDynamicFilePath = projectPath + File.separator + "y-codegenerator-maker/src/main/resources/template/";
        String outputDynamicFilePath = outputFilePath + File.separator + "acm-template/src/cn/y/acm/MainTemplate.java";

        DynamicFileGenerator.doGenerate(inputDynamicFilePath, outputDynamicFilePath, model);
    }

}

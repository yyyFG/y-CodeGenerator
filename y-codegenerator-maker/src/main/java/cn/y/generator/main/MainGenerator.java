package cn.y.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import cn.y.generator.file.DynamicFileGenerator;
import cn.y.generator.file.JarGenerator;
import cn.y.generator.file.ScriptGenerator;
import cn.y.meta.Meta;
import cn.y.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class MainGenerator {

    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        Meta meta = MetaManager.getMetaObject();
        System.out.println(meta);

        // 输出路径
        String projectPath = System.getProperty("user.dir");
        System.out.println("projectPath: " + projectPath);
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
        System.out.println("outputPath: " + outputPath);

        if(!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }

        // 复制原始文件
        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        String sourceCopyDestPath = outputPath + File.separator + ".source";
        FileUtil.copy(sourceRootPath, sourceCopyDestPath, false);

        // 读取 resources 目录
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourcePath = classPathResource.getAbsolutePath();
        System.out.println("inputResourcePath: " + inputResourcePath);

        // Java 包的基础路径
        String outputBasePackage = meta.getBasePackage();
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBasePackage, "."));
        System.out.println("outputBasePackagePath: " + outputBasePackagePath);
        String outputBaseJavaPackagePath = outputPath + File.separator + "src/main/java/" + outputBasePackagePath;
        System.out.println("outputBaseJavaPackagePath: " + outputBaseJavaPackagePath);

        String inputFilePath;
        String outputFilePath;

        // model.DataModel
        inputFilePath = inputResourcePath + "template/java/model/DataModel.java.ftl";
        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/model/DataModel.java";
        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // ConfigCommand.java.ftl
        inputFilePath = inputResourcePath + "template/java/cli/command/ConfigCommand.java.ftl";
        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ConfigCommand.java";
        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // GenerateCommand.java.ftl
        inputFilePath = inputResourcePath + "template/java/cli/command/GenerateCommand.java.ftl";
        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/GenerateCommand.java";
        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // ListCommand.java.ftl
        inputFilePath = inputResourcePath + "template/java/cli/command/ListCommand.java.ftl";
        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ListCommand.java";
        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // CommandExecutor.java.ftl
        inputFilePath = inputResourcePath + "template/java/cli/CommandExecutor.java.ftl";
        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/cli/CommandExecutor.java";
        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // Main.java.ftl
        inputFilePath = inputResourcePath + "template/java/Main.java.ftl";
        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/Main.java";
        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // MainGenerator.java.ftl
        inputFilePath = inputResourcePath + "template/java/generator/MainGenerator.java.ftl";
        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/generator/main/MainGenerator.java";
        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // DynamicGenerator.java.ftl
        inputFilePath = inputResourcePath + "template/java/generator/DynamicGenerator.java.ftl";
        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/generator/file/DynamicGenerator.java";
        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // StaticGenerator.java.ftl
        inputFilePath = inputResourcePath + "template/java/generator/StaticGenerator.java.ftl";
        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/generator/file/StaticGenerator.java";
        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // pom.xml.ftl
        inputFilePath = inputResourcePath + File.separator + "template/pom.xml.ftl";
        System.out.println("inputFilePath: " + inputFilePath);
        outputFilePath = outputPath + File.separator + "/pom.xml";
        System.out.println("outputFilePath: " + outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // README.md.ftl
        inputFilePath = inputResourcePath + File.separator + "template/README.md.ftl";
        System.out.println("inputFilePath: " + inputFilePath);
        outputFilePath = outputPath + File.separator + "/README.md";
        System.out.println("outputFilePath: " + outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // 打包 jar 包
        JarGenerator.doGenerate(outputPath);

        String shellOutputFilePath = outputPath + File.separator + "generator";
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + jarName;
        ScriptGenerator.doGenerate(shellOutputFilePath, jarPath);
    }
}

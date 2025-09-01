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

public abstract class GenerateTemplate {

    public void doGenerate() throws TemplateException, IOException, InterruptedException {
        Meta meta = MetaManager.getMetaObject();

        // 0. 输出路径
        String projectPath = System.getProperty("user.dir");
//        System.out.println("projectPath: " + projectPath);
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
//        System.out.println("outputPath: " + outputPath);
        if(!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }

        // 1. 拷贝源文件
        String sourceCopyDestPath = copySource(meta, outputPath);

        // 2. 生成代码
        generateCode(meta, outputPath);

        // 3. 生成 jar 包
        String jarPath = buildJar(outputPath, meta);

        // 4. 封装脚本
        String shellOutputFilePath = buildScript(outputPath, jarPath);

        // 5. 生成精简版的程序（产物包）
        buildDist(outputPath, sourceCopyDestPath, jarPath, shellOutputFilePath);
    }


    /**
     * 生成精简版程序
     * @param outputPath
     * @param sourceCopyDestPath
     * @param jarPath
     * @param shellOutputFilePath
     */
    protected void buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath){
        String distOutputPath = outputPath + File.separator + "-dist";
        // 拷贝 jar 包
        String targetAbsolutePath = distOutputPath + File.separator + "target";
        FileUtil.mkdir(targetAbsolutePath);
        String jarAbsolutePath = targetAbsolutePath + File.separator + jarPath;
        FileUtil.copy(jarAbsolutePath, targetAbsolutePath, true);
        // 拷贝脚本文件
        FileUtil.copy(shellOutputFilePath, distOutputPath, true);
        // 拷贝源模板文件
        FileUtil.copy(sourceCopyDestPath, distOutputPath, true);
    }

    /**
     * 封装脚本
     * @param outputPath
     * @param jarPath
     * @return
     */
    protected String buildScript(String outputPath, String jarPath) throws IOException{
        String shellOutputFilePath = outputPath + File.separator + "generator";
        ScriptGenerator.doGenerate(shellOutputFilePath, jarPath);
        return shellOutputFilePath;
    }

    /**
     * 构建 jar 包
     * @param outputPath
     * @param meta
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    protected static String buildJar(String outputPath, Meta meta) throws IOException, InterruptedException {
        // 打包 jar 包
        JarGenerator.doGenerate(outputPath);
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + jarName;
        return jarPath;
    }

    /**
     * 生成代码
     * @param meta
     * @param outputPath
     * @throws IOException
     * @throws TemplateException
     */
    protected static void generateCode(Meta meta, String outputPath) throws IOException, TemplateException {
        // 读取 resources 目录
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourcePath = classPathResource.getAbsolutePath();
//        System.out.println("inputResourcePath: " + inputResourcePath);

        // Java 包的基础路径
        String outputBasePackage = meta.getBasePackage();
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBasePackage, "."));
//        System.out.println("outputBasePackagePath: " + outputBasePackagePath);
        String outputBaseJavaPackagePath = outputPath + File.separator + "src/main/java/" + outputBasePackagePath;
//        System.out.println("outputBaseJavaPackagePath: " + outputBaseJavaPackagePath);

        String inputFilePath;
        String outputFilePath;

        // model.DataModel
        inputFilePath = inputResourcePath + File.separator + "template/java/model/DataModel.java.ftl";
//        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/model/DataModel.java";
//        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // ConfigCommand.java.ftl
        inputFilePath = inputResourcePath + File.separator + "template/java/cli/command/ConfigCommand.java.ftl";
//        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ConfigCommand.java";
//        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // GenerateCommand.java.ftl
        inputFilePath = inputResourcePath + File.separator + "template/java/cli/command/GenerateCommand.java.ftl";
//        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/GenerateCommand.java";
//        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // ListCommand.java.ftl
        inputFilePath = inputResourcePath + File.separator + "template/java/cli/command/ListCommand.java.ftl";
//        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ListCommand.java";
//        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // CommandExecutor.java.ftl
        inputFilePath = inputResourcePath + File.separator + "template/java/cli/CommandExecutor.java.ftl";
//        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/cli/CommandExecutor.java";
//        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // Main.java.ftl
        inputFilePath = inputResourcePath + File.separator + "template/java/Main.java.ftl";
//        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/Main.java";
//        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // MainGenerator.java.ftl
        inputFilePath = inputResourcePath + File.separator + "template/java/generator/main/MainGenerator.java.ftl";
//        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/generator/main/MainGenerator.java";
//        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // DynamicGenerator.java.ftl
        inputFilePath = inputResourcePath + File.separator + "template/java/generator/file/DynamicGenerator.java.ftl";
//        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/generator/file/DynamicGenerator.java";
//        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // StaticGenerator.java.ftl
        inputFilePath = inputResourcePath + File.separator + "template/java/generator/file/StaticGenerator.java.ftl";
//        System.out.println(inputFilePath);
        outputFilePath = outputBaseJavaPackagePath + "/generator/file/StaticGenerator.java";
//        System.out.println(outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // pom.xml.ftl
        inputFilePath = inputResourcePath + File.separator + "template/pom.xml.ftl";
//        System.out.println("inputFilePath: " + inputFilePath);
        outputFilePath = outputPath + File.separator + "/pom.xml";
//        System.out.println("outputFilePath: " + outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);

        // README.md.ftl
        inputFilePath = inputResourcePath + File.separator + "template/README.md.ftl";
//        System.out.println("inputFilePath: " + inputFilePath);
        outputFilePath = outputPath + File.separator + "/README.md";
//        System.out.println("outputFilePath: " + outputFilePath);
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath, meta);
    }

    /**
     * 复制原始文件
     * @param meta
     * @param outputPath
     * @return
     */
    protected static String copySource(Meta meta, String outputPath) {
        // 复制原始文件
        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        String sourceCopyDestPath = outputPath + File.separator + ".source";
        FileUtil.copy(sourceRootPath, sourceCopyDestPath, false);

        return sourceCopyDestPath;
    }
}

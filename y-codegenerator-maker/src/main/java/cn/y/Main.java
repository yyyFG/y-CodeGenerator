package cn.y;

import cn.y.generator.main.GenerateTemplate;
import cn.y.generator.main.MainGenerator;
import cn.y.generator.main.ZipGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
//        // 获取整个项目的根路径
//        String projectPath = System.getProperty("user.dir");
//        System.out.println(projectPath);
//        File parentFile = new File(projectPath);
//        // 输入路径：ACM 示例代码模板目录
//        String inputPath = new File(parentFile, "y-generator-demo-project/acm-template").getAbsolutePath();
//        System.out.println(inputPath);
//        // 输出路径：直接输出到项目的根目录
//        String outputPath = projectPath;
//        copyFilesByHutools(inputPath, outputPath);
//    }

//        args = new String[]{"generate", "-l", "-a", "-o"};
//        args = new String[]{"config"};
//        args = new String[]{"list"};

//        CommandExecutor commandExecutor = new CommandExecutor();
//        commandExecutor.doExecute(args);

//        MainGenerator mainGenerator = new MainGenerator();
        GenerateTemplate generateTemplate = new ZipGenerator();
        generateTemplate.doGenerate();
    }
}
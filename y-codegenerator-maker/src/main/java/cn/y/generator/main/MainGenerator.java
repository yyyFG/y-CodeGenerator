package cn.y.generator.main;

import cn.y.generator.main.GenerateTemplate;

/**
 * 生成代码生成器
 */
public class MainGenerator extends GenerateTemplate {

//    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
//        Meta meta = MetaManager.getMetaObject();
//        System.out.println(meta);
//
//    }

    @Override
    protected void buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath){
        System.out.println("不要给我输出 dist 啦！");
    }
}

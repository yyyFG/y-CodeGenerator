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

public class MainGenerator extends GenerateTemplate {

    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        Meta meta = MetaManager.getMetaObject();
        System.out.println(meta);


    }
}

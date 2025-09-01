package cn.y.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.y.meta.Meta;
import cn.y.meta.enums.FileGenerateTypeEnum;
import cn.y.meta.enums.FileTypeEnum;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TemplateMaker {

    /**
     * 制作模板
     * @param newMeta
     * @param originalProjectPath
     * @param inputFilePath
     * @param modelInfo
     * @param searchStr
     * @param id
     * @return
     */
    public static long makeTemplate(Meta newMeta, String originalProjectPath,
                                    String inputFilePath, Meta.ModelConfig.ModelInfo modelInfo,
                                    String searchStr, Long id){

        //
        if (id == null){
            id = IdUtil.getSnowflakeNextId();
        }

        //
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;

        // 是否
        //
        if (!FileUtil.exist(templatePath)){
            FileUtil.mkdir(templatePath);
            FileUtil.copy(originalProjectPath, templatePath, true);
        }

        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originalProjectPath));
        // win
        sourceRootPath = sourceRootPath.replace("\\", "/");

        // 2. 输入文件信息
        String fileInputPath = inputFilePath;
        String fileOutputPath = fileInputPath + ".ftl";


        // 二、 使用字符串替换，生成模板文件
        String fileInputAbsolutePath = sourceRootPath + File.separator + fileInputPath;
        // 输出模板文件
        String fileOutputAbsolutePath = sourceRootPath + File.separator + fileOutputPath;

        String fileCount = null;
        if (FileUtil.exist(fileOutputAbsolutePath)){
            fileCount = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileCount = FileUtil.readUtf8String(fileInputAbsolutePath);
        }

        String replacement = String.format("${%s}", modelInfo.getFieldName());
        String newFileCount = StrUtil.replace(fileCount, searchStr, replacement);

        // 输出模板文件
        FileUtil.writeUtf8String(newFileCount, fileOutputAbsolutePath);

        //
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());

        // 三、 生成配置文件
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";

        //
        if (FileUtil.exist(metaOutputPath)){
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            BeanUtil.copyProperties(newMeta, oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;

            // 1.
            List<Meta.FileConfig.FileInfo> fileInfoList = oldMeta.getFileConfig().getFiles();
            fileInfoList.add(fileInfo);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = oldMeta.getModelConfig().getModels();
            modelInfoList.add(modelInfo);

            //
            newMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
            newMeta.getModelConfig().setModels(distinctModels(modelInfoList));

        }else {
            //
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            newMeta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);
            fileInfoList.add(fileInfo);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.add(modelInfo);
        }

        // 输出元信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonStr(newMeta), metaOutputPath);
        return id;

    }


    /**
     * 模型去重
     * @param modelInfoList
     * @return
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelInfoList){
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(modelInfoList.stream()
                .collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)).values());
        return newModelInfoList;
    }


    /**
     * 文件去重
     * @param fileInfoList
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList){
        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(fileInfoList.stream()
                .collect(Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r)).values());
        return newFileInfoList;
    }

    public static void main(String[] args) {

        // 一、输入信息
        // 1. 输入项目信息
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM 示范模板生成器");

        String projectPath = System.getProperty("user.dir");
        String originalProjectPath = new File(projectPath).getParent() + File.separator + "acm-template";
        String inputFilePath = "src/cn/y/acm/MainTemplate.java";


        // 3. 输入模型参数信息
//        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
//        modelInfo.setFieldName("outputText");
//        modelInfo.setType("String");
//        modelInfo.setDefaultValue("sum = ");

        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("String");
        modelInfo.setType("String");

//        String searchStr = "Sum: ";

        String searchStr = "MainTemplate";

        long id =makeTemplate(meta, originalProjectPath, inputFilePath, modelInfo, searchStr, 1962553210487308288L);

        System.out.println(id);

    }
}

package cn.y.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.y.meta.enums.FileFilterRangeEnum;
import cn.y.meta.enums.FileFilterRuleEnum;
import cn.y.template.models.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件过滤
 */
public class FileFilter {


    /**
     * 单个文件过滤
     *
     * @param fileFilterConfigList 过滤规则
     * @param file 单个文件
     * @return 是否保留
     */
    public static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigList, File file){
        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);

        // 所有过滤器校验结束的结果
        boolean result = true;

        if (CollUtil.isEmpty(fileFilterConfigList)){
            return true;
        }

        for (FileFilterConfig fileFilterConfig : fileFilterConfigList){
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();

            FileFilterRangeEnum fileFilterRangeEnum = FileFilterRangeEnum.getEnumByValue(range);
            if (fileFilterRangeEnum == null){
                continue;
            }

            // 要过滤的内容
            String content = fileName;
            switch (fileFilterRangeEnum){
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:
            }

            FileFilterRuleEnum fileFilterRuleEnum = FileFilterRuleEnum.getEnumByValue(rule);
            if (fileFilterRuleEnum == null){
                continue;
            }

            switch (fileFilterRuleEnum){
                case EQUALS:
                    result = content.equals(value);
                    break;
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case ENDS_WITH:
                    result = content.endsWith(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                default:
            }

            // 有一个不满足，就直接返回
            if (!result){
                return false;
            }

        }
        // 都满足
        return true;
    }


    /**
     * 对某个文件或目录进行过滤，返回文件列表
     *
     * @param filePath 文件路径
     * @param fileFilterConfigList 过滤规则
     * @return 过滤后的文件列表
     */
    public static List<File> doFilter(String filePath, List<FileFilterConfig> fileFilterConfigList){
        //
        List<File> fileList = FileUtil.loopFiles(filePath);
        return fileList.stream()
                .filter(file -> doSingleFileFilter(fileFilterConfigList, file))
                .collect(Collectors.toList());
    }
}

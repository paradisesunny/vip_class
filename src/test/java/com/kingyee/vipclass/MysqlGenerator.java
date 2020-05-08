package com.kingyee.vipclass;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MysqlGenerator {
    /**
     * RUN THIS
     */
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setOpen(false);
        gc.setControllerName(null);
        gc.setAuthor(System.getProperty("user.name"));
        // gc.setMapperName("%sDao");
        //覆盖文件,只覆盖entity
        gc.setFileOverride(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.118.3:3308/prad?useUnicode=true&serverTimezone=GMT&useSSL=false" +
                "&characterEncoding=utf8");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("pro");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.kingyee.prad");
        // pc.setService("service");
        // pc.setModuleName();
        // pc.setParent("com.baomidou.mybatisplus.samples.generator");

        //        pc.setEntity("db");
        //        pc.setMapper("dao");

        mpg.setPackageInfo(pc);

        //自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };
        //覆盖entry
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建,这里调用默认的方法
                checkDir(filePath);
                //对于已存在的文件，只需重复生成 entity 和 mapper.xml
                File file = new File(filePath);
                boolean exist = file.exists();
                if (exist) {
                    if (FileType.ENTITY == fileType) {
                        return true;
                    } else {
                        return false;
                    }
                }
                //不存在的文件都需要创建
                return true;
            }
        });

        // 自定义配置-生成xml到resources/mapper
        boolean bCreateXmlMapper = false;
        if (bCreateXmlMapper) {
            List<FileOutConfig> focList = new ArrayList<>();
            focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输入文件名称
                    return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                }
            });
            cfg.setFileOutConfigList(focList);
        }
        mpg.setCfg(cfg);

        // 不在java src中使用xml，不生成controller
        mpg.setTemplate(new TemplateConfig().setXml(null).setController(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setExclude("databasechangelog", "databasechangeloglock");
        strategy.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");
        strategy.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
        strategy.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // strategy.setSuperEntityClass("com.baomidou.mybatisplus.samples.generator.common.BaseEntity");
        // strategy.setEntityLombokModel(true);
        // strategy.setSuperControllerClass("com.baomidou.mybatisplus.samples.generator.common.BaseController");
        // strategy.setInclude(scanner("表名"));
        // strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        // strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        // mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}
package site.pyyf.compiler.dao;


import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SiteSettingMapper {
    int getSiteSetting(String description);

}

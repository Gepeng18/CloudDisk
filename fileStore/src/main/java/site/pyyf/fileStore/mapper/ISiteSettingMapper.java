package site.pyyf.fileStore.mapper;


import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ISiteSettingMapper {
    int getSiteSetting(String description);

}

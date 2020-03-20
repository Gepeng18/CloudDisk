package site.pyyf.fileStore.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pyyf.fileStore.mapper.ISiteSettingMapper;
import site.pyyf.fileStore.service.ISiteSettingService;


@Service
public class SiteSettingServiceImpl implements ISiteSettingService {

    @Autowired
    public ISiteSettingMapper siteSettingMapper;

    public int allowOnlineExecutor(){
        return siteSettingMapper.getSiteSetting("允许在线编译");
    }

}

package site.pyyf.compiler.service.Impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pyyf.compiler.dao.SiteSettingMapper;
import site.pyyf.compiler.service.ISiteSettingService;

@Service
public class SiteSettingService implements ISiteSettingService {
    public static void main(String[] args) {
        System.out.println("我爱你");
    }
    @Autowired
    public SiteSettingMapper siteSettingMapper;

    public int allowOnlineExecutor(){
        return siteSettingMapper.getSiteSetting("允许在线编译");
    }

}

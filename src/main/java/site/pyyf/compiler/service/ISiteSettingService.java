package site.pyyf.compiler.service;

import org.springframework.beans.factory.annotation.Autowired;
import site.pyyf.compiler.dao.SiteSettingMapper;

public interface ISiteSettingService {


    public int allowRegister();

    public int allowKaptchaLogin();
    public int allowOnlineExecutor();

}

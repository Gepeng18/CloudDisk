package site.pyyf.fileStore.event;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import site.pyyf.fileStore.controller.BaseController;
import site.pyyf.fileStore.entity.Event;
import site.pyyf.fileStore.entity.MyFile;
import site.pyyf.fileStore.utils.CloudDiskConstant;

@Component
public class EventConsumer extends BaseController implements CloudDiskConstant {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteFilesOrFolders(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        int entityType= event.getEntityType();
        if(entityType==0){
            //删除文件
            MyFile deletingFile = JSONObject.parseObject(event.getEntityInfo(),MyFile.class);
            iFileStoreService.deleteFile(deletingFile);
            logger.info("我kafka把文件 "+deletingFile.getMyFileName()+" 删除啦");

            if (StringUtils.substringAfterLast(deletingFile.getMyFileName(), ".").equals("md")) {
                logger.info("我kafka把markdown文件 "+deletingFile.getMyFileName()+" 的content删除啦");
                iLibraryService.deleteByBookId(deletingFile.getMyFileId());
                iEbookContentService.deleteByBookId(deletingFile.getMyFileId());
            }
        }



    }


}

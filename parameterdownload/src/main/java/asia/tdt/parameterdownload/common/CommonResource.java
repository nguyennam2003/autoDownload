package asia.tdt.parameterdownload.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CommonResource {
    @Value("${web.url}")
    private String webUrl;

    @Value("${schedule.time}")
    private String cronJobTime;

    @Value("${folderPath.currentFile}")
    private String pathCurrentFile;

    @Value("${folderPath.fileBackup}")
    private String pathFileBackup;
}

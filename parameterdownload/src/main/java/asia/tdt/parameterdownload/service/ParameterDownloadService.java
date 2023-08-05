package asia.tdt.parameterdownload.service;

import asia.tdt.parameterdownload.common.CommonResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static asia.tdt.parameterdownload.common.Constants.*;


@Component
@RequiredArgsConstructor
@Slf4j
public class ParameterDownloadService {
    private final CommonResource commonResource;

    @Scheduled(cron = "${schedule.time}")
    @Scheduled(fixedRate = 5000)
    public void autoDownload() throws IOException {
        log.info("Start download file");
        URL url = new URL(commonResource.getWebUrl());

        URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String fileName = getFileName();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {

            if (inputLine.contains(fileName)) {
                String fullPath = url.getProtocol() + "://" + url.getHost() + url.getPath() + fileName;
                downloadFile(fullPath, fileName);
            }
        }
        in.close();
        log.info("Finish download file");
    }

    private String getFileName() {
        String fileName = START_WITH
                .concat(getDateFormatInFileName())
                .concat("_")
                .concat(getHourFormatInFileName())
                .concat(END_WITH);
        return fileName;
    }

    private String getDateFormatInFileName() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_IN_FILE_NAME);
        return currentTime.format(formatter);
    }

    private String getHourFormatInFileName() {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.getHour() + TIME_MINUTE_IN_FILE_NAME;
    }

    private void downloadFile(String path, String fileName) throws IOException {
        URL downloadUrl = new URL(path);
        InputStream in = downloadUrl.openStream();

        // move file to back up
        boolean isSuccess = backupFile();
        if (!isSuccess) {
            log.warn("Failed when downloading file when backing up file error!");
            return;
        }

        File outPutFolder = new File(commonResource.getPathCurrentFile());
        if (!outPutFolder.exists())
            outPutFolder.mkdirs();

        // download file
        OutputStream fileLocation = Files.newOutputStream(Paths.get(outPutFolder.getPath() + "/" + fileName));

        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            fileLocation.write(buffer, 0, length);
        }
        log.info("Download file success with fileName = {}", fileName);

        in.close();
        fileLocation.close();
    }

    private boolean backupFile() {
        File pathCurrentFile = new File(commonResource.getPathCurrentFile());
        File pathFileBackup = new File(commonResource.getPathFileBackup());

        if (!pathFileBackup.exists())
            pathFileBackup.mkdirs();

        File[] files = pathCurrentFile.listFiles();
        if (Objects.isNull(files)) return true;

        boolean isSuccess = true;
        for (File file : files) {
            try {
                String fileName = file.getName();
                // check nếu là file ngày hôm nay thì không move
                if (fileName.contains(getDateFormatInFileName()))
                    continue;
                File destFile = new File(pathFileBackup, file.getName());
                file.renameTo(destFile);

            } catch (Exception e) {
                log.error("Failed when move file with fileName = {}", file.getName(), e);
                isSuccess = false;
            }
        }
        log.info("Move file to backup success.");
        return isSuccess;
    }
}

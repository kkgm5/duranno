package hi.duranno.web.archive;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileStore2 {
    @Value("${file.dir2}")
    private String fileDir;

    public String getFullPath(String filename, String ordinal) {
        return fileDir + ordinal + "th/" + filename;
    }

    public String storeFile(MultipartFile multipartFile, String ordinal) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFileName = multipartFile.getOriginalFilename();
        multipartFile.transferTo(new File(getFullPath(originalFileName, ordinal)));

        return originalFileName;
    }

    public void deleteFile(String fileName, String ordinal) {
        File file = new File(getFullPath(fileName, ordinal));

        if (file.exists()) {
            file.delete();
        }
    }
}

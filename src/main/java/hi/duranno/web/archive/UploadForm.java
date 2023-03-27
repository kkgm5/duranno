package hi.duranno.web.archive;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
public class UploadForm {
    @NotEmpty
    private String title;
    private MultipartFile attachFile;
}

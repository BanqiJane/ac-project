package xyz.acproject.utils.http.data;

import lombok.Builder;
import lombok.Data;

/**
 * @author Jane
 * @ClassName FormFileData
 * @Description TODO
 * @date 2022/7/1 18:02
 * @Copyright:2022
 */
@Data
@Builder
public class FormFileData {
    private String fileName;
    private byte[] fileContent;
}

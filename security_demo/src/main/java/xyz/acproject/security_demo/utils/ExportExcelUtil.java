package xyz.acproject.security_demo.utils;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import xyz.acproject.lang.exception.ServerException;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
public class ExportExcelUtil {

    /**
     * 简单的列名导出excel
     *
     * @param httpServletResponse
     * @param t
     * @param dataList
     * @param fileName
     * @param <T>
     */
    public static <T> void export(HttpServletResponse httpServletResponse, Class<T> t, List<T> dataList, String fileName) {
        try {
            //设置头居中
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            //设置内容居中
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            //导出
            httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            httpServletResponse.setCharacterEncoding("utf-8");
            fileName = URLEncoder.encode(fileName, "UTF-8");
            httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(httpServletResponse.getOutputStream(), t)
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .sheet(fileName)
                    .doWrite(dataList);
        } catch (Exception e) {
            log.error("导出失败", e);
            throw new ServerException("导出失败");
        }
    }

}

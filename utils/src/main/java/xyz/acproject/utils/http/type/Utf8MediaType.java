package xyz.acproject.utils.http.type;

import okhttp3.MediaType;

/**
 * @author Jane
 * @ClassName Utf8MediaType
 * @Description TODO
 * @date 2022/7/1 16:36
 * @Copyright:2022
 */
public enum Utf8MediaType {
    MEDIA_TYPE_TEXT(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")),

    MEDIA_TYPE_BODY(MediaType.parse("text/plain; charset=utf-8")),
    MEDIA_TYPE_FILE(MediaType.parse("application/octet-stream; charset=utf-8")),
    MEDIA_TYPE_JSON(MediaType.parse("application/json; charset=utf-8")),
    MEDIA_TYPE_XML(MediaType.parse("application/xml; charset=utf-8")),
    MEDIA_TYPE_HTML(MediaType.parse("text/html; charset=utf-8")),
    MEDIA_TYPE_IMAGE(MediaType.parse("image/jpeg")),
    MEDIA_TYPE_AUDIO(MediaType.parse("audio/mp3")),
    MEDIA_TYPE_VIDEO(MediaType.parse("video/mp4")),
    MEDIA_TYPE_PDF(MediaType.parse("application/pdf")),
    MEDIA_TYPE_ZIP(MediaType.parse("application/zip")),
    MEDIA_TYPE_EXCEL(MediaType.parse("application/vnd.ms-excel")),
    MEDIA_TYPE_WORD(MediaType.parse("application/msword")),
    MEDIA_TYPE_PPT(MediaType.parse("application/vnd.ms-powerpoint")),
    MEDIA_TYPE_RAR(MediaType.parse("application/x-rar-compressed")),
    MEDIA_TYPE_7Z(MediaType.parse("application/x-7z-compressed")),
    MEDIA_TYPE_TAR(MediaType.parse("application/x-tar")),
    MEDIA_TYPE_GZIP(MediaType.parse("application/x-gzip")),
    MEDIA_TYPE_BZIP2(MediaType.parse("application/x-bzip2"));
    Utf8MediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public MediaType getMediaType(){
        return this.mediaType;
    }

    private MediaType mediaType;

}

package xyz.acproject.utils.io;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;

/**
 * @author BanqiJane
 * @ClassName FileTools
 * @Description TODO
 * @date 2020年8月10日 下午12:28:27
 * @Copyright:2020 blogs.acproject.xyz Inc. All rights reserved.
 */
public final class JarFileUtils {
    private volatile static JarFileUtils jarFileUtils;


    public static JarFileUtils getJarFileUtils() {

        if (jarFileUtils == null) {
            synchronized (JarFileUtils.class) {
                if (jarFileUtils == null) {
                    jarFileUtils = new JarFileUtils();
                }
            }
        }
        return jarFileUtils;
    }

    public File getBaseJarFile() {
        ApplicationHome home = new ApplicationHome(getClass());
        File jarFile = home.getSource();
        if(jarFile!=null) {
            return jarFile.getParentFile();
        }
        return jarFile;
    }

    public static String getBaseJarPath() {
        File file = getJarFileUtils().getBaseJarFile();
        if(file!=null) {
            return file.getPath();
        }
        return "";
    }

    public static byte[] readFileFromResources(String fileName) {
        Resource resource = new ClassPathResource(fileName);
        InputStream is = null;
        try {
            is = resource.getInputStream();
            return ImageUtils.readInputStream(is);
//			InputStreamReader isr = new InputStreamReader(is);
//			BufferedReader br = new BufferedReader(isr);
//			String data = null;
//			while((data = br.readLine()) != null) {
//			}
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

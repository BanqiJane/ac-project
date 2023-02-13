package xyz.acproject.utils.io;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jane
 * @ClassName FileUtils
 * @Description TODO
 * @date 2021/6/9 11:53
 * @Copyright:2021
 */
public class FileUtils {
    public static byte[] readFileToByte(String filePath) {
        //create file object
        File file = new File(filePath);
        byte fileContent[] = null;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            fileContent = new byte[(int) file.length()];
            fin.read(fileContent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }finally {
            try {
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileContent;
    }

    public static void writeByteToFile(File file,byte[] bytes) {
        if(ArrayUtils.isEmpty(bytes))return;
        InputStream in = new ByteArrayInputStream(bytes);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while((len=in.read(buf))!=-1){
                fileOutputStream.write(buf,0,len);
            }
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }finally {
            try {
                if(fileOutputStream!=null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(in!=null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeByteToFile(String filePath,byte[] bytes) {
        if(ArrayUtils.isEmpty(bytes))return;
        InputStream in = new ByteArrayInputStream(bytes);
        //create file object
        File file = new File(filePath);
        //创建文件
        String path = filePath.substring(0, filePath.lastIndexOf("/"));
        if (!file.exists()) {
            new File(path).mkdir();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while((len=in.read(buf))!=-1){
                fileOutputStream.write(buf,0,len);
            }
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }finally {
            try {
                if(fileOutputStream!=null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(in!=null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  List<String> getAllFilePaths(String rootPath,FileFilter fileFilter){
        List<String> filePaths = new ArrayList<>();
        File file = new File(rootPath);
        File[] files = file.listFiles(fileFilter);
        for(int i=0;i<files.length;i++){
            if(files[i].isDirectory()){
                getAllFilePaths(files[i].getPath(),fileFilter);
            }else{
                filePaths.add(files[i].getPath());
            }
        }
        return filePaths;
    }
    public String getFileText(String file,String charsetName){
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufreader = null;
        try{
            bufreader = new BufferedReader(new FileReader(file));
            char[] by = new char[2048];
            int length =-1;
            while((length=bufreader.read(by))!=-1){
                String str = new String(by,0,length);
                byte[] b = str.getBytes(charsetName);
                stringBuilder.append(new String(b,charsetName));
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(bufreader!=null)
                    bufreader.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
    public static void copyFile(File oldFolder,File newFolder){
        if(newFolder.exists()==false)newFolder.mkdirs();
        File[] files = oldFolder.listFiles();
        for(File file : files){
            if(file.isDirectory()){
                File newFile = new File(newFolder,file.getName());
                copyFile(file,newFile);
            }else{
                File newFile = new File(newFolder,file.getName());
                try{
                    Files.copy(file.toPath(),newFile.toPath());
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createFile(String src) {

        // path表示你所创建文件的路径
        String path = src.substring(0,src.lastIndexOf("/"));
        // fileName表示你创建的文件名
        String fileName = src.substring(src.lastIndexOf("/")+1,src.length());
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        File file = new File(f, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static File bytesToFile(byte[] bytes, String outPath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(outPath+fileName);
            file.setWritable(true, false);
            if (!file.exists()) { //判断文件目录是否存在
                file.mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

}

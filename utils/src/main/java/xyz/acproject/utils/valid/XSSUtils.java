package xyz.acproject.utils.valid;

import java.util.regex.Pattern;

public class XSSUtils {

    public static boolean isValid(String value) {
        if (value != null) {
            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
            if(scriptPattern.matcher(value).matches())
                return false;
            // Avoid anything in a src="" type of e-xpression
            scriptPattern = Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\\'](.*?)[\\\"|\\\']", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if(scriptPattern.matcher(value).matches())
                return false;
            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
            if(scriptPattern.matcher(value).matches())
                return false;
            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<[\r\n| | ]*script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if(scriptPattern.matcher(value).matches())
                return false;
            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if(scriptPattern.matcher(value).matches())
                return false;
            // Avoid e-xpression(...) expressions
            scriptPattern = Pattern.compile("e-xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if(scriptPattern.matcher(value).matches())
                return false;
            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            if(scriptPattern.matcher(value).matches())
                return false;
            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            if(scriptPattern.matcher(value).matches())
                return false;
            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if(scriptPattern.matcher(value).matches())
                return false;
            // Avoid onerror= expressions
            scriptPattern = Pattern.compile("onerror(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if(scriptPattern.matcher(value).matches())
                return false;
            // Avoid onmouse= expressions
            scriptPattern = Pattern.compile("onmouse(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if(scriptPattern.matcher(value).matches())
                return false;
            // Avoid onclick expressions
            scriptPattern = Pattern.compile("onclick(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if(scriptPattern.matcher(value).matches())
                return false;
            // Avoid ajax expressions
            scriptPattern = Pattern.compile("ajax\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if(scriptPattern.matcher(value).matches())
                return false;
            // Avoid function() expressions
            scriptPattern = Pattern.compile("function\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if(scriptPattern.matcher(value).matches())
                return false;

        }
        return true;
    }
}

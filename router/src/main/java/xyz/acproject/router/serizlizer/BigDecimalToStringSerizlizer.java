package xyz.acproject.router.serizlizer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * @author Jane
 * @ClassName BigDecimalToStringSerizlizer
 * @Description TODO
 * @date 2021/3/26 12:23
 * @Copyright:2021
 */
public class BigDecimalToStringSerizlizer implements ObjectSerializer {
    public static final BigDecimalToStringSerizlizer instance = new BigDecimalToStringSerizlizer();
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }
        out.write(((BigDecimal) object).toPlainString());
    }
}

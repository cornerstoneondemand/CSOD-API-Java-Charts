package util;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

/**
 * Created by David on 2/10/15.
 */
public class LowerCaseNaming extends PropertyNamingStrategy{
    @Override
    public String nameForField(MapperConfig
                                       config,
                               AnnotatedField field, String defaultName) {
        return convert(defaultName);

    }
    @Override
    public String nameForGetterMethod(MapperConfig
                                              config,
                                      AnnotatedMethod method, String defaultName) {
        return convert(defaultName);
    }

    @Override
    public String nameForSetterMethod(MapperConfig
                                              config,
                                      AnnotatedMethod method, String defaultName) {
        String a = convert(defaultName);
        return a;
    }

    public String convert(String defaultName )
    {
        char[] arr = defaultName.toCharArray();
        if(arr.length !=0)
        {
            if ( Character.isLowerCase(arr[0])){
                char upper = Character.toUpperCase(arr[0]);
                arr[0] = upper;
            }
        }
        return new StringBuilder().append(arr).toString();
    }

}

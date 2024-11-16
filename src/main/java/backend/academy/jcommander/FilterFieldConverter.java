package backend.academy.jcommander;

import backend.academy.enums.FilterField;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

public class FilterFieldConverter implements IStringConverter<FilterField> {
    @Override
    public FilterField convert(String value) {
        try {
            return FilterField.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ParameterException("Invalid filter field: " + value, e);
        }
    }
}

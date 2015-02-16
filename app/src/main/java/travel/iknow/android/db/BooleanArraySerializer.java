package travel.iknow.android.db;

import com.activeandroid.serializer.TypeSerializer;

/**
 * Created by Pristalov Pavel on 16.02.2015 for IKnowTravel.
 */
public class BooleanArraySerializer extends TypeSerializer
{

    @Override
    public Class<?> getDeserializedType()
    {
        return boolean[].class;
    }

    @Override
    public Class<?> getSerializedType()
    {
        return String.class;
    }

    @Override
    public String serialize(Object data)
    {
        if (data == null)
        {
            return null;
        }
        return toString((boolean[]) data);
    }

    @Override
    public boolean[] deserialize(Object data)
    {
        if (data == null)
        {
            return null;
        }
        return toArray((String) data);
    }


    private boolean[] toArray(String value)
    {
        String[] values = value.split(",");
        boolean[] result = new boolean[values.length];
        for (int i = 0; i < values.length; i++)
        {
            result[i] = Boolean.parseBoolean(values[i]);
        }
        return result;
    }

    private String toString(boolean[] values)
    {
        String result = "";
        for (int i = 0; i < values.length; i++)
        {
            result += String.valueOf(values[i]);
            if (i < values.length - 1)
            {
                result += ",";
            }
        }
        return result;
    }
}

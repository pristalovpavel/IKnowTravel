package travel.iknow.android.data.model;

/**
 * Created by Pristalov Pavel on 13.02.2015 for IKnowTravel.
 */
public class Token
{
    private String token;

    public Token()
    {
        token = "";
    }

    public Token(String token)
    {
        this.token = token;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}

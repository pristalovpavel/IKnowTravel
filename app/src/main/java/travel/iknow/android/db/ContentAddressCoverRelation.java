package travel.iknow.android.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;

import travel.iknow.android.data.model.Category;
import travel.iknow.android.data.model.Content;

/**
 * Created by Pristalov Pavel on 16.02.2015 for IKnowTravel.
 */
@Table(name="content_addresscover")
public class ContentAddressCoverRelation extends Model
{
    @Column(name="content_id")
    private String contentId;

    @Column(name="addresscover_id")
    private String addressCoverId;

    public ContentAddressCoverRelation()
    {
        setContentId("");
        setAddressCoverId("");
    }

    public ContentAddressCoverRelation(Content content)
    {
        this.contentId = content.getContentId();
        this.addressCoverId = content.getAddressCover().getCoverId();
    }

    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    public String getAddressCoverId()
    {
        return addressCoverId;
    }

    public void setAddressCoverId(String addressCoverId)
    {
        this.addressCoverId = addressCoverId;
    }
}

package travel.iknow.android.db;

import android.graphics.AvoidXfermode;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import travel.iknow.android.data.model.Content;

/**
 * Created by Pristalov Pavel on 16.02.2015 for IKnowTravel.
 */
@Table(name="content_articlecover")
public class ContentArticleCoverRelation extends Model
{
    @Column(name="content_id")
    private String contentId;

    @Column(name="articlecover_id")
    private String articleCoverId;

    public ContentArticleCoverRelation()
    {
        setContentId("");
        setArticleCoverId("");
    }

    public ContentArticleCoverRelation(Content content)
    {
        this.contentId = content.getContentId();
        this.articleCoverId = content.getArticleCover().getCoverId();
    }

    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    public String getArticleCoverId()
    {
        return articleCoverId;
    }

    public void setArticleCoverId(String articleCoverId)
    {
        this.articleCoverId = articleCoverId;
    }
}

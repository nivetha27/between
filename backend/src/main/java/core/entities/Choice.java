package core.entities;

public class Choice {

  private Integer choiceId;

  private String caption;

  private String imageUrl;

  private Long votes;

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public Integer getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(Integer choiceId) {
    this.choiceId = choiceId;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public long getVotes() {
    return votes;
  }

  public void setVotes(long votes) {
    this.votes = votes;
  }
}

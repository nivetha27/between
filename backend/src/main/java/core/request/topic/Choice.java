package core.request.topic;

public class Choice {

  private String caption;

  private String imageUrl;

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public core.entities.Choice toCoreChoice(Integer choiceId) {
    core.entities.Choice choice = new core.entities.Choice();
    choice.setChoiceId(choiceId);
    choice.setCaption(getCaption());
    choice.setImageUrl(getImageUrl());
    choice.setVotes(0);
    return choice;
  }
}

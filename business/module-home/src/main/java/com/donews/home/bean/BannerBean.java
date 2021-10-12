package com.donews.home.bean;

import com.donews.common.contract.BaseCustomViewModel;

public class BannerBean extends BaseCustomViewModel {
    int source_type;
    String topic_name;
    String topic_image;
    String topic_id;
    String link;
    String activity_id;

    public String getTopic_image() {
        return topic_image;
    }

    public void setTopic_image(String topic_image) {
        this.topic_image = topic_image;
    }

    public int getSource_type() {
        return source_type;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public String getLink() {
        return link;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setSource_type(int source_type) {
        this.source_type = source_type;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    @Override
    public String toString() {
        return "BannerBean{" +
                "source_type=" + source_type +
                ", topic_name='" + topic_name + '\'' +
                ", topic_id='" + topic_id + '\'' +
                ", link='" + link + '\'' +
                ", activity_id='" + activity_id + '\'' +
                '}';
    }
}

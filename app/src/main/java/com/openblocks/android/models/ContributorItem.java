package com.openblocks.android.models;

public class ContributorItem {
    public String name;
    public String profile_picture_url;
    public String github_link;

    public ContributorItem(String name, String profile_picture_url, String github_link) {
        this.name = name;
        this.profile_picture_url = profile_picture_url;
        this.github_link = github_link;
    }
}

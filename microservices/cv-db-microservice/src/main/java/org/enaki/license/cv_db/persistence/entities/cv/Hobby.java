package org.enaki.license.cv_db.persistence.entities.cv;

public class Hobby {
    public Long hobbyId;
    public Long uid;
    public String name;
    public String description;

    public Hobby(Long hobbyId, Long uid, String name, String description) {
        this.hobbyId = hobbyId;
        this.uid = uid;
        this.name = name;
        this.description = description;
    }
}

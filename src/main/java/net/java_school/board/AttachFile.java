package net.java_school.board;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class AttachFile {

    @Parent Key<FileGroup> theGroup;
    @Id public Long id;

    public String blobKeyString;
    public String owner;

    public String filename;
    public String contentType;
    public long size;
    @Index public Date creation;
    public boolean deletable;
    
    public AttachFile() {
        creation = new Date();
    }

    public AttachFile(String group) {
        this();
        theGroup = Key.create(FileGroup.class, group);
    }
    public Long getId() {
        return id;
    }
    public String getBlobKeyString() {
        return blobKeyString;
    }

    public void setBlobKeyString(String blobKeyString) {
        this.blobKeyString = blobKeyString;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public void setGroup(String group) {
        theGroup = Key.create(FileGroup.class, group);
    }
    

}

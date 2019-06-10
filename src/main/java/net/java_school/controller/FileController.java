package net.java_school.controller;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.Key;
import static com.googlecode.objectify.ObjectifyService.ofy;
import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;

import net.java_school.board.AttachFile;
import net.java_school.board.FileGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/files")
public class FileController {

    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    private static final Logger log = Logger.getLogger(FileController.class.getName());

    @PostMapping("/{group}")
    public String upload(@PathVariable String group, HttpServletRequest req) {
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("attachFile");
        if (blobKeys != null && !blobKeys.isEmpty()) {
            BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
            int size = blobKeys.size();
            for (int i = 0; i < size; i++) {
                BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKeys.get(i));
                AttachFile attachFile = new AttachFile(group);
                attachFile.setBlobKeyString(blobKeys.get(i).getKeyString());
                attachFile.setOwner("John Doe");
                attachFile.setFilename(blobInfo.getFilename());
                attachFile.setContentType(blobInfo.getContentType());
                attachFile.setSize(blobInfo.getSize());
                ofy().save().entity(attachFile).now();
            }
        }

        return "redirect:/";
    }

    @GetMapping("/{group}")
    @ResponseBody
    public List<AttachFile> getFilenames(@PathVariable String group) {

        Key<FileGroup> theGroup = Key.create(FileGroup.class, group);
        List<AttachFile> attachFiles = ofy()
                .load()
                .type(AttachFile.class)
                .ancestor(theGroup)
                .order("creation")
                .list();

        attachFiles.forEach((attachFile) -> {
            attachFile.setDeletable(true);
        });

        return attachFiles;

    }

    @DeleteMapping("/{group}/{id}")
    public ResponseEntity<String> del(@PathVariable String group, @PathVariable Long id) {
        Key<FileGroup> theGroup = Key.create(FileGroup.class, group);
        Key<AttachFile> key = Key.create(theGroup, AttachFile.class, id);

        AttachFile attachFile = ofy()
                .load()
                .key(key)
                .now();

        String blobKeyString = attachFile.getBlobKeyString();
        BlobKey blobKey = new BlobKey(blobKeyString);
        blobstoreService.delete(blobKey);

        ofy().delete().key(key).now();

        return new ResponseEntity<>("OK", HttpStatus.NO_CONTENT);

    }

    @GetMapping("/{group}/{id}")
    public void download(@PathVariable String group, @PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Key<FileGroup> theGroup = Key.create(FileGroup.class, group);
        Key<AttachFile> key = Key.create(theGroup, AttachFile.class, id);

        AttachFile attachFile = ofy()
                .load()
                .key(key)
                .now();

        String blobKeyString = attachFile.getBlobKeyString();
        BlobKey blobKey = new BlobKey(blobKeyString);
        BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
        String filename = URLEncoder.encode(blobInfo.getFilename(), "UTF-8");
        resp.setContentType("application/octet-stream");
        resp.addHeader("Content-Disposition", "attachment; filename*=UTF-8''" + filename);
        blobstoreService.serve(blobKey, resp);

    }

}

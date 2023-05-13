package training.doctor.management.service.helper;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import training.doctor.management.model.DocDTO;

import java.io.IOException;
import java.util.Optional;

@Service
public class StorageService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations operations;

    public String fileAdd(String title, MultipartFile file) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("type", file.getContentType());
        metaData.put("title", title);
        ObjectId id = gridFsTemplate.store(
                file.getInputStream(), file.getName(), file.getContentType(), metaData);
        return id.toString();
    }

    public DocDTO getFile(String id) throws IllegalStateException, IOException {
        Optional<GridFSFile> file = Optional.ofNullable(gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id))));
        if(file.isPresent()){
            DocDTO data = new DocDTO();
            data.setTitle(file.get().getMetadata().get("title").toString());
            data.setStream(operations.getResource(file.get()).getInputStream());
            return data;}
        throw  new RuntimeException("File Not Found");
    }
}

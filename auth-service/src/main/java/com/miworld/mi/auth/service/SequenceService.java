package com.miworld.mi.auth.service;

import com.miworld.mi.auth.model.SequenceId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class SequenceService {
    @Autowired
    private MongoOperations mongoOperations;

    public Long getNextSequenceId(String key) {

        Query query = new Query(Criteria.where("_id").is(key));

        Update update = new Update();
        update.inc("seq", 1);

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        SequenceId seqId = mongoOperations.findAndModify(query, update, options, SequenceId.class);


        if (seqId == null) {
            seqId = mongoOperations.save(new SequenceId(key,1L));
            if(seqId == null)
                throw new RuntimeException("Unable to get sequence id for key : " + key);
        }

        return seqId.getSeq();

    }
}

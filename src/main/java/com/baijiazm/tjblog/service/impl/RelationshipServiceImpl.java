package com.baijiazm.tjblog.service.impl;

import com.baijiazm.tjblog.mapper.RelationshipMapper;
import com.baijiazm.tjblog.model.entity.RelationshipEntity;
import com.baijiazm.tjblog.service.IRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("relationshipService")
public class RelationshipServiceImpl implements IRelationshipService {

    @Autowired
    private RelationshipMapper relationshipMapper;

    @Override
    public void insertRelationship(RelationshipEntity relationshipEntity) {
        relationshipMapper.insert(relationshipEntity);
    }

    @Override
    public Long selectCountById(Integer cid, Integer mid) {
        return relationshipMapper.selectCountById(cid, mid);
    }

    @Override
    public void deleteById(Integer cid, Integer mid) {
        if (cid == null && mid == null) {
            return;
        } else if (cid != null) {
            if (mid != null) {
                relationshipMapper.deleteByCMId(cid, mid);
            }
            relationshipMapper.deleteByCId(cid);
        } else {
            relationshipMapper.deleteByMId(mid);
        }
    }

//    @Override
//    public List<RelationshipVoKey> getRelationshipById(Integer cid, Integer mid) {
//        RelationshipVoExample relationshipVoExample = new RelationshipVoExample();
//        RelationshipVoExample.Criteria criteria = relationshipVoExample.createCriteria();
//        if (cid != null) {
//            criteria.andCidEqualTo(cid);
//        }
//        if (mid != null) {
//            criteria.andMidEqualTo(mid);
//        }
//        return relationshipVoMapper.selectByExample(relationshipVoExample);
//    }
}

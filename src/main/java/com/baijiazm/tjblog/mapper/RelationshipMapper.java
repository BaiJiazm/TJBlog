package com.baijiazm.tjblog.mapper;

import com.baijiazm.tjblog.model.entity.RelationshipEntity;
import com.sun.tracing.dtrace.ProviderAttributes;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface RelationshipMapper {

    String insert = "insert into t_relationship (`content_id`, `meta_id`) " +
            " values( #{relation.contentId}, #{relation.metaId})";

    @Insert(insert)
    void insert(@Param("relation") RelationshipEntity relationshipEntity);


    String selectCountById = "select count(*) from t_relationship where content_id=#{cid} and meta_id=#{mid}";

    @Select(selectCountById)
    Long selectCountById(@Param("cid") Integer cid, @Param("mid") Integer mid);


    String deleteByCMId = "delete from t_relationship where content_id=#{cid} and meta_id=#{mid}";

    @Delete(deleteByCMId)
    void deleteByCMId(@Param("cid") Integer cid, @Param("mid") Integer mid);


    String deleteByCId = "delete from t_relationship where content_id=#{cid}";

    @Delete(deleteByCId)
    void deleteByCId(@Param("cid") Integer cid);


    String deleteByMId = "delete from t_relationship where meta_id=#{mid}";

    @Delete(deleteByMId)
    void deleteByMId(@Param("mid") Integer mid);
}
